package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.view.InternalViewController
import it.unibo.controller.{
  CancelDiscardMessage,
  CandidateCardToPlayMessage,
  ConfirmDiscardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewMessage,
  ToggleCardInListMessage
}

final class InternalViewSubscriber(viewController: InternalViewController)
    extends BaseSubscriber[InternalViewMessage]:

  override val logger: Logger = Logger("View -> IntervalView")

  override protected def onMessageReceived(msg: InternalViewMessage): Unit = msg match
    case InitializeDiscardProcedureMessage()   => viewController.initDiscardProcedure()
    case ToggleCardInListMessage(cardId)       => viewController.toggleCardInDiscardList(cardId)
    case ConfirmDiscardMessage()               => viewController.confirmDiscard()
    case CancelDiscardMessage()                => viewController.cancelDiscard()
    case CandidateCardToPlayMessage(cardId)    => viewController.candidateCardToPlay(cardId)
