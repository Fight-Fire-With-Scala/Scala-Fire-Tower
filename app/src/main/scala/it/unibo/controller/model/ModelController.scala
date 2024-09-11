package it.unibo.controller.model

import it.unibo.controller.{ModelSubject, RefreshType}
import it.unibo.model.ModelModule.Model
import it.unibo.model.effects.MoveEffect
import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.effects.hand.HandManager
import it.unibo.model.effects.phase.PhaseManager
import it.unibo.controller.RefreshMessage
import it.unibo.model.gameboard.player.Bot

final case class ModelController(model: Model, modelObserver: ModelSubject)
    extends PhaseManager with PlayerController with HandManager:
  
  def applyEffect(ef: IGameEffect, refreshType: RefreshType): Unit =
    val newGb = model.getGameBoard.resolveEffect(ef)
    model.setGameBoard(newGb)
    modelObserver.onNext(RefreshMessage(newGb, refreshType))

  def activateBot(): Unit =
    val gb = model.getGameBoard
    val currentPlayer = gb.getCurrentPlayer
    currentPlayer match
      case bot: Bot => bot.think(gb, gb.gamePhase)
      case _        =>
