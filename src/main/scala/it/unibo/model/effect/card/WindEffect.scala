package it.unibo.model.effect.card

import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_Function_List
import it.unibo.model.effect.core.IOffensiveCard
import it.unibo.model.effect.core.IStandardCardEffect
import it.unibo.model.effect.core.LogicEffectSolver
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

enum WindEffect(override val effectId: Int) extends IStandardCardEffect with IOffensiveCard:
  case North extends WindEffect(4)
  case South extends WindEffect(5)
  case East extends WindEffect(6)
  case West extends WindEffect(7)

object WindEffect:
  given Conversion[Direction, WindEffect] =
    case gameboard.Direction.North => WindEffect.North
    case gameboard.Direction.South => WindEffect.South
    case gameboard.Direction.East  => WindEffect.East
    case gameboard.Direction.West  => WindEffect.West

  private def getPlaceFireEffect(direction: Direction) =
    ILogicEffect(VerySmallEffect(Map("a" -> Fire)), Rule("fire"), List(direction))

  val windEffectSolver: LogicEffectSolver[WindEffect] = LogicEffectSolver:
    case WindEffect.North => getPlaceFireEffect(Direction.North)
    case WindEffect.East  => getPlaceFireEffect(Direction.East)
    case WindEffect.West  => getPlaceFireEffect(Direction.West)
    case WindEffect.South => getPlaceFireEffect(Direction.South)
