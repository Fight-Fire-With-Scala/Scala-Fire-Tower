package it.unibo.model.gameboard

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.core.GameBoardEffectSolver
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.effect.core.PatternEffectSolver
import it.unibo.model.effect.core.given_Conversion_GameBoardEffect_GameBoardEffectSolver
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.hand.HandEffect.handEffectSolver
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.patternEffectSolver
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.effect.phase.PhaseEffect.phaseEffectSolver
import it.unibo.model.logger

trait GameBoardManager:
  protected def gameBoardEffectSolver(gb: GameBoard, effect: IGameEffect): GameBoard =
    logger.debug(s"Effect to solve $effect")

    val gameBoardEffect = effect match
      case ef: PhaseEffect             => phaseEffectSolver.solve(ef)
      case ef: HandEffect              =>
        val handEffect = handEffectSolver.solve(ef)
        handleHandEffect(gb, handEffect)
      case ef: PatternEffect           => patternEffectSolver.solve(ef)
      case ef: GameBoardEffectSolver => ef

    gameBoardEffect.solve(gb).gameBoard

  private def handleHandEffect(gb: GameBoard, effect: IGameEffect): GameBoardEffectSolver =
    effect match
      case e: PatternEffectSolver =>
        val effect = e.solve(gb)
        effect match
          case ef: PatternEffect  => patternEffectSolver.solve(ef)
          case e: GameBoardEffect => e
      case e: GameBoardEffectSolver => e
