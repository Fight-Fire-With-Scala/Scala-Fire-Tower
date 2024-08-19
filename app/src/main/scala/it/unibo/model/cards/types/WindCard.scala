package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{VerySmallEffect, WindEffect}
import it.unibo.model.cards.resolvers.{
  EffectResolver,
  InstantWindResolver,
  MetaResolver,
  MultiStepResolver,
  WindResolver
}
import it.unibo.model.gameboard.{Dice, Direction}
import it.unibo.model.cards.choices.WindChoice.*
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

enum WindCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect with CanBeDiscarded:
  case North extends WindCard(id = 4, effect = WindCard.getEffect(Direction.North))
  case South extends WindCard(id = 5, effect = WindCard.getEffect(Direction.South))
  case East extends WindCard(id = 6, effect = WindCard.getEffect(Direction.East))
  case West extends WindCard(id = 7, effect = WindCard.getEffect(Direction.West))

object WindCard:
  given Conversion[Direction, WindEffect] = WindEffect(_)

  private val dice: Dice[Direction] = Dice(Direction.values.toSeq, 42L)

  def getEffect(direction: Direction): WindResolver = WindResolver {
    case UpdateWind       => InstantWindResolver(direction)
    case RandomUpdateWind => InstantWindResolver(dice.roll())
    case PlaceFire        =>
      MultiStepResolver(VerySmallEffect(Map("a" -> Fire)), Rule("fire"), List(direction))
  }
