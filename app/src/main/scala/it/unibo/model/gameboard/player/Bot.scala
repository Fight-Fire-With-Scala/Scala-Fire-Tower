package it.unibo.model.gameboard.player

import it.unibo.model.cards.Card

final case class Bot(moves: List[Move], hand: List[Card], extraCard: Option[Card] = None)
    extends Player:
  override val name: String = "BOT"
  override protected def updatePlayer(
      moves: List[Move],
      hand: List[Card],
      extraCard: Option[Card]
  ): Player = copy(moves = moves, hand = hand, extraCard = extraCard)
