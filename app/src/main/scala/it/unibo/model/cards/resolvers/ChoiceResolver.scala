package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.GameChoice

case class ChoiceResolver[C <: GameChoice](private val resolver: C => Resolver)
    extends CompositeResolver[C, Resolver]:
  override def resolve(choice: C): Resolver = resolver(choice)
