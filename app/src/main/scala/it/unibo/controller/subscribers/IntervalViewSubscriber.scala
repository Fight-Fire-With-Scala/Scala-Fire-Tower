package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.view.ViewController
import it.unibo.controller.{
  CancelDiscardMessage,
  CandidateCardToPlayMessage,
  ConfirmDiscardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewMessage,
  ToggleCardInListMessage,
  UpdateGamePhaseView
}
import it.unibo.model.gameboard.GamePhase

final class IntervalViewSubscriber(turnViewController: ViewController)
    extends BaseSubscriber[InternalViewMessage]:

  override val logger: Logger = Logger("View -> IntervalView")

  override protected def onMessageReceived(msg: InternalViewMessage): Unit = msg match
    case UpdateGamePhaseView(phase: GamePhase) => turnViewController.updateGamePhase(phase)
    case InitializeDiscardProcedureMessage()   => turnViewController.initDiscardProcedure()
    case ToggleCardInListMessage(cardId)       => turnViewController.toggleCardInDiscardList(cardId)
    case ConfirmDiscardMessage()               => turnViewController.confirmDiscard()
    case CancelDiscardMessage()                => turnViewController.cancelDiscard()
    case CandidateCardToPlayMessage(cardId)    => turnViewController.candidateCardToPlay(cardId)
