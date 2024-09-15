package it.unibo.model.effect.hand

import it.unibo.model.card.Card
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.card._
import it.unibo.model.effect.core._
import it.unibo.model.effect.core.PatternEffectSolver
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.CardComputation
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Player
import it.unibo.model.effect.core.given_Conversion_ICardEffect_ILogicEffect

enum HandEffect extends IGameEffect:
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
    val gb      = gbe.gameBoard
    val cardOpt = gb.getCurrentPlayer.hand.find(_.id == cardId)
    cardOpt.map(card => solveCard(gb, card)).getOrElse(gb)

  private def solveCard(gb: GameBoard, card: Card): GameBoardEffect | PatternEffect =
    card.effect match
      case effect: FireEffect        => CardComputation(card.id, effect)
      case effect: FirebreakEffect   => CardComputation(card.id, effect)
      case effect: WaterEffect       => CardComputation(card.id, effect)
      case effect: WindEffect        => CardComputation(card.id, effect)
      case effect: BucketEffect.type => CardComputation(card.id, effect)
      case _                         => gb

  val handEffectSolver: GameEffectSolver[IGameEffect, IGameEffect] = GameEffectSolver:
    case DrawCard(nCards)   => solveDrawCard(nCards)
    case DiscardCard(cards) => solveDiscardCard(cards)
    case PlayCard(cardId)   => solveCardEffect(cardId)