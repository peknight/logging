package com.peknight.logging.natchez.syntax

import cats.Show
import cats.data.EitherT
import cats.effect.{Clock, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.show.*
import com.peknight.error.syntax.applicativeError.asError
import com.peknight.error.{Error, Success}
import com.peknight.log4cats.ext.syntax.logger.log as _log
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherTSyntax:
  extension [F[_], A, B] (eitherT: EitherT[F, A, B])
    def log[Param](name: String = "", message: String = "", param: Option[Param] = None,
                   successLevel: LogLevel = LogLevel.Info, errorLevel: LogLevel = LogLevel.Error)
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[B])
    : EitherT[F, Error, B] =
      given CanEqual[LogLevel, LogLevel] = CanEqual.derived
      val run =
        for
          startTime <- Clock[F].monotonic
          either <- Option(name).filter(_.nonEmpty).fold(eitherT.value)(name => trace.span(name)(eitherT.value))
          endTime <- Clock[F].monotonic
          traceId <- trace.traceId.map(_.getOrElse(""))
          duration = endTime - startTime
          error = either.fold(Error.apply, _ => Success)
          value = either.toOption
          level = if either.isRight then successLevel else errorLevel
          _ <- logger._log(level, error.throwable)(s"$traceId|$name|$duration|${error.errorType}|${error.message}|${param.map(_.show).getOrElse("")}|${value.map(_.show).getOrElse("")}|$message")
        yield
          value.toRight(error)
      EitherT(run.asError.map(_.flatten))
  end extension
end EitherTSyntax
