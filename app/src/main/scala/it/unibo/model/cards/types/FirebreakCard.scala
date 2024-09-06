package it.unibo.model.cards.types

import it.unibo.model.cards.choices.{FirebreakChoice, GameChoice}
import it.unibo.model.cards.choices.FirebreakChoice.{Deforest, Reforest}
import it.unibo.model.cards.effects.{MediumAltEffect, SmallEffect, VerySmallEffect}
import it.unibo.model.cards.resolvers.{
  EffectResolver,
  FirebreakResolver,
  MetaResolver,
  MultiStepResolver
}
import it.unibo.model.gameboard.grid.ConcreteToken.{Empty, Firebreak}
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.Rule

enum FirebreakCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect with CanBeDiscarded:
  case DeReforest
      extends FirebreakCard(
        id = 10,
        effect = FirebreakResolver {
          case Deforest =>
            MultiStepResolver(VerySmallEffect(Map("a" -> Firebreak)), Rule("deforest"))
          case Reforest =>
            MultiStepResolver(VerySmallEffect(Map("a" -> Firebreak)), Rule("reforest"))
        }
      )
  case ScratchLine
      extends FirebreakCard(
        id = 9,
        effect = MultiStepResolver(
          MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)),
          Rule("scratch_line")
        )
      )
  case DozerLine
      extends FirebreakCard(
        id = 8,
        effect = MultiStepResolver(SmallEffect(Map("a" -> Firebreak)), Rule("dozer_line"))
      )
