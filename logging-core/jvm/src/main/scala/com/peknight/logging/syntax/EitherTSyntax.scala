package com.peknight.logging.syntax

import cats.Show
import cats.data.EitherT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.logging.syntax.eitherF.log as fLog
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherTSyntax:
  extension [F[_], A, B] (eitherT: EitherT[F, A, B])
    def log[Param](name: String = "", param: Option[Param] = None, traceId: String = "", message: String = "",
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                  (using sync: Sync[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : EitherT[F, Error, B] =
      EitherT(eitherT.value.fLog[Param](name, param, traceId, message, successLevel, errorLevel))
  end extension
end EitherTSyntax
object EitherTSyntax extends EitherTSyntax
