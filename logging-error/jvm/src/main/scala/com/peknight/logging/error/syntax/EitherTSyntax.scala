package com.peknight.logging.error.syntax

import cats.Show
import cats.data.EitherT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.logging.error.syntax.eitherF.log as fLog
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherTSyntax:
  extension [F[_], A, B] (eitherT: EitherT[F, A, B])
    def log[Param](traceId: String = "", name: String = "", message: String = "", param: Option[Param] = None,
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                  (using sync: Sync[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : EitherT[F, Error, B] =
      EitherT(eitherT.value.fLog[Param](traceId, name, message, param, successLevel, errorLevel))
  end extension
end EitherTSyntax
object EitherTSyntax extends EitherTSyntax
