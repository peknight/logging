package com.peknight.logging.natchez.syntax

import cats.Show
import cats.data.{IorT, Ior}
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.logging.natchez.syntax.iorT.log as tLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait IorFSyntax:
  extension [F[_], A, B] (f: F[Ior[A, B]])
    def log[Param](name: String = "", param: Option[Param] = None,
                   startMessage: String = "", message: String = "",
                   startLevel: Option[LogLevel] = None,
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error),
                   startLogParam: Boolean = true,
                   logParam: Boolean = true)
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[B])
    : F[Ior[Error, B]] =
      IorT(f).tLog[Param](name, param, startMessage, message, startLevel, successLevel, errorLevel, startLogParam,
        logParam).value
  end extension
end IorFSyntax
object IorFSyntax extends IorFSyntax
