package it.unibo.model.cards.choices

import it.unibo.model.cards.effects.PatternChoiceEffect

sealed trait StepChoice extends GameChoice

case object PatternComputation extends StepChoice
case class PatternApplication(p: PatternChoiceEffect) extends StepChoice
