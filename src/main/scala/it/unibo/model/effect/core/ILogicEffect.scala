package it.unibo.model.effect.core

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.Rule

final case class ILogicEffect(computations: List[ILogicComputation]) extends IGameEffect

object ILogicEffect:
  given Conversion[ILogicComputation, List[ILogicComputation]] = List(_)
  given Conversion[ILogicComputation, ILogicEffect]            = ILogicEffect(_)
  given Conversion[List[ILogicComputation], ILogicEffect]      = ILogicEffect(_)

  def apply(): ILogicEffect =
    ILogicEffect(ILogicComputation(Map.empty, Rule(""), List.empty))

final case class ILogicComputation(
    pattern: Map[Position, Token],
    goal: (Int, Int) => Rule,
    directions: List[Direction]
)

object ILogicComputation:
  def apply(pattern: Map[Position, Token], goals: (Int, Int) => Rule): ILogicComputation =
    ILogicComputation(pattern, goals, Direction.values.toList)
