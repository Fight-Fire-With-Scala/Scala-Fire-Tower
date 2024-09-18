package it.unibo.model.effect.hand

import it.unibo.model.card.Card
import it.unibo.model.gameboard.player.Player
import it.unibo.model.gameboard.{ Deck, GameBoard }

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
          case None => (newDeck, player)
    (gb.copy(deck = finalDeck), finalPlayer)

  protected def drawCards(gb: GameBoard, nCards: Int)(player: Player): (GameBoard, Player) =
    drawCardsFromDeck(gb, nCards, _.drawCard())(player)

  protected def drawSpecialCards(gb: GameBoard, nCards: Int)(player: Player): (GameBoard, Player) =
    drawCardsFromDeck(gb, nCards, _.drawSpecialCard())(player)

  protected def discardCards(gb: GameBoard, cards: List[Int]): GameBoard =
    val player = gb.getCurrentPlayer
    gb.updateCurrentPlayer(player.discardCards(cards))

  protected def playCard(gb: GameBoard, card: Card): GameBoard =
    val (player, _) = gb.getCurrentPlayer.playCard(card.id)
    gb.updateCurrentPlayer(player)
