package it.unibo.model.effect.phase

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.core.GameBoardEffectResolver
import it.unibo.model.effect.core.GameEffectResolver
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.gameboard.GamePhase

final case class PhaseEffect(newPhase: GamePhase) extends IGameEffect

object PhaseEffect extends PhaseManager:
  private def resolvePhase(effect: PhaseEffect) =
    GameBoardEffectResolver { (gbe: GameBoardEffect) =>
      val newGb = updateGamePhase(gbe.gameBoard, effect.newPhase)
      GameBoardEffect(newGb)
    }

  val phaseEffectResolver: GameEffectResolver[PhaseEffect, GameBoardEffectResolver] =
    GameEffectResolver((effect: PhaseEffect) => resolvePhase(effect))
