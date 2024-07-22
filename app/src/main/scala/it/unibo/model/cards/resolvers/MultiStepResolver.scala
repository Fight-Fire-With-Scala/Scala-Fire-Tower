package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.StepChoice
import it.unibo.model.cards.choices.StepChoice.{stepChoices, PatternApplication, PatternComputation}
import it.unibo.model.cards.effects.PatternChoiceEffect

case class MultiStepResolver(steps: Set[StepChoice], resolver: StepChoice => StepResolver)
    extends CompositeResolver[StepChoice, StepResolver]:
  override def resolve(choice: StepChoice): StepResolver = resolver(choice)

object MultiStepResolver:
  def apply(pattern: PatternChoiceEffect): MultiStepResolver = new MultiStepResolver(
    stepChoices,
    {
      case PatternComputation => PatternComputationResolver(pattern)
      case PatternApplication => PatternApplicationResolver()
    }
  )
