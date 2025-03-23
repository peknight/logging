package com.peknight.logging

import cats.Show
import cats.syntax.show.*
import com.peknight.error.{Error, Success}

import scala.concurrent.duration.Duration

package object message:
  def apply[A: Show, Param: Show](either: Either[Error, A],
                                  traceId: String = "",
                                  name: String = "",
                                  message: String = "",
                                  duration: Option[Duration] = None,
                                  param: Option[Param] = None): String =
    val error = either.fold(Error.apply, _ => Success)
    val value = either.toOption
    s"$traceId|$name|${duration.map(_.toString).getOrElse("")}|${error.errorType}|${error.message}|${error.showValue.getOrElse("")}|${param.map(_.show).getOrElse("")}|${value.map(_.show).getOrElse("")}|$message"
end message
