package it.unibo.model.effect.core

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.Rule

trait IOffensiveEffect

trait IDefensiveEffect

final case class ILogicEffect(computations: List[ILogicComputation]) extends IGameEffect

object ILogicEffect:
  given Conversion[ILogicComputation, List[ILogicComputation]] = List(_)
  given Conversion[ILogicComputation, ILogicEffect]            = ILogicEffect(_)
  given Conversion[List[ILogicComputation], ILogicEffect]      = ILogicEffect(_)

  def apply(): ILogicEffect =
    ILogicEffect(OffensiveEffect(Map.empty, Rule(""), List.empty))

trait ILogicComputation:
  val pattern: Map[Position, Token]
  val goal: (Int, Int) => Rule
  val directions: List[Direction] = Direction.values.toList

final case class OffensiveEffect(
    pattern: Map[Position, Token],
    goal: (Int, Int) => Rule,
    override val directions: List[Direction] = Direction.values.toList
) extends ILogicComputation

object OffensiveEffect:
  def apply(pattern: Map[Position, Token], goal: (Int, Int) => Rule): OffensiveEffect =
    new OffensiveEffect(pattern, goal)

final case class DefensiveEffect(
    pattern: Map[Position, Token],
    goal: (Int, Int) => Rule,
    override val directions: List[Direction] = Direction.values.toList
) extends ILogicComputation

object DefensiveEffect:
  def apply(pattern: Map[Position, Token], goal: (Int, Int) => Rule): DefensiveEffect =
    new DefensiveEffect(pattern, goal)
