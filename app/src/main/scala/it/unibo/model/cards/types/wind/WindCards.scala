package it.unibo.model.cards.types.wind

import it.unibo.model.cards.f
import it.unibo.model.cards.pattern
import it.unibo.model.cards.types.HasMultipleEffects

import WindChoice._
import it.unibo.model.cards.resolver.{PatternResolver, Resolver, WindResolver}

trait WindCards extends HasMultipleEffects[WindChoice, Resolver]:
  val choices: Set[WindChoice] = WindChoice.windChoices
  val windDirection: WindDirection
  val effect: WindChoice => Resolver = generateEffect(windDirection)

  private def generateEffect(direction: WindDirection): WindChoice => Resolver = {
    case UpdateWind => WindResolver(direction)
    case RandomUpdateWind => WindResolver(direction).rollForWindDirection()
    case PlaceFire => PatternResolver(pattern { f }.toMatrix(1, 1))
  }

case object WindCards:
  val windCards: Set[WindCards] = Set(North, South, East, West)

  case object North extends WindCards:
    val effectCode: Int = 4
    val windDirection: WindDirection = WindDirection.North
    
  case object South extends WindCards:
    val effectCode: Int = 5
    val windDirection: WindDirection = WindDirection.South
    
  case object East extends WindCards:
    val effectCode: Int = 6
    val windDirection: WindDirection = WindDirection.East

  case object West extends WindCards:
    val effectCode: Int = 7
    val windDirection: WindDirection = WindDirection.West