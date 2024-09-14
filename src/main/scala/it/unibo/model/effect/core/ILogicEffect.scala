package it.unibo.model.effect.core

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.Rule

final case class ILogicEffect(
    pattern: Map[Position, Token],
    goals: List[Int => Rule],
    directions: List[Direction]
) extends IGameEffect

object ILogicEffect:
  given Conversion[Int => Rule, List[Int => Rule]] = List(_)

  def apply(pattern: Map[Position, Token], goals: List[Int => Rule]) =
    new ILogicEffect(pattern, goals, Direction.values.toList)

  def apply() =
    new ILogicEffect(Map.empty, List.empty, List.empty)
