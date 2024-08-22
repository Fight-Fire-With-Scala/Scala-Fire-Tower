package it.unibo.controller.subscribers

import it.unibo.controller.{InitializeDiscardProcedureMessage, InternalViewMessage}
import it.unibo.view.GameBoardController
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber

import scala.concurrent.Future

class InternalViewMessageHandler(gameBoardController: GameBoardController)
    extends Subscriber[InternalViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: InternalViewMessage): Future[Ack] =
    msg match
      case InitializeDiscardProcedureMessage =>
        println("Received InitializeDiscardProcedureMessage")
    Continue

  override def onError(ex: Throwable): Unit =
    println(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      println(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    println(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
