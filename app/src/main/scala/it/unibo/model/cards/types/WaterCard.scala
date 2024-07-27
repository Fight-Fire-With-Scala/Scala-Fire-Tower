package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.types.{f, pattern, w}
import it.unibo.model.cards.resolvers.{MetaResolver, MultiStepResolver, EffectResolver}

// noinspection DuplicatedCode
enum WaterCard(
    override val effectCode: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case SmokeJumper
      extends WaterCard(
        effectCode = 11,
        effect = MultiStepResolver(pattern { w; w; w; w; f; w; w; w; w }.mapTo(3, 3))
      )
  case AirDrop
      extends WaterCard(
        effectCode = 12,
        effect = MultiStepResolver(pattern { w; w; w }.mapTo(1, 3))
      )
  case FireEngine
      extends WaterCard(
        effectCode = 13,
        effect = MultiStepResolver(pattern { w; w; w; w }.mapTo(2, 2))
      )
  case Bucket
      extends WaterCard(
        effectCode = 14,
        effect = MultiStepResolver(pattern { w; w; w }.mapTo(1, 3))
      )
