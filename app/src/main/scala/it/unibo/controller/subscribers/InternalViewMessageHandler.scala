package it.unibo.controller.subscribers

import it.unibo.controller.{
  CancelDiscardMessage,
  ConfirmDiscardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewMessage,
  ToggleCardInListMessage
}
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
      case InitializeDiscardProcedureMessage() =>
        println("Received InitializeDiscardProcedureMessage")
        gameBoardController.initDiscardProcedure()
      case ToggleCardInListMessage(cardId)     =>
        println(s"Received ToggleCardInListMessage with cardId: $cardId")
        gameBoardController.toggleCardInDiscardList(cardId)
      case ConfirmDiscardMessage()             =>
        println("Received ConfirmDiscardMessage")
        gameBoardController.confirmDiscard()
      case CancelDiscardMessage()              =>
        println("Received CancelDiscardMessage")
        gameBoardController.cancelDiscard()

    Continue

  override def onError(ex: Throwable): Unit =
    println(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      println(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    println(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
