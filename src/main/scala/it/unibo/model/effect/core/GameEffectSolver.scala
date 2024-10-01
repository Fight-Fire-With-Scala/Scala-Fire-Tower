package it.unibo.model.effect.core

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.gameboard.GameBoard

final case class GameEffectSolver[InputEffect <: GameEffect, OutputEffect <: GameEffect](
    override val solver: InputEffect => OutputEffect
) extends SolveGameEffect[InputEffect, OutputEffect]

final case class PatternEffectSolver(
    override val solver: GameBoardEffect => PatternEffect | GameBoardEffect
) extends SolveGameEffect[GameBoardEffect, PatternEffect | GameBoardEffect]

final case class LogicEffectSolver[CEffect <: CardEffect](
    override val solver: CEffect => LogicEffect
) extends SolveGameEffect[CEffect, LogicEffect]

final case class GameBoardEffectSolver(override val solver: GameBoardEffect => GameBoardEffect)
    extends SolveGameEffect[GameBoardEffect, GameBoardEffect]

given Conversion[GameBoardEffect, GameBoardEffectSolver] =
  gbe => GameBoardEffectSolver((gbe: GameBoardEffect) => gbe)

given Conversion[GameBoard, GameBoardEffect] = GameBoardEffect(_)
