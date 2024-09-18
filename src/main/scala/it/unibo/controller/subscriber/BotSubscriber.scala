package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.BotMessage
import it.unibo.controller.UpdateGamePhaseMessage
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType.EndGameUpdate
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase.EndGamePhase

/** This class is subscribed to the Bot updates and changes the Model accordingly */
final class BotSubscriber(val controller: ModelController)
    extends BaseSubscriber[BotMessage]
    with UpdateGamePhaseHandler:

  override val logger: Logger = Logger("Bot -> BotSubscriber")

  override def onMessageReceived(msg: BotMessage): Unit = msg match
    case UpdateGamePhaseMessage(ef: PhaseEffect) =>
      controller.model.getGameBoard.isGameEnded match
        case Some(_) => controller.applyEffect(PhaseEffect(EndGamePhase), EndGameUpdate)
        case None    => handleUpdateGamePhase(ef)
