package it.unibo.model.effects.hand

import it.unibo.model.cards.Card
import it.unibo.model.gameboard.Deck
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Player

trait HandManager:
  private def drawCardsFromDeck(
      gb: GameBoard,
      nCards: Int,
      drawCardFunc: Deck => (Option[Card], Deck)
  )(player: Player): (GameBoard, Player) =
    val deck = gb.deck
    val (finalDeck, finalPlayer) = (1 to nCards).foldLeft((deck, player)):
      case ((currentDeck, player), _) =>
        val (card, newDeck) = drawCardFunc(currentDeck)
        card match
          case Some(c) =>
            val newPlayer = player.drawCardFromDeck(c)
            (newDeck, newPlayer)
          case None    => (newDeck, player)
    (gb.copy(deck = finalDeck), finalPlayer)

  def drawCards(gb: GameBoard, nCards: Int)(player: Player): (GameBoard, Player) =
    drawCardsFromDeck(gb, nCards, _.drawCard())(player)

  def drawSpecialCards(gb: GameBoard, nCards: Int)(player: Player): (GameBoard, Player) =
    drawCardsFromDeck(gb, nCards, _.drawSpecialCard())(player)

  def discardCards(gb: GameBoard, cards: List[Int]): GameBoard =
    val player = gb.getCurrentPlayer
    gb.updateCurrentPlayer(player.discardCards(cards))
