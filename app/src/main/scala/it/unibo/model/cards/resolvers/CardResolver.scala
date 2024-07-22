package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.GameChoice

trait Resolver

trait StepResolver extends Resolver

trait CompositeResolver[C <: GameChoice, R <: Resolver] extends Resolver:
  def resolve(choice: C): R
