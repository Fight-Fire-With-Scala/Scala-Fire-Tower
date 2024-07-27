package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.resolvers.{MetaResolver, EffectResolver}

trait HasEffect:
  def effectCode: Int
  def effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
