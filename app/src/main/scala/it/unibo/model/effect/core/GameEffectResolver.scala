package it.unibo.model.effect.core

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect.CardComputation

case class GameEffectResolver[InputEffect <: IGameEffect, OutputEffect <: IGameEffect](
    override val resolver: InputEffect => OutputEffect
) extends IResolveGameEffect[InputEffect, OutputEffect]

case class GameLogicEffectResolver(override val resolver: GameBoardEffect => CardComputation | GameBoardEffect)
    extends IResolveGameEffect[GameBoardEffect, CardComputation | GameBoardEffect]

case class GameBoardEffectResolver(override val resolver: GameBoardEffect => GameBoardEffect)
    extends IResolveGameEffect[GameBoardEffect, GameBoardEffect]
