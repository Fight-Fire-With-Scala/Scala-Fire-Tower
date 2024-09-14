package it.unibo.model.gameboard.player

import it.unibo.model.card.Card
import it.unibo.model.gameboard.grid.TowerPosition

final case class Person(
    override val name: String,
    moves: List[Move],
    hand: List[Card],
    towerPositions: Set[TowerPosition],
    extraCard: Option[Card] = None
) extends Player:
  override def updatePlayer(
      moves: List[Move],
      hand: List[Card],
      extraCard: Option[Card]
  ): Player = copy(moves = moves, hand = hand, extraCard = extraCard)

object Person:
  def apply(name: String, moves: List[Move], hand: List[Card]) =
    new Person(name, List.empty, List.empty, Set.empty)
