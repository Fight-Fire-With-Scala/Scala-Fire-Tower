package it.unibo.model.cards.effects

import it.unibo.model.cards.effects.patterns.PatternChoice
import it.unibo.model.cards.{GameChoice, GameEffect}
import it.unibo.model.cards.resolvers.{LinearResolver, Resolver, ResolverWithChoice}

trait HasEffect:
  def effectCode: Int
  def effect: Resolver

trait HasSingleEffect[E <: GameEffect] extends HasEffect:
  def effect: LinearResolver[E]

trait HasMultipleEffects[C <: GameChoice, E <: GameEffect] extends HasEffect:
  def choices: Set[C]
  def effect: ResolverWithChoice[C, E]

type HasSpatialEffect = HasSingleEffect[PatternChoice]