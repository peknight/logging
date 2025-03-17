package com.peknight.logging.natchez.syntax

import cats.Show
import cats.data.EitherT
import cats.effect.Sync
import com.peknight.error.Error
import com.peknight.logging.natchez.syntax.eitherT.log as tLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait EitherFSyntax:
  extension [F[_], A, B] (f: F[Either[A, B]])
    def log[Param](name: String = "", message: String = "", param: Option[Param] = None,
                   successLevel: LogLevel = LogLevel.Info, errorLevel: LogLevel = LogLevel.Error)
                  (using sync: Sync[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param], valueShow: Show[B])
    : F[Either[Error, B]] =
      EitherT(f).tLog[Param](name, message, param, successLevel, errorLevel).value
  end extension
end EitherFSyntax
object EitherFSyntax extends EitherFSyntax
