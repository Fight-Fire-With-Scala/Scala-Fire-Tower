package it.unibo.controller.model

import it.unibo.model.cards.choices.StepChoice.PatternComputation
import it.unibo.model.cards.choices.WindChoice.PlaceFire
import it.unibo.model.cards.effects.PatternChoiceEffect
import it.unibo.model.cards.types.{FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.player.Player

trait CardController:
  def drawCards(gb: GameBoard, nCards: Int, player: Option[Player] = None): GameBoard =
    val currentPlayer = player.getOrElse(gb.currentPlayer)
    val deck = gb.deck
    val (finalDeck, finalPlayer) = (1 to nCards).foldLeft((deck, currentPlayer)) {
      case ((currentDeck, currentPlayer), _) =>
        val (card, newDeck) = currentDeck.drawCard()
        val newPlayer = currentPlayer.drawCardFromDeck(card)
        (newDeck, newPlayer)
    }
    gb.copy(deck = finalDeck, currentPlayer = finalPlayer)

  def discardCards(gb: GameBoard, cards: List[Int]): GameBoard =
    val player = gb.currentPlayer
    gb.copy(currentPlayer = player.discardCards(cards))

  def setCurrentCardId(gb: GameBoard, cardId: Int): GameBoard =
    val newBoard = gb.board.copy(currentCardId = Some(cardId))
    gb.copy(board = newBoard)

  def resolvePatternComputation(gb: GameBoard, cardId: Int): GameBoard =
    val card = gb.currentPlayer.hand.find(_.id == cardId)
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
    val currentPlayer = gb.currentPlayer
    gb.copy(
      currentPlayer = board.currentCardId match
        case Some(cardId) => currentPlayer.playCard(cardId)._1
        case None         => currentPlayer
      ,
      board = board.copy(currentCardId = None, availablePatterns = Set.empty)
    )
