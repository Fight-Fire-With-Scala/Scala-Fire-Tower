package it.unibo.model.cards

import it.unibo.model.cards.effects.{FireCard, FirebreakCard, WaterCard, WindCard}

trait GameEffect
trait FirstEffect extends GameEffect
trait LastEffect extends GameEffect

trait GameChoice
trait FirstChoice extends GameChoice
trait LastChoice extends GameChoice

val allCards = WaterCard.waterCards ++ FirebreakCard.firebreakCards ++ FireCard.fireCards ++
  WindCard.windCards

case class Card(id: Int = 0, cardType: CardType)
