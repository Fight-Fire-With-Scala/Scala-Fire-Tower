package it.unibo.model.cards.types

import it.unibo.model.cards.choices.WindChoice
import it.unibo.model.cards.choices.WindChoice.windChoices
import it.unibo.model.cards.choices.WindChoice.{PlaceFire, RandomUpdateWind, UpdateWind}
import it.unibo.model.cards.effects.WindEffect
import it.unibo.model.cards.resolvers.{ChoiceResolver, InstantResolver, MultiStepResolver, Resolver}
import it.unibo.model.gameboard.Dice

sealed trait WindCardType extends HasEffectType:
  lazy val windDirection: WindEffect
  val effect: Resolver = generateEffect(windDirection)

  private def generateEffect(direction: WindEffect): Resolver =
    val dice = Dice(WindEffect.windDirections.toSeq, 42L)
    ChoiceResolver[WindChoice](
      {
        case UpdateWind       => InstantResolver[WindEffect](direction)
        case RandomUpdateWind => InstantResolver[WindEffect](dice.roll())
        case PlaceFire        => MultiStepResolver(pattern(f).mapTo(1, 1))
      }
    )

case object WindCardType:
  val windCards: Set[WindCardType] = Set(North, South, East, West)

  case object North extends WindCardType:
    val effectCode: Int = 4
    lazy val windDirection: WindEffect = WindEffect.North

  case object South extends WindCardType:
    val effectCode: Int = 5
    lazy val windDirection: WindEffect = WindEffect.South

  case object East extends WindCardType:
    val effectCode: Int = 6
    lazy val windDirection: WindEffect = WindEffect.East

  case object West extends WindCardType:
    val effectCode: Int = 7
    lazy val windDirection: WindEffect = WindEffect.West
