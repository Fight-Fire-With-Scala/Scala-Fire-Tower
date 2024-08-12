package it.unibo.model.cards.types

import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{LargeEffect, MediumEffect, VeryLargeEffect, VerySmallEffect}
import it.unibo.model.cards.resolvers.{EffectResolver, MetaResolver, MultiStepResolver}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Water}
import it.unibo.model.prolog.Rule

enum WaterCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case SmokeJumper
      extends WaterCard(
        id = 11,
        effect = MultiStepResolver(
          VeryLargeEffect(Map("a" -> Water, "b" -> Fire)),
          Rule(Struct.of("fire", Var.of("R"))),
          WaterCard.directions
        )
      )
  case AirDrop
      extends WaterCard(
        id = 12,
        effect = MultiStepResolver(
          MediumEffect(WaterCard.defaultTokens),
          Rule(Struct.of("fire", Var.of("R"))),
          WaterCard.directions
        )
      )
  case FireEngine
      extends WaterCard(
        id = 13,
        effect = MultiStepResolver(
          LargeEffect(WaterCard.defaultTokens),
          Rule(Struct.of("fire", Var.of("R"))),
          WaterCard.directions
        )
      )
  case Bucket
      extends WaterCard(
        id = 14,
        effect = MultiStepResolver(
          VerySmallEffect(WaterCard.defaultTokens),
          Rule(Struct.of("fire", Var.of("R"))),
          WaterCard.directions
        )
      )

object WaterCard:
  val directions: List[Direction] = Direction.values.toList
  val defaultTokens: Map[String, Token] = Map("a" -> Water)
