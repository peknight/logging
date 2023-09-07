package com.peknight.logging

import cats.Monoid
import cats.data.NonEmptyList

sealed trait Log extends Serializable derives CanEqual

object Log:
  case object NoLog extends Log

  case class LogMessage(
                         message: String,
                         throwable: Option[Throwable] = None,
                         level: LogLevel = LogLevel.Info,
                         logger: Option[String | Class[_]] = None
                       ) extends Log
  case class Logs(logs: NonEmptyList[LogMessage]) extends Log

  object Logs:
    def apply(head: LogMessage, tail: List[LogMessage]): Logs = Logs(NonEmptyList(head, tail))
    def apply(head: LogMessage, tail: LogMessage*): Logs = Logs(NonEmptyList.of(head, tail*))
  end Logs

  def empty: Log = NoLog

  def apply(
             message: String,
             throwable: Option[Throwable] = None,
             level: LogLevel = LogLevel.Info,
             logger: Option[String | Class[_]] = None
           ): Log =
    LogMessage(message, throwable, level, logger)

  def apply(logs: NonEmptyList[LogMessage]): Log = Logs(logs)
  def apply(head: LogMessage, tail: List[LogMessage]): Log = Logs(head, tail)
  def apply(head: LogMessage, tail: LogMessage*): Log = Logs(head, tail*)

  given Monoid[Log] with
      def empty: Log = NoLog
      def combine(x: Log, y: Log): Log = (x, y) match
        case (NoLog, yLog) => yLog
        case (xLog, NoLog) => xLog
        case (Logs(xLogs), Logs(yLogs)) => Logs(xLogs ++ yLogs.toList)
        case (Logs(xLogs), yLog: LogMessage) =>Logs(xLogs.head, xLogs.tail :+ yLog)
        case (xLog: LogMessage, Logs(yLogs)) => Logs(xLog, yLogs.toList)
        case (xLog: LogMessage, yLog: LogMessage) => Logs(xLog, yLog)
  end given
end Log
