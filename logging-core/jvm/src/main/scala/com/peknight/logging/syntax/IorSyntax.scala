package com.peknight.logging.syntax

import cats.data.Ior
import cats.syntax.applicative.*
import cats.{Applicative, Show}
import com.peknight.error.Success
import com.peknight.error.syntax.ior.asError
import com.peknight.log4cats.ext.syntax.logger.log as lLog
import com.peknight.logging.message.ior as msg
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

import scala.concurrent.duration.Duration

trait IorSyntax:
  extension [A, B] (ior: Ior[A, B])
    def log[F[_], Param](name: String = "", param: Option[Param] = None, traceId: String = "", message: String = "",
                         duration: Option[Duration] = None,
                         successLevel: Option[LogLevel] = Some(LogLevel.Info),
                         errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                        (using applicative: Applicative[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : F[Unit] =
      val i = ior.asError
      val error = i.left.getOrElse(Success)
      val level = if i.fold(_.success, _ => true, (e, _) => e.success) then successLevel else errorLevel
      level.fold(().pure)(level => logger.lLog(level, error.throwable)(msg(i, name, param, traceId, message, duration)))
  end extension
end IorSyntax
object IorSyntax extends IorSyntax
