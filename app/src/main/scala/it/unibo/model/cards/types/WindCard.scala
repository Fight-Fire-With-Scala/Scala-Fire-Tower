package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.WindEffect
import it.unibo.model.cards.resolvers.{
  InstantWindResolver,
  MetaResolver,
  MultiStepResolver,
  EffectResolver,
  WindResolver
}
import it.unibo.model.gameboard.Dice
import it.unibo.model.cards.choices.WindChoice.*

enum WindCard(
    override val effectCode: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case North extends WindCard(effectCode = 4, effect = WindCard.getEffect(WindEffect.North))
  case South extends WindCard(effectCode = 5, effect = WindCard.getEffect(WindEffect.South))
  case East extends WindCard(effectCode = 6, effect = WindCard.getEffect(WindEffect.East))
  case West extends WindCard(effectCode = 7, effect = WindCard.getEffect(WindEffect.West))

object WindCard:
  private val dice: Dice[WindEffect] = Dice(WindEffect.values.toSeq, 42L)

  def getEffect(direction: WindEffect): WindResolver = WindResolver {
    case UpdateWind       => InstantWindResolver(direction)
    case RandomUpdateWind => InstantWindResolver(dice.roll())
    case PlaceFire        => MultiStepResolver(pattern(f).mapTo(1, 1))
  }
