package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{LargeEffect, MediumEffect, VeryLargeEffect, VerySmallEffect}
import it.unibo.model.cards.resolvers.{EffectResolver, MetaResolver, MultiStepResolver}
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
        effect =
          MultiStepResolver(VeryLargeEffect(Map("a" -> Water, "b" -> Fire)), Rule("smoke_jumper"))
      ) with CanBeDiscarded
  case AirDrop
      extends WaterCard(
        id = 12,
        effect = MultiStepResolver(MediumEffect(Map("a" -> Water)), Rule("water"))
      ) with CanBeDiscarded
  case FireEngine
      extends WaterCard(
        id = 13,
        effect = MultiStepResolver(LargeEffect(Map("a" -> Water)), Rule("water"))
      ) with CanBeDiscarded
  case Bucket
      extends WaterCard(
        id = 14,
        effect = MultiStepResolver(VerySmallEffect(Map("a" -> Water)), Rule("bucket"))
      )
      with CanBePlayedAsExtra with CannotBeDiscarded
