package it.unibo.model.cards.resolvers

trait Resolver

trait LinearResolver[E <: GameEffect] extends Resolver:
  def resolve(): E

trait ResolverWithChoice[C <: GameChoice, E <: GameEffect] extends Resolver:
  def resolve(choice: C): E

trait GameEffect

trait GameChoice
