package it.unibo.controller.subscribers

import it.unibo.controller.{
  logger,
  CancelDiscardMessage,
  CandidateCardToPlayMessage,
  ConfirmDiscardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewMessage,
  UpdateGamePhase,
  SetupActionPhase,
  ToggleCardInListMessage
}
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.view.TurnViewController
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber

import scala.concurrent.Future

class InternalViewMessageHandler(gameBoardController: TurnViewController)
    extends Subscriber[InternalViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: InternalViewMessage): Future[Ack] =
    msg match
      case UpdateGamePhase(phase: GamePhase)   =>
        logger.info("Received UpdateGamePhase")
        gameBoardController.updateGamePhase(phase)
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
      case CandidateCardToPlayMessage(cardId)  =>
        logger.info(s"Received CandidateCardToPlayMessage with cardId: $cardId")
        gameBoardController.candidateCardToPlay(cardId)
    Continue

  override def onError(ex: Throwable): Unit =
    logger.error(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      logger.error(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    logger.error(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
