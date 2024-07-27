package it.unibo.model.cards.types

import it.unibo.model.cards.choices.{FirebreakChoice, GameChoice}
import it.unibo.model.cards.choices.FirebreakChoice.{Deforest, Reforest}
import it.unibo.model.cards.resolvers.{FirebreakResolver, MetaResolver, MultiStepResolver, EffectResolver}
import it.unibo.model.cards.types.{b, e, pattern, r}

// noinspection DuplicatedCode
enum FirebreakCard(
    override val effectCode: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:

  case DeReforest
      extends FirebreakCard(
        effectCode = 10,
        effect = FirebreakResolver {
          case Deforest => MultiStepResolver(pattern(b).mapTo(1, 1))
          case Reforest => MultiStepResolver(pattern(r).mapTo(1, 1))
        }
      )
  case ScratchLine
      extends FirebreakCard(
        effectCode = 9,
        effect = MultiStepResolver(pattern { b; e; b }.mapTo(1, 3))
      )
  case DozerLine
      extends FirebreakCard(
        effectCode = 8,
        effect = MultiStepResolver(pattern { b; b }.mapTo(1, 2))
      )
