package com.peknight.logging.syntax

import cats.Show
import cats.effect.{Clock, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.option.*
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asError
import com.peknight.error.syntax.either.asError
import com.peknight.logging.syntax.either.log as eLog
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherFSyntax:
  extension [F[_], A, B] (f: F[Either[A, B]])
    def log[Param](name: String = "", param: Option[Param] = None, traceId: String = "", message: String = "",
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                  (using sync: Sync[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : F[Either[Error, B]] =
      val run =
        for
          startTime <- Clock[F].monotonic
          either <- f.asError.map(_.flatMap(_.asError))
          endTime <- Clock[F].monotonic
          duration = (endTime - startTime).some
          _ <- either.eLog(name, param, traceId, message, duration, successLevel, errorLevel)
        yield
          either
      run.asError.map(_.flatten)
  end extension
end EitherFSyntax
object EitherFSyntax extends EitherFSyntax
