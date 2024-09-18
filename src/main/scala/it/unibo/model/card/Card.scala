package it.unibo.model.card

import it.unibo.model.effect.core.ICardEffect

final case class Card(id: Int, title: String, description: String, effect: ICardEffect)

object Card:
  def apply(cardId: Int, effect: ICardEffect) = new Card(cardId, "", "", effect)
