package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.cards.GameChoice

sealed trait PatternChoice extends GameChoice

case object PatternChoice:
  val windChoices: Set[PatternChoice] = Set(Reforest, Deforest)

  case object Reforest extends PatternChoice
  case object Deforest extends PatternChoice
