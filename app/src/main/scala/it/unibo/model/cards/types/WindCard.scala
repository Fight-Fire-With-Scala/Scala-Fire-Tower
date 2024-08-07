package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{VerySmallEffect, WindEffect}
import it.unibo.model.cards.resolvers.{EffectResolver, InstantWindResolver, MetaResolver, MultiStepResolver, WindResolver}
import it.unibo.model.gameboard.Dice
import it.unibo.model.cards.choices.WindChoice.*
import it.unibo.model.gameboard.grid.Fire
import it.unibo.model.prolog.PrologSolver.engine
import it.unibo.model.prolog.Scala2P.given

enum WindCard(
               override val id: Int,
               override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case North extends WindCard(id = 4, effect = WindCard.getEffect(WindEffect.North))
  case South extends WindCard(id = 5, effect = WindCard.getEffect(WindEffect.South))
  case East extends WindCard(id = 6, effect = WindCard.getEffect(WindEffect.East))
  case West extends WindCard(id = 7, effect = WindCard.getEffect(WindEffect.West))

object WindCard:
  private val dice: Dice[WindEffect] = Dice(WindEffect.values.toSeq, 42L)

  def getEffect(direction: WindEffect): WindResolver = WindResolver {
    case UpdateWind       => InstantWindResolver(direction)
    case RandomUpdateWind => InstantWindResolver(dice.roll())
    case PlaceFire        => MultiStepResolver(
      VerySmallEffect(Map("a" -> Fire)),
      engine("""
        | cell(2, 1, X)
        |""".stripMargin)
    )
  }
