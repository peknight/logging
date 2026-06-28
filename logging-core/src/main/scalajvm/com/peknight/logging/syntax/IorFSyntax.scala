package com.peknight.logging.syntax

import cats.Show
import cats.data.Ior
import cats.effect.{Clock, Sync}
import cats.syntax.applicative.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.option.*
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.aeiAsError
import com.peknight.log4cats.syntax.logger.log as lLog
import com.peknight.logging.message.start
import com.peknight.logging.syntax.ior.log as iLog
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

trait IorFSyntax:
  extension [F[_], A, B] (f: F[Ior[A, B]])
    def log[Param](name: String = "", param: Option[Param] = None, traceId: String = "",
                   startMessage: String = "", message: String = "",
                   startLevel: Option[LogLevel] = None,
                   successLevel: Option[LogLevel] = Some(LogLevel.Info),
                   errorLevel: Option[LogLevel] = Some(LogLevel.Error),
                   startLogParam: Boolean = true,
                   logParam: Boolean = true)
                  (using sync: Sync[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : F[Ior[Error, B]] =
      val run =
        for
          _ <- startLevel.fold(().pure[F])(level =>
            logger.lLog(level)(start[Param](name, param.filter(_ => startLogParam), traceId, startMessage)))
          startTime <- Clock[F].monotonic
          ior <- f.aeiAsError
          endTime <- Clock[F].monotonic
          duration = (endTime - startTime).some
          _ <- ior.iLog(name, param, traceId, message, duration, successLevel, errorLevel)
        yield
          ior
      run.aeiAsError
  end extension
end IorFSyntax
object IorFSyntax extends IorFSyntax
