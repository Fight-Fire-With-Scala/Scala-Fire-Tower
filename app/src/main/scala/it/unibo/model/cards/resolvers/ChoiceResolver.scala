package it.unibo.model.cards.resolvers

import it.unibo.model.cards.choices.CardChoice

case class ChoiceResolver[C <: CardChoice](private val resolver: C => Resolver)
    extends CompositeResolver[C, Resolver]:
  override def resolve(choice: C): Resolver = resolver(choice)
