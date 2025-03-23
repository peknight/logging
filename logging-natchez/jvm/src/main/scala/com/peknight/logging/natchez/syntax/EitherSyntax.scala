package com.peknight.logging.natchez.syntax

import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Monad, Show}
import com.peknight.logging.syntax.either.log as eLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

import scala.concurrent.duration.Duration

trait EitherSyntax:
  extension [A, B] (either: Either[A, B])
    def log[F[_], Param](name: String = "", message: String = "",
                         duration: Option[Duration] = None,
                         param: Option[Param] = None,
                         successLevel: Option[LogLevel] = Some(LogLevel.Info),
                         errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                        (using monad: Monad[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param],
                         valueShow: Show[B]): F[Unit] =
      for
        traceId <- trace.traceId
        _ <- either.eLog(traceId.getOrElse(""), name, message, duration, param, successLevel, errorLevel)
      yield
        ()
  end extension
end EitherSyntax
object EitherSyntax extends EitherSyntax
