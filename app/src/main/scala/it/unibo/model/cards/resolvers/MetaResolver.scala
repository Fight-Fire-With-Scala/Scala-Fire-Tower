package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.StepChoice.{PatternApplication, PatternComputation}
import it.unibo.model.cards.choices.{GameChoice, StepChoice, WindChoice}
import it.unibo.model.cards.effects.{CardEffect, PatternEffect, WindEffect}
import it.unibo.model.gameboard.Direction
import it.unibo.model.prolog.Rule

sealed trait MetaResolver[C <: GameChoice, R <: EffectResolver] extends EffectResolver:
  def resolve(choice: C): R

final case class WindResolver(private val resolver: WindChoice => ChoiceResultResolver)
    extends MetaResolver[WindChoice, ChoiceResultResolver]:
  override def resolve(choice: WindChoice): ChoiceResultResolver = resolver(choice)

sealed trait ChoiceResultResolver extends EffectResolver

sealed trait InstantResolver[E <: CardEffect](effect: E) extends ChoiceResultResolver:
  def resolve(): E = effect

final case class InstantWindResolver(private val effect: WindEffect)
    extends InstantResolver[WindEffect](effect):
  override def resolve(): WindEffect = effect

final case class MultiStepResolver(private val resolver: StepChoice => StepResolver)
    extends MetaResolver[StepChoice, StepResolver] with ChoiceResultResolver:
  override def resolve(choice: StepChoice): StepResolver = resolver(choice)

given Conversion[Rule, List[Rule]] = List(_)

object MultiStepResolver:
  def apply(
      pattern: PatternEffect,
      goals: List[Rule],
      directions: List[Direction] = Direction.values.toList
  ): MultiStepResolver = new MultiStepResolver({
    case PatternComputation    => PatternComputationResolver(pattern, goals, directions)
    case PatternApplication(p) => PatternApplicationResolver(p)
  })
