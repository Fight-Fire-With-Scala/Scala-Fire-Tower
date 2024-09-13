package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.BotMessage
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType.PhaseUpdate
import it.unibo.model.ModelModule.Model
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Bot

/** This class is subscribed to the View updates and changes the Model accordingly */
final class BotSubscriber(controller: ModelController) extends BaseSubscriber[BotMessage]:

  given Conversion[Model, GameBoard] = _.getGameBoard

  override val logger: Logger = Logger("Bot -> BotSubscriber")

  override def onMessageReceived(msg: BotMessage): Unit = msg match
    case UpdateGamePhase(ef: PhaseEffect) =>
      val gb = controller.model.getGameBoard
      controller.applyEffect(ef, PhaseUpdate)
      gb.getCurrentPlayer match
        case b: Bot => b.think(controller)
        case _      =>
