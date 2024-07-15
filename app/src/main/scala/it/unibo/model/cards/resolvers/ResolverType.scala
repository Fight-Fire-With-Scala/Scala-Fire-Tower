package it.unibo.model.cards.resolvers

import it.unibo.model.cards.{GameChoice, GameEffect}
import it.unibo.model.cards.PatternChoice

trait Resolver

trait LinearResolver[E <: GameEffect] extends Resolver:
  def resolve(): E

trait ResolverWithChoice[C <: GameChoice, E <: GameEffect] extends Resolver:
  def resolve(choice: C): E

type SpatialResolver = LinearResolver[PatternChoice]
