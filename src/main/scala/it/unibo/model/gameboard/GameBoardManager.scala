package it.unibo.model.gameboard

import it.unibo.model.effect.{ card, GameBoardEffect }
import it.unibo.model.effect.card.WindUpdateEffect
import it.unibo.model.effect.card.WindUpdateEffect.{ windChoiceSolver, RandomUpdateWind, UpdateWind }
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
      case ef: PhaseEffect           => phaseEffectSolver.solve(ef)
      case ef: HandEffect            => handleHandEffect(gb, ef)
      case ef: PatternEffect         => patternEffectSolver.solve(ef)
      case ef: GameBoardEffectSolver => ef
      case ef: WindUpdateEffect      => windChoiceSolver.solve(ef)

    gameBoardEffect.solve(gb).gameBoard

  private def handleHandEffect(gb: GameBoard, effect: IGameEffect): GameBoardEffectSolver =
    val handEffect = handEffectSolver.solve(effect)
    handEffect match
      case e: PatternEffectSolver =>
        val effect = e.solve(gb)
        effect match
          case ef: PatternEffect  => patternEffectSolver.solve(ef)
          case e: GameBoardEffect => e
      case e: GameBoardEffectSolver => e
