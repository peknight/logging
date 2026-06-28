package com.peknight.logging.natchez.syntax

import cats.Show
import cats.effect.Sync
import cats.syntax.applicativeError.*
import cats.syntax.monadError.*
import com.peknight.logging.natchez.syntax.eitherF.log as fLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait MonadErrorSyntax:
  extension [F[_], A] (fa: F[A])
    def log[Param](name: String = "", param: Option[Param] = None,
                   startMessage: String = "", message: String = "",
                   startLevel: Option[LogLevel] = None,
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error),
                   startLogParam: Boolean = true,
                   logParam: Boolean = true)
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[A])
    : F[A] =
      fa.attempt.fLog[Param](name, param, startMessage, message, startLevel, successLevel, errorLevel, startLogParam,
        logParam).rethrow
  end extension
end MonadErrorSyntax
object MonadErrorSyntax extends MonadErrorSyntax
