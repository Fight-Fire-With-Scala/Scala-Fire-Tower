package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.StepChoice
import it.unibo.model.cards.choices.{PatternApplication, PatternComputation}
import it.unibo.model.cards.effects.PatternChoiceEffect

case class MultiStepResolver(resolver: StepChoice => StepResolver)
    extends CompositeResolver[StepChoice, StepResolver]:
  override def resolve(choice: StepChoice): StepResolver = resolver(choice)

object MultiStepResolver:
  def apply(pattern: PatternChoiceEffect): MultiStepResolver = new MultiStepResolver({
    case PatternComputation => PatternComputationResolver(pattern)
    case PatternApplication(_) => PatternApplicationResolver()
  })
