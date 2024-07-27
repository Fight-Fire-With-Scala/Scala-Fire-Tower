package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.StepChoice.{PatternApplication, PatternComputation}
import it.unibo.model.cards.choices.{FirebreakChoice, GameChoice, StepChoice, WindChoice}
import it.unibo.model.cards.effects.{CardEffect, PatternChoiceEffect, WindEffect}

sealed trait MetaResolver[C <: GameChoice, R <: EffectResolver] extends EffectResolver:
  def resolve(choice: C): R

sealed case class WindResolver(private val resolver: WindChoice => ChoiceResultResolver)
    extends MetaResolver[WindChoice, ChoiceResultResolver]:
  override def resolve(choice: WindChoice): ChoiceResultResolver = resolver(choice)

sealed case class FirebreakResolver(private val resolver: FirebreakChoice => ChoiceResultResolver)
    extends MetaResolver[FirebreakChoice, ChoiceResultResolver]:
  override def resolve(choice: FirebreakChoice): ChoiceResultResolver = resolver(choice)

sealed trait ChoiceResultResolver extends EffectResolver

sealed trait InstantResolver[E <: CardEffect](effect: E) extends ChoiceResultResolver:
  def resolve(): E = effect

sealed case class InstantWindResolver(effect: WindEffect)
    extends InstantResolver[WindEffect](effect):
  override def resolve(): WindEffect = effect

sealed case class MultiStepResolver(private val resolver: StepChoice => StepResolver)
    extends MetaResolver[StepChoice, StepResolver] with ChoiceResultResolver:
  override def resolve(choice: StepChoice): StepResolver = resolver(choice)

object MultiStepResolver:
  def apply(pattern: PatternChoiceEffect): MultiStepResolver = new MultiStepResolver({
    case PatternComputation    => PatternComputationResolver(pattern)
    case PatternApplication(p) => PatternApplicationResolver(p)
  })
