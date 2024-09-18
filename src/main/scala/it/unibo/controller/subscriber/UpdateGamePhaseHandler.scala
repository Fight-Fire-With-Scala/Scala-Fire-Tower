package it.unibo.controller.subscriber

import it.unibo.controller.model.ModelController
import it.unibo.model.ModelModule.Model
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Bot
import it.unibo.controller.view.RefreshType.{ EndGameUpdate, PhaseUpdate }

trait UpdateGamePhaseHandler:

  def controller: ModelController

  given Conversion[Model, GameBoard] = _.getGameBoard
  given Model                        = controller.model

  def handleUpdateGamePhase(ef: PhaseEffect): Unit =
    val gb = controller.model.getGameBoard
    controller.applyEffect(ef, PhaseUpdate)
    gb.getCurrentPlayer match
      case b: Bot => b.think
      case _      =>

    controller.model.getGameBoard.isGameEnded match
      case Some(_) => controller.applyEffect(ef, EndGameUpdate)
      case None    =>
