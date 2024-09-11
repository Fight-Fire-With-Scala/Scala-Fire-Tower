package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.Message
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber

import scala.concurrent.Future

trait BaseSubscriber[T <: Message] extends Subscriber[T]:
  override def scheduler: Scheduler = Scheduler.global

  protected val logger: Logger

  override def onNext(msg: T): Future[Ack] =
    logger.info(s"[Message] ${msg.getClass.getName}")
    onMessageReceived(msg)
    Continue

  override def onError(ex: Throwable): Unit = onErrorHandler(ex)

  override def onComplete(): Unit = onCompleteHandler()

  protected def onMessageReceived(msg: T): Unit

  private def onErrorHandler(ex: Throwable): Unit =
    logger.error(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      logger.error(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    logger.error(s"Full description: ${ex.toString}")

  private def onCompleteHandler(): Unit = logger.info(s"Received final event")
