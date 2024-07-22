package it.unibo.model.cards

import it.unibo.model.cards.types.{FireCardType, FirebreakCardType, HasEffect, WaterCardType, WindCardType}

case class Card(id: Int = 0, cardType: CardType)

object Card:
  val allCards: Set[HasEffect] = WaterCardType.waterCards ++ FirebreakCardType.firebreakCards ++ FireCardType.fireCards ++
    WindCardType.windCards