package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.Resolver

trait HasEffectType:
  def effectCode: Int
  def effect: Resolver
