package it.unibo.model.effect.card

import it.unibo.model.effect.core.{IStandardCardEffect, LogicEffectSolver, OffensiveEffect}
import it.unibo.model.effect.core.SingleStepEffect.given_Conversion_ILogicComputation_SingleStepEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

enum WindEffect(override val effectId: Int, val direction: Direction) extends IStandardCardEffect:
  case North extends WindEffect(4, Direction.North)
  case South extends WindEffect(5, Direction.South)
  case East extends WindEffect(6, Direction.East)
  case West extends WindEffect(7, Direction.West)

object WindEffect:
  given Conversion[Direction, WindEffect] =
    case Direction.North => WindEffect.North
    case Direction.South => WindEffect.South
    case Direction.East  => WindEffect.East
    case Direction.West  => WindEffect.West

  private def getPlaceFireEffect(direction: Direction) =
    OffensiveEffect(
      VerySmallEffect(Map("a" -> Fire)),
      Rule("fire"),
      List(direction)
    )

  val windEffectSolver: LogicEffectSolver[WindEffect] = LogicEffectSolver: (e: WindEffect) =>
    getPlaceFireEffect(e.direction)
