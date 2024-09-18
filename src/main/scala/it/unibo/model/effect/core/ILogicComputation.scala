package it.unibo.model.effect.core

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{ Position, Token }
import it.unibo.model.prolog.Rule

trait ILogicComputation:
  val pattern: Map[Position, Token]
  val goal: (Option[Int], Option[Int]) => Rule
  val directions: List[Direction] = Direction.values.toList

final case class OffensiveEffect(
    pattern: Map[Position, Token],
    goal: (Option[Int], Option[Int]) => Rule,
    override val directions: List[Direction] = Direction.values.toList
) extends ILogicComputation

object OffensiveEffect:
  def apply(
      pattern: Map[Position, Token],
      goal: (Option[Int], Option[Int]) => Rule
  ): OffensiveEffect =
    new OffensiveEffect(pattern, goal)

final case class DefensiveEffect(
    pattern: Map[Position, Token],
    goal: (Option[Int], Option[Int]) => Rule,
    override val directions: List[Direction] = Direction.values.toList
) extends ILogicComputation

object DefensiveEffect:
  def apply(
      pattern: Map[Position, Token],
      goal: (Option[Int], Option[Int]) => Rule
  ): DefensiveEffect =
    new DefensiveEffect(pattern, goal)
