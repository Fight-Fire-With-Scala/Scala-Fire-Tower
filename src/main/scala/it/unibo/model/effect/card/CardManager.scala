package it.unibo.model.effect.card

import it.unibo.model.card.{ Card, ICanBePlayedAsExtra }
import it.unibo.model.effect.MoveEffect.{ BotChoice, CardChosen }
import it.unibo.model.effect.core.*
import it.unibo.model.effect.hand.HandEffect.playCard
import it.unibo.model.effect.hand.HandManager
import it.unibo.model.effect.{ GameBoardEffect, MoveEffect }
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Move
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect

trait CardManager extends HandManager:
  protected def updateDeckAndHand(gb: GameBoard, move: Move): GameBoardEffect =
    move.effect match
      case BotChoice(cardId, patternChosen) =>
        val cardOpt = findCard(gb, cardId)
        cardOpt match
          case Some(card) => handleCardEffect(gb, card)
          case None       => gb
      case CardChosen(card, _) => handleCardEffect(gb, card)
      case _                   => gb

  private def findCard(gb: GameBoard, cardId: Int): Option[Card] =
    gb.getCurrentPlayer.hand.find(_.id == cardId) match
      case Some(card) => Some(card)
      case None =>
        gb.getCurrentPlayer.extraCard match
          case Some(card) if card.id == cardId => Some(card)
          case _ => None

  private def handleCardEffect(gb: GameBoard, card: Card): GameBoard =
    card.effect match
      case _: ICanBePlayedAsExtra => playCard(gb, card)
      case _ =>
        val gbUpdatedHand = playCard(gb, card)
        updateDeck(gbUpdatedHand, card)

  private def updateDeck(gb: GameBoard, card: Card) =
    val newDeck = gb.deck.copy(playedCards = card :: gb.deck.playedCards)
    gb.copy(deck = newDeck)
