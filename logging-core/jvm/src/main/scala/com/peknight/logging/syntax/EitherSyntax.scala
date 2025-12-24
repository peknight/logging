package com.peknight.logging.syntax

import cats.syntax.applicative.*
import cats.{Applicative, Show}
import com.peknight.error.Success
import com.peknight.error.syntax.either.asError
import com.peknight.log4cats.syntax.logger.log as lLog
import com.peknight.logging.message.either as msg
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

import scala.concurrent.duration.Duration

trait EitherSyntax:
  extension [A, B] (either: Either[A, B])
    def log[F[_], Param](name: String = "", param: Option[Param] = None, traceId: String = "", message: String = "",
                         duration: Option[Duration] = None,
                         successLevel: Option[LogLevel] = Some(LogLevel.Info),
                         errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                        (using applicative: Applicative[F], logger: Logger[F], paramShow: Show[Param], valueShow: Show[B])
    : F[Unit] =
      val e = either.asError
      val error = e.left.getOrElse(Success)
      val level = if either.isRight then successLevel else errorLevel
      level.fold(().pure)(level => logger.lLog(level, error.throwable)(msg(e, name, param, traceId, message, duration)))
  end extension
end EitherSyntax
object EitherSyntax extends EitherSyntax
