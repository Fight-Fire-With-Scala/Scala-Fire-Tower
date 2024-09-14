package it.unibo.model.effect.core

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.gameboard.GameBoard

case class GameEffectSolver[InputEffect <: IGameEffect, OutputEffect <: IGameEffect](
    override val solver: InputEffect => OutputEffect
) extends ISolveGameEffect[InputEffect, OutputEffect]

case class PatternEffectSolver(
    override val solver: GameBoardEffect => PatternEffect | GameBoardEffect
) extends ISolveGameEffect[GameBoardEffect, PatternEffect | GameBoardEffect]

case class LogicEffectSolver[CardEffect <: ICardEffect](
    override val solver: CardEffect => ILogicEffect
) extends ISolveGameEffect[CardEffect, ILogicEffect]

case class GameBoardEffectSolver(override val solver: GameBoardEffect => GameBoardEffect)
    extends ISolveGameEffect[GameBoardEffect, GameBoardEffect]

given Conversion[GameBoardEffect, GameBoardEffectSolver] =
  gbe => GameBoardEffectSolver((gbe: GameBoardEffect) => gbe)

given Conversion[GameBoard, GameBoardEffect] = GameBoardEffect(_)
