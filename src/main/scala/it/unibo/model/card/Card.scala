package it.unibo.model.card

import it.unibo.model.effect.core.CardEffect

final case class Card(id: Int, title: String, description: String, effect: CardEffect)
    extends GameCard

object Card:
  def apply(cardId: Int, effect: CardEffect) = new Card(cardId, "", "", effect)
