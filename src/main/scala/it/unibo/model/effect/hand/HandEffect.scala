package it.unibo.model.effect.hand

import it.unibo.model.card.Card
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.core.*
import it.unibo.model.effect.core.PatternEffectSolver
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.CardComputation
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Player
import it.unibo.model.effect.core.CardEffect.given_Conversion_CardEffect_LogicEffect

enum HandEffect extends GameEffect:
  case PlayCard(cardId: Int)
  case DrawCard(nCards: Int)
  case DiscardCard(cards: List[Int])

object HandEffect extends HandManager:
  private def solveDrawCard(nCards: Int) = GameBoardEffectSolver: (gbe: GameBoardEffect) =>
    val gb              = gbe.gameBoard
    val (newGb, player) = drawCards(gb, nCards)(gb.getCurrentPlayer)
    GameBoardEffect(newGb.updateCurrentPlayer(player))

  private def solveDiscardCard(cards: List[Int]) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb          = gbe.gameBoard
      val cardsPlayed = cards.flatMap(cId => gb.getCurrentPlayer.hand.find(_.id == cId))
      val playedCards = cardsPlayed ++ gb.deck.playedCards
      val newDeck     = gb.deck.copy(playedCards = playedCards)
      discardCards(gb, cards).copy(deck = newDeck)

  private def solveCardEffect(cardId: Int) = PatternEffectSolver: (gbe: GameBoardEffect) =>
    val gb            = gbe.gameBoard
    val currentPlayer = gb.getCurrentPlayer
    val cardOpt       = currentPlayer.hand.find(_.id == cardId)
    cardOpt match
      case Some(card) => solveCard(card)
      case None =>
        currentPlayer.extraCard match
          case Some(card) => solveCard(card)
          case None       => gb

  private def solveCard(card: Card): PatternEffect = CardComputation(card.id, card.effect)

  val handEffectSolver: GameEffectSolver[GameEffect, GameEffect] = GameEffectSolver:
    case DrawCard(nCards)   => solveDrawCard(nCards)
    case DiscardCard(cards) => solveDiscardCard(cards)
    case PlayCard(cardId)   => solveCardEffect(cardId)
