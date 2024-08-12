package it.unibo.model.cards.types

import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.resolvers.{EffectResolver, MetaResolver, MultiStepResolver}
import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{LargeEffect, MediumEffect, VeryLargeEffect, VerySmallEffect}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import it.unibo.model.prolog.Rule

enum FireCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case Explosion
      extends FireCard(
        id = 0,
        effect = MultiStepResolver(
          VeryLargeEffect(Map("a" -> Fire, "b" -> Firebreak)),
          Rule(Struct.of("fire", Var.of("R"))),
          FireCard.directions
        )
      )
  case Flare
      extends FireCard(
        id = 1,
        effect = MultiStepResolver(
          MediumEffect(FireCard.defaultTokens),
          Rule(Struct.of("fire", Var.of("R"))),
          FireCard.directions
        )
      )
  case BurningSnag
      extends FireCard(
        id = 2,
        effect = MultiStepResolver(
          LargeEffect(FireCard.defaultTokens),
          Rule(Struct.of("fire", Var.of("R"))),
          FireCard.directions
        )
      )
//  case Ember
//      extends FireCard(
//        id = 3,
//        effect = MultiStepResolver(
//          VerySmallEffect(FireCard.defaultTokens),
//          List(AtLeast("neigh", "cell", "[]", "[]"))
//        )
//      )

object FireCard:
  val directions: List[Direction] = Direction.values.toList
  val defaultTokens: Map[String, Token] = Map("a" -> Fire)
