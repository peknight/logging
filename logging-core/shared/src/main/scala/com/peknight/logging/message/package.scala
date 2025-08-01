package com.peknight.logging

import cats.Show
import cats.data.Ior
import cats.syntax.show.*
import com.peknight.error.{Error, Success}

import scala.concurrent.duration.*

package object message:
  def either[A: Show, Param: Show](either: Either[Error, A],
                                   name: String = "",
                                   param: Option[Param] = None,
                                   traceId: String = "",
                                   message: String = "",
                                   duration: Option[Duration] = None): String =
    val error = either.fold(identity, _ => Success)
    val value = either.toOption
    apply[A, Param](error, value, name, param, traceId, message, duration)

  def ior[A: Show, Param: Show](ior: Ior[Error, A],
                                name: String = "",
                                param: Option[Param] = None,
                                traceId: String = "",
                                message: String = "",
                                duration: Option[Duration] = None): String =
    val error = ior.fold(identity, _ => Success, (e, _) => e)
    val value = ior.toOption
    apply[A, Param](error, value, name, param, traceId, message, duration)

  def apply[A: Show, Param: Show](error: Error, value: Option[A],
                                  name: String = "",
                                  param: Option[Param] = None,
                                  traceId: String = "",
                                  message: String = "",
                                  duration: Option[Duration] = None): String =
    s"$traceId|$name|${duration.map(format).getOrElse("")}|${error.errorType}|${error.message}|${error.showValue.getOrElse("")}|${param.map(_.show).getOrElse("")}|${value.map(_.show).getOrElse("")}|$message"

  private def format(duration: Duration): String =
    if !duration.isFinite then "∞"
    else if duration > 10.days then s"${duration.toDays}days#${duration.toNanos}"
    else if duration > 10.hours then s"${duration.toHours}hours#${duration.toNanos}"
    else if duration > 10.minutes then s"${duration.toMinutes}minutes#${duration.toNanos}"
    else if duration > 10.seconds then s"${duration.toSeconds}seconds#${duration.toNanos}"
    else if duration > 10.millis then s"${duration.toMillis}millis#${duration.toNanos}"
    else if duration > 10.micros then s"${duration.toMicros}micros#${duration.toNanos}"
    else s"${duration.toNanos}ns#${duration.toNanos}"
end message
