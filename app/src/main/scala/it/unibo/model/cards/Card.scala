package it.unibo.model.cards

import it.unibo.model.cards.types.{FireCard, FirebreakCard, HasEffect, WaterCard, WindCard}

case class Card(id: Int = 0, cardType: CardType)

object Card:
  val allCards: Set[HasEffect] =
    (WaterCard.values ++ FirebreakCard.values ++ FireCard.values ++ WindCard.values).toSet
