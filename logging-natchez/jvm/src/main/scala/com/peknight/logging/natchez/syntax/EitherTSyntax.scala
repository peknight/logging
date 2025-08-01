package com.peknight.logging.natchez.syntax

import cats.Show
import cats.data.EitherT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.faeLiftET
import com.peknight.logging.syntax.eitherT.log as lLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherTSyntax:
  extension [F[_], A, B] (eitherT: EitherT[F, A, B])
    def log[Param](name: String = "", param: Option[Param] = None, message: String = "",
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[B])
    : EitherT[F, Error, B] =
      for 
        traceId <- trace.traceId.faeLiftET.map(_.getOrElse(""))
        value <- eitherT.lLog[Param](name, param, traceId, message, successLevel, errorLevel)
      yield
        value
  end extension
end EitherTSyntax
object EitherTSyntax extends EitherTSyntax
