package it.unibo.model.cards.choices

sealed trait FirebreakChoice extends CardChoice

case object FirebreakChoice:
  val firebreakChoices: Set[FirebreakChoice] = Set(Reforest, Deforest)

  case object Reforest extends FirebreakChoice
  case object Deforest extends FirebreakChoice
