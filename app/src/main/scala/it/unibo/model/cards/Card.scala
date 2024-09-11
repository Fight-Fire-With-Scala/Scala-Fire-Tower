package it.unibo.model.cards

import it.unibo.model.effects.core.ICardEffect

case class Card(id: Int, title: String, description: String, effect: ICardEffect)
