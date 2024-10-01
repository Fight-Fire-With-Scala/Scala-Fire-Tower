package it.unibo.model.effect.phase

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.core.GameBoardEffectSolver
import it.unibo.model.effect.core.GameEffectSolver
import it.unibo.model.effect.core.GameEffect
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.GamePhase

final case class PhaseEffect(newPhase: GamePhase) extends GameEffect

object PhaseEffect extends PhaseManager:
  private def solvePhase(effect: PhaseEffect) =
    GameBoardEffectSolver { (gbe: GameBoardEffect) =>
      val newGb = updateGamePhase(gbe.gameBoard, effect.newPhase)
      newGb
    }

  val phaseEffectSolver: GameEffectSolver[PhaseEffect, GameBoardEffectSolver] =
    GameEffectSolver((effect: PhaseEffect) => solvePhase(effect))
