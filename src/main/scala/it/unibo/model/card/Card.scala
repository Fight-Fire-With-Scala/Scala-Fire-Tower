package it.unibo.model.card

import it.unibo.model.effect.core.ICardEffect

case class Card(id: Int, title: String, description: String, effect: ICardEffect)
