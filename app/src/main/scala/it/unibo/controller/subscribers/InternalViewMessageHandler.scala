package it.unibo.controller.subscribers

import it.unibo.controller.{CancelDiscardMessage, CandidateCardToPlayMessage, ConfirmDiscardMessage, InitializeDiscardProcedureMessage, InternalViewMessage, ToggleCardInListMessage, UpdateGamePhaseView, logger}
import it.unibo.model.gameboard.GamePhase
import it.unibo.view.TurnViewController
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber

import scala.concurrent.Future

class InternalViewMessageHandler(turnViewController: TurnViewController)
    extends Subscriber[InternalViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: InternalViewMessage): Future[Ack] =
    msg match
      case UpdateGamePhaseView(phase: GamePhase)   =>
        logger.info("Received UpdateGamePhase")
        turnViewController.updateGamePhase(phase)
      case InitializeDiscardProcedureMessage() =>
        logger.info("Received InitializeDiscardProcedureMessage")
        turnViewController.initDiscardProcedure()
      case ToggleCardInListMessage(cardId)     =>
        logger.info(s"Received ToggleCardInListMessage with cardId: $cardId")
        turnViewController.toggleCardInDiscardList(cardId)
      case ConfirmDiscardMessage()             =>
        logger.info("Received ConfirmDiscardMessage")
        turnViewController.confirmDiscard()
      case CancelDiscardMessage()              =>
        logger.info("Received CancelDiscardMessage")
        turnViewController.cancelDiscard()
      case CandidateCardToPlayMessage(cardId)  =>
        logger.info(s"Received CandidateCardToPlayMessage with cardId: $cardId")
        turnViewController.candidateCardToPlay(cardId)
    Continue

  override def onError(ex: Throwable): Unit =
    logger.error(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      logger.error(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    logger.error(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
