package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.{
  GameChoice,
  GameEffect,
  LinearResolver,
  Resolver,
  ResolverWithChoice
}

val allCards = WaterCards.waterCards ++ FirebreakCards.firebreakCards ++ FireCards.fireCards ++
  WindCards.windCards

trait HasEffect:
  def effectCode: Int
  def effect: Resolver

trait HasSingleEffect[E <: GameEffect] extends HasEffect:
  def effect: LinearResolver[E]

trait HasMultipleEffects[C <: GameChoice, E <: GameEffect] extends HasEffect:
  def choices: Set[C]
  def effect: ResolverWithChoice[C, E]
