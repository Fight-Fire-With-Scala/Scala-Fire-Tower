package it.unibo.model.gameboard.player

import it.unibo.model.cards.Card
import it.unibo.model.gameboard.grid.TowerPosition

final case class Bot(
    moves: List[Move],
    hand: List[Card],
    towerPositions: Set[TowerPosition],
    extraCard: Option[Card] = None
) extends Player:
  override val name: String = "BOT"
  override protected def updatePlayer(
      moves: List[Move],
      hand: List[Card],
      extraCard: Option[Card]
  ): Player = copy(moves = moves, hand = hand, extraCard = extraCard)

  def think(): Unit = println("Bot is thinking...")

object Bot:
  def apply(moves: List[Move], hand: List[Card]) = new Bot(List.empty, List.empty, Set.empty)
