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
import it.unibo.controller.{logger, SetupActionPhase}

class InternalViewMessageHandler(gameBoardController: GameBoardController)
    extends Subscriber[InternalViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: InternalViewMessage): Future[Ack] =
    msg match
      case SetupActionPhase() =>
        logger.info("Received SetupActionPhase")
        gameBoardController.handleStartActionPhase()
      case InitializeDiscardProcedureMessage() =>
        logger.info("Received InitializeDiscardProcedureMessage")
        gameBoardController.initDiscardProcedure()
      case ToggleCardInListMessage(cardId)     =>
        logger.info(s"Received ToggleCardInListMessage with cardId: $cardId")
        gameBoardController.toggleCardInDiscardList(cardId)
      case ConfirmDiscardMessage()             =>
        logger.info("Received ConfirmDiscardMessage")
        gameBoardController.confirmDiscard()
      case CancelDiscardMessage()              =>
        logger.info("Received CancelDiscardMessage")
        gameBoardController.cancelDiscard()

    Continue

  override def onError(ex: Throwable): Unit =
    logger.error(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      logger.error(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    logger.error(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
