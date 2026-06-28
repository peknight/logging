package com.peknight.logging.natchez.syntax

import cats.syntax.flatMap.*
import cats.data.Ior
import cats.syntax.functor.*
import cats.{Monad, Show}
import com.peknight.logging.syntax.ior.log as iLog
import natchez.Trace
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.extras.LogLevel

import scala.concurrent.duration.Duration

trait IorSyntax:
  extension [A, B] (ior: Ior[A, B])
    def log[F[_], Param](name: String = "", param: Option[Param] = None, message: String = "",
                         duration: Option[Duration] = None,
                         successLevel: Option[LogLevel] = Some(LogLevel.Info),
                         errorLevel: Option[LogLevel] = Some(LogLevel.Error))
                        (using monad: Monad[F], logger: Logger[F], trace: Trace[F], paramShow: Show[Param],
                         valueShow: Show[B]): F[Unit] =
      for
        traceId <- trace.traceId
        _ <- ior.iLog(name, param, traceId.getOrElse(""), message, duration, successLevel, errorLevel)
      yield
        ()
  end extension
end IorSyntax
object IorSyntax extends IorSyntax
