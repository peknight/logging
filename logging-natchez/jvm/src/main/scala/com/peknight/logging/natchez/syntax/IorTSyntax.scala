package com.peknight.logging.natchez.syntax

import cats.Show
import cats.data.IorT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asIT
import com.peknight.logging.syntax.iorT.log as lLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait IorTSyntax:
  extension [F[_], A, B] (iorT: IorT[F, A, B])
    def log[Param](name: String = "", param: Option[Param] = None, message: String = "",
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[B])
    : IorT[F, Error, B] =
      for 
        traceId <- trace.traceId.asIT.map(_.getOrElse(""))
        value <- iorT.lLog[Param](name, param, traceId, message, successLevel, errorLevel)
      yield
        value
  end extension
end IorTSyntax
object IorTSyntax extends IorTSyntax
