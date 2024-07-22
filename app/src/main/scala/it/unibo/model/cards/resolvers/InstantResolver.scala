package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.CardEffect

case class InstantResolver[E <: CardEffect](effect: E) extends Resolver:
  def resolve(): E = effect
