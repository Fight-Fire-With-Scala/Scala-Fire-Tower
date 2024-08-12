package it.unibo.model.cards.types

import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.choices.{FirebreakChoice, GameChoice}
import it.unibo.model.cards.choices.FirebreakChoice.{Deforest, Reforest}
import it.unibo.model.cards.effects.{MediumAltEffect, SmallEffect, VerySmallEffect}
import it.unibo.model.cards.resolvers.{
  EffectResolver,
  FirebreakResolver,
  MetaResolver,
  MultiStepResolver
}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.ConcreteToken.{Empty, Firebreak}
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.Rule

enum FirebreakCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case DeReforest
      extends FirebreakCard(
        id = 10,
        effect = FirebreakResolver {
          case Deforest => MultiStepResolver(
              VerySmallEffect(FirebreakCard.defaultTokens),
              Rule(Struct.of("fire", Var.of("R"))),
              FirebreakCard.directions
            )
          case Reforest => MultiStepResolver(
              VerySmallEffect(FirebreakCard.defaultTokens),
              Rule(Struct.of("fire", Var.of("R"))),
              FirebreakCard.directions
            )
        }
      )
  case ScratchLine
      extends FirebreakCard(
        id = 9,
        effect = MultiStepResolver(
          MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)),
          Rule(Struct.of("fire", Var.of("R"))),
          FirebreakCard.directions
        )
      )
  case DozerLine
      extends FirebreakCard(
        id = 8,
        effect = MultiStepResolver(
          SmallEffect(FirebreakCard.defaultTokens),
          Rule(Struct.of("fire", Var.of("R"))),
          FirebreakCard.directions
        )
      )

object FirebreakCard:
  val directions: List[Direction] = Direction.values.toList
  val defaultTokens: Map[String, Token] = Map("a" -> Firebreak)
