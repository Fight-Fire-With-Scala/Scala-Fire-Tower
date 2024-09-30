package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.CancelDiscardMessage
import it.unibo.controller.CandidateCardToPlayMessage
import it.unibo.controller.ConfirmDiscardMessage
import it.unibo.controller.InitializeDiscardProcedureMessage
import it.unibo.controller.InternalViewMessage
import it.unibo.controller.ToggleCardInListMessage
import it.unibo.controller.view.ViewController
import it.unibo.launcher.Launcher.view.runOnUIThread

final class InternalViewSubscriber(controller: ViewController)
    extends BaseSubscriber[InternalViewMessage]:

  override val logger: Logger = Logger("View -> IntervalView")

  override protected def onMessageReceived(msg: InternalViewMessage): Unit = msg match
    case InitializeDiscardProcedureMessage() =>
      runOnUIThread:
        controller.initDiscardProcedure()
    case ToggleCardInListMessage(cardId) =>
      runOnUIThread:
        controller.toggleCardInDiscardList(cardId)
    case ConfirmDiscardMessage() =>
      runOnUIThread:
        controller.confirmDiscard()
    case CancelDiscardMessage() =>
      runOnUIThread:
        controller.cancelDiscard()
    case CandidateCardToPlayMessage(cardId) =>
      runOnUIThread:
        controller.candidateCardToPlay(cardId)
