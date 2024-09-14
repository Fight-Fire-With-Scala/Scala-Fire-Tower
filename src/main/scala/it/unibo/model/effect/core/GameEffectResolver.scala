package it.unibo.model.effect.core

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.gameboard.GameBoard

case class GameEffectResolver[InputEffect <: IGameEffect, OutputEffect <: IGameEffect](
    override val resolver: InputEffect => OutputEffect
) extends IResolveGameEffect[InputEffect, OutputEffect]

case class PatternEffectResolver(
    override val resolver: GameBoardEffect => PatternEffect | GameBoardEffect
) extends IResolveGameEffect[GameBoardEffect, PatternEffect | GameBoardEffect]

case class LogicEffectResolver[CardEffect <: ICardEffect](
    override val resolver: CardEffect => ILogicEffect
) extends IResolveGameEffect[CardEffect, ILogicEffect]

case class GameBoardEffectResolver(override val resolver: GameBoardEffect => GameBoardEffect)
    extends IResolveGameEffect[GameBoardEffect, GameBoardEffect]

given Conversion[GameBoardEffect, GameBoardEffectResolver] =
  gbe => GameBoardEffectResolver((gbe: GameBoardEffect) => gbe)

given Conversion[GameBoard, GameBoardEffect] = GameBoardEffect(_)
