package it.unibo.model.cards.effects

import it.unibo.model.cards.resolvers.wind.{WindChoice, WindDirection, WindResolver}
import it.unibo.model.cards.resolvers.{Dice, ResolverWithChoice}
import it.unibo.model.cards.GameEffect

trait WindCard extends HasMultipleEffects[WindChoice, GameEffect]:
  val choices: Set[WindChoice] = WindChoice.windChoices
  val windDirection: WindDirection
  val effect: ResolverWithChoice[WindChoice, GameEffect] = generateEffect(windDirection)

  private def generateEffect(direction: WindDirection): ResolverWithChoice[WindChoice, GameEffect] =
    val dice = Dice(WindDirection.windDirections.toSeq, 42L)
    WindResolver(direction, dice)

case object WindCard:
  val windCards: Set[WindCard] = Set(North, South, East, West)

  case object North extends WindCard:
    val effectCode: Int = 4
    val windDirection: WindDirection = WindDirection.North

  case object South extends WindCard:
    val effectCode: Int = 5
    val windDirection: WindDirection = WindDirection.South

  case object East extends WindCard:
    val effectCode: Int = 6
    val windDirection: WindDirection = WindDirection.East

  case object West extends WindCard:
    val effectCode: Int = 7
    val windDirection: WindDirection = WindDirection.West
