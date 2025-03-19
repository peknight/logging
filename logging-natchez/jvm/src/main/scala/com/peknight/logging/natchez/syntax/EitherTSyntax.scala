package com.peknight.logging.natchez.syntax

import cats.Show
import cats.data.EitherT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asError
import com.peknight.logging.error.syntax.eitherT.log as _log
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherTSyntax:
  extension [F[_], A, B] (eitherT: EitherT[F, A, B])
    def log[Param](name: String = "", message: String = "", param: Option[Param] = None,
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[B])
    : EitherT[F, Error, B] =
      for 
        traceId <- EitherT(trace.traceId.asError).map(_.getOrElse(""))
        value <- eitherT._log[Param](traceId, name, message, param, successLevel, errorLevel)
      yield
        value
  end extension
end EitherTSyntax
object EitherTSyntax extends EitherTSyntax
