package it.unibo.model.cards.types

import it.unibo.model.cards.choices.FirebreakChoice
import it.unibo.model.cards.choices.FirebreakChoice.{Deforest, Reforest}
import it.unibo.model.cards.resolvers.{ChoiceResolver, MultiStepResolver, Resolver}
import it.unibo.model.cards.types.{b, e, pattern, r}

sealed trait FirebreakCardType extends HasEffectType

// noinspection DuplicatedCode
case object FirebreakCardType:
  val firebreakCards: Set[FirebreakCardType] = Set(DeReforest, ScratchLine, DozerLine)

  case object DeReforest extends FirebreakCardType:
    val effectCode: Int = 10
    val effect: Resolver = ChoiceResolver[FirebreakChoice](
      {
        case Deforest => MultiStepResolver(pattern(b).mapTo(1, 1))
        case Reforest => MultiStepResolver(pattern(r).mapTo(1, 1))
      }
    )

  case object ScratchLine extends FirebreakCardType:
    val effectCode: Int = 9
    val effect: Resolver = MultiStepResolver(pattern { b; e; b }.mapTo(1, 3))

  case object DozerLine extends FirebreakCardType:
    val effectCode: Int = 8
    val effect: Resolver = MultiStepResolver(pattern { b; b }.mapTo(1, 2))
