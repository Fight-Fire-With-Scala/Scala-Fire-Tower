package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ ILogicComputation, ILogicEffect, IOffensiveCard, IStandardCardEffect, LogicEffectSolver }
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_Function_List
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_ILogicComputation_ILogicEffect
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_ILogicComputation_List
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

enum WindEffect(override val effectId: Int, val direction: Direction)
    extends IStandardCardEffect
    with IOffensiveCard:
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
    ILogicComputation(VerySmallEffect(Map("a" -> Fire)), Rule("fire"), List(direction))

  val windEffectSolver: LogicEffectSolver[WindEffect] = LogicEffectSolver:
    case e: WindEffect => getPlaceFireEffect(e.direction)
