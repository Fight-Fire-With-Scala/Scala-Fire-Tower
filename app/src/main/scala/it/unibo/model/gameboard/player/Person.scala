package it.unibo.model.gameboard.player

import it.unibo.model.cards.Card
import it.unibo.model.gameboard.grid.{Position, TowerPosition}

final case class Person(
    override val name: String,
    moves: List[Move],
    hand: List[Card],
    towerPosition: TowerPosition = TowerPosition.TOP_RIGHT,
    extraCard: Option[Card] = None
) extends Player:
  override protected def updatePlayer(
      moves: List[Move],
      hand: List[Card],
      extraCard: Option[Card]
  ): Player = copy(moves = moves, hand = hand, extraCard = extraCard)
