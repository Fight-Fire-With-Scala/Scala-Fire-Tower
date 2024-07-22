package it.unibo.model.cards.choices

sealed trait StepChoice extends GameChoice

case object StepChoice:
  val stepChoices: Set[StepChoice] = Set(PatternComputation, PatternApplication)

  case object PatternComputation extends StepChoice
  case object PatternApplication extends StepChoice
