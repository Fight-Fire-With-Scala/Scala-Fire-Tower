package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ IStandardCardEffect, LogicEffectSolver, OffensiveEffect }
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_ILogicComputation_PatternLogicEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.VerySmallPattern
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

enum WindEffect(override val effectId: Int, val direction: Direction) extends IStandardCardEffect:
  case North extends WindEffect(5, Direction.North)
  case South extends WindEffect(6, Direction.South)
  case East extends WindEffect(7, Direction.East)
  case West extends WindEffect(8, Direction.West)

object WindEffect:
  given Conversion[Direction, WindEffect] =
    case Direction.North => WindEffect.North
    case Direction.South => WindEffect.South
    case Direction.East  => WindEffect.East
    case Direction.West  => WindEffect.West

  private def getPlaceFireEffect(direction: Direction) =
    OffensiveEffect(
      VerySmallPattern(Map("a" -> Fire)),
      Rule("fire"),
      List(direction)
    )

  val windEffectSolver: LogicEffectSolver[WindEffect] = LogicEffectSolver: (e: WindEffect) =>
    getPlaceFireEffect(e.direction)
