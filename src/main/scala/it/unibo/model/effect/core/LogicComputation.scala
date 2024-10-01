package it.unibo.model.effect.core

import it.unibo.model.gameboard.Direction
import it.unibo.model.reasoner.Rule
import it.unibo.model.gameboard.Pattern

trait LogicComputation:
  val pattern: Pattern
  val goal: (Option[Int], Option[Int]) => Rule
  val directions: List[Direction] = Direction.values.toList

final case class OffensiveEffect(
    pattern: Pattern,
    goal: (Option[Int], Option[Int]) => Rule,
    override val directions: List[Direction] = Direction.values.toList
) extends LogicComputation

object OffensiveEffect:
  def apply(
      pattern: Pattern,
      goal: (Option[Int], Option[Int]) => Rule
  ): OffensiveEffect =
    new OffensiveEffect(pattern, goal)

final case class DefensiveEffect(
    pattern: Pattern,
    goal: (Option[Int], Option[Int]) => Rule,
    override val directions: List[Direction] = Direction.values.toList
) extends LogicComputation

object DefensiveEffect:
  def apply(
      pattern: Pattern,
      goal: (Option[Int], Option[Int]) => Rule
  ): DefensiveEffect =
    new DefensiveEffect(pattern, goal)
