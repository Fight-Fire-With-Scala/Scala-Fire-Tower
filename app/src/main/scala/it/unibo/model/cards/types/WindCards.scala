package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.wind.{WindChoice, WindDirection, WindResolver}
import it.unibo.model.cards.resolvers.{Dice, GameEffect, ResolverWithChoice}

trait WindCards extends HasMultipleEffects[WindChoice, GameEffect]:
  val choices: Set[WindChoice] = WindChoice.windChoices
  val windDirection: WindDirection
  val effect: ResolverWithChoice[WindChoice, GameEffect] = generateEffect(windDirection)

  private def generateEffect(direction: WindDirection): ResolverWithChoice[WindChoice, GameEffect] =
    val dice = Dice(WindDirection.windDirections.toSeq, 42L)
    WindResolver(direction, dice)

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
