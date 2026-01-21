package com.peknight.logging.syntax

import cats.Show
import cats.data.IorT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.logging.syntax.iorF.log as fLog
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait IorTSyntax:
  extension [F[_], A, B] (eitherT: IorT[F, A, B])
    def log[Param](name: String = "", param: Option[Param] = None, traceId: String = "",
                   startMessage: String = "", message: String = "",
                   startLevel: Option[LogLevel] = None,
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error),
                   startLogParam: Boolean = true,
                   logParam: Boolean = true)
                  (using sync: Sync[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : IorT[F, Error, B] =
      IorT(eitherT.value.fLog[Param](name, param, traceId, startMessage, message, startLevel, successLevel, errorLevel,
        startLogParam, logParam))
  end extension
end IorTSyntax
object IorTSyntax extends IorTSyntax
