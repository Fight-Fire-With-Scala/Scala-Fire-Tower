package it.unibo.model.effect.core

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect

case class GameEffectResolver[InputEffect <: IGameEffect, OutputEffect <: IGameEffect](
    override val resolver: InputEffect => OutputEffect
) extends IResolveGameEffect[InputEffect, OutputEffect]

case class GameLogicEffectResolver(
    override val resolver: GameBoardEffect => PatternEffect | GameBoardEffect
) extends IResolveGameEffect[GameBoardEffect, PatternEffect | GameBoardEffect]

case class GameBoardEffectResolver(override val resolver: GameBoardEffect => GameBoardEffect)
    extends IResolveGameEffect[GameBoardEffect, GameBoardEffect]

given Conversion[GameBoardEffect, GameBoardEffectResolver] =
  gbe => GameBoardEffectResolver((gbe: GameBoardEffect) => gbe)
