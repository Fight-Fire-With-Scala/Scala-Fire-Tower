package it.unibo.model.effects.phase

import it.unibo.model.effects.GameBoardEffect
import it.unibo.model.effects.core.{GameBoardEffectResolver, GameEffectResolver, IGameEffect}
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
