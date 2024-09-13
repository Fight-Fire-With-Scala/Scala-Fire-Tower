package it.unibo.model.gameboard

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.core.GameBoardEffectResolver
import it.unibo.model.effect.core.GameLogicEffectResolver
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.logger
import it.unibo.model.effect.core.given_Conversion_GameBoardEffect_GameBoardEffectResolver

trait GameBoardManager:
  protected def gameBoardEffectResolver(gb: GameBoard, effect: IGameEffect): GameBoard =
    logger.info(s"Effect to resolve $effect")

    val gameBoardEffect = effect match
      case ef: PhaseEffect             => PhaseEffect.phaseEffectResolver.resolve(ef)
      case ef: HandEffect              =>
        val handEffect = HandEffect.handEffectResolver.resolve(ef)
        handleHandEffect(gb, handEffect)
      case ef: PatternEffect           => PatternEffect.patternEffectResolver.resolve(ef)
      case ef: GameBoardEffectResolver => ef

    gameBoardEffect.resolve(GameBoardEffect(gb)).gameBoard

  private def handleHandEffect(gb: GameBoard, effect: IGameEffect): GameBoardEffectResolver =
    effect match
      case e: GameLogicEffectResolver =>
        val effect = e.resolve(GameBoardEffect(gb))
        effect match
          case ef: PatternEffect  => PatternEffect.patternEffectResolver.resolve(ef)
          case e: GameBoardEffect => e
      case e: GameBoardEffectResolver => e
