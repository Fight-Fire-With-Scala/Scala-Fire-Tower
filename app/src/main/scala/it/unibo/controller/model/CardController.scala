package it.unibo.controller.model

import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.StepChoice.PatternComputation
import it.unibo.model.cards.choices.WindChoice.PlaceFire
import it.unibo.model.cards.effects.PatternChoiceEffect
import it.unibo.model.cards.types.{FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.gameboard.{Deck, GameBoard}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.player.Player

trait CardController:
  private def drawCardsFromDeck(gb: GameBoard, nCards: Int, drawCardFunc: Deck => (Card, Deck))(
      player: Player
  ): (GameBoard, Player) =
    val deck = gb.deck
    val (finalDeck, finalPlayer) = (1 to nCards).foldLeft((deck, player)) {
      case ((currentDeck, player), _) =>
        val (card, newDeck) = drawCardFunc(currentDeck)
        val newPlayer = player.drawCardFromDeck(card)
        (newDeck, newPlayer)
    }
    (gb.copy(deck = finalDeck), finalPlayer)

  def drawCards(gb: GameBoard, nCards: Int)(player: Player): (GameBoard, Player) =
    drawCardsFromDeck(gb, nCards, _.drawCard())(player)

  def drawSpecialCards(gb: GameBoard, nCards: Int)(player: Player): (GameBoard, Player) =
    drawCardsFromDeck(gb, nCards, _.drawSpecialCard())(player)

  def discardCards(gb: GameBoard, cards: List[Int]): GameBoard =
    val player = gb.getCurrentPlayer()
    gb.updateCurrentPlayer(player.discardCards(cards))

  def setCurrentCardId(gb: GameBoard, cardId: Int): GameBoard =
    val newBoard = gb.board.copy(currentCardId = Some(cardId))
    gb.copy(board = newBoard)

  def resolvePatternComputation(gb: GameBoard, cardId: Int): GameBoard =
    val card = gb.getCurrentPlayer().hand.find(_.id == cardId)
    card match
      case Some(c) => c.cardType.effectType match
          case _: FireCard | _: FirebreakCard | _: WaterCard => gb
              .resolveCardPlayed(c, PatternComputation)
          case card: WindCard                                => gb.resolveCardPlayed(c, PlaceFire)
          case _                                             => gb
      case None    => gb

  def resolvePatternChoice(gb: GameBoard, pattern: Map[Position, Token]): GameBoard =
    val effect = PatternChoiceEffect(pattern)
    val board = gb.board.applyEffect(Some(effect))
    val currentPlayer = gb.getCurrentPlayer()
    val newGb = board.currentCardId match
      case Some(cardId) => gb.updateCurrentPlayer(currentPlayer.playCard(cardId)._1)
      case None         => gb
    newGb.copy(board = board.copy(currentCardId = None, availablePatterns = Set.empty))
