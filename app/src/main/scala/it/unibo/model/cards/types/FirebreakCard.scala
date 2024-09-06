package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{MediumAltEffect, SmallEffect, VerySmallEffect}
import it.unibo.model.cards.resolvers.{EffectResolver, MetaResolver, MultiStepResolver}
import it.unibo.model.gameboard.grid.ConcreteToken.{Empty, Firebreak}
import it.unibo.model.prolog.Rule
import it.unibo.model.cards.resolvers.given_Conversion_Rule_List

enum FirebreakCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect with CanBeDiscarded:
  case DeReforest
      extends FirebreakCard(
        id = 10,
        effect = MultiStepResolver(
          VerySmallEffect(Map("a" -> Firebreak)),
          List(Rule("deforest"), Rule("reforest"))
        )
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
