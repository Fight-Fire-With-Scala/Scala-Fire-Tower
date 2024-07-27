package it.unibo.model.cards.types

import it.unibo.model.cards.types.{b, f, pattern}
import it.unibo.model.cards.resolvers.{MetaResolver, MultiStepResolver, EffectResolver}
import it.unibo.model.cards.choices.GameChoice

// noinspection DuplicatedCode
enum FireCard(
    override val effectCode: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case Explosion
      extends FireCard(
        effectCode = 0,
        effect = MultiStepResolver(pattern { f; f; f; f; b; f; f; f; f }.mapTo(3, 3))
      )
  case Flare
      extends FireCard(effectCode = 1, effect = MultiStepResolver(pattern { f; f; f }.mapTo(1, 3)))
  case BurningSnag
      extends FireCard(
        effectCode = 2,
        effect = MultiStepResolver(pattern { f; f; f; f }.mapTo(2, 2))
      )
  case Ember extends FireCard(effectCode = 3, effect = MultiStepResolver(pattern(f).mapTo(1, 1)))
