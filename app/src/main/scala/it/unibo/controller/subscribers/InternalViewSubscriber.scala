package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.view.ViewController
import it.unibo.controller.{
  CancelDiscardMessage,
  CandidateCardToPlayMessage,
  ConfirmDiscardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewMessage,
  ToggleCardInListMessage
}

final class InternalViewSubscriber(controller: ViewController)
    extends BaseSubscriber[InternalViewMessage]:

  override val logger: Logger = Logger("View -> IntervalView")

  override protected def onMessageReceived(msg: InternalViewMessage): Unit = msg match
    case InitializeDiscardProcedureMessage()   => controller.initDiscardProcedure()
    case ToggleCardInListMessage(cardId)       => controller.toggleCardInDiscardList(cardId)
    case ConfirmDiscardMessage()               => controller.confirmDiscard()
    case CancelDiscardMessage()                => controller.cancelDiscard()
    case CandidateCardToPlayMessage(cardId)    => controller.candidateCardToPlay(cardId)
