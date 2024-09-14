package it.unibo.model.effect.hand

import it.unibo.model.card.Card
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.card._
import it.unibo.model.effect.card.FireEffect.fireEffectResolver
import it.unibo.model.effect.card.FirebreakEffect.fireBreakEffectResolver
import it.unibo.model.effect.card.WaterEffect.waterEffectResolver
import it.unibo.model.effect.card.WindEffect.windEffectResolver
import it.unibo.model.effect.core._
import it.unibo.model.effect.core.LogicEffectResolver
import it.unibo.model.effect.core.PatternEffectResolver
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.CardComputation
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Player

enum HandEffect extends IGameEffect:
  case PlayCard(cardId: Int)
  case DrawCard(nCards: Int)
  case DiscardCard(cards: List[Int])

object HandEffect extends HandManager:
  private def resolveDrawCard(nCards: Int) = GameBoardEffectResolver: (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    val (newGb, player) = drawCards(gb, nCards)(gb.getCurrentPlayer)
    GameBoardEffect(newGb.updateCurrentPlayer(player))

  private def resolveDiscardCard(cards: List[Int]) =
    GameBoardEffectResolver: (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val cardsPlayed = cards.flatMap(cId => gb.getCurrentPlayer.hand.find(_.id == cId))
      val playedCards = cardsPlayed ++ gb.deck.playedCards
      val newDeck = gb.deck.copy(playedCards = cardsPlayed)
      discardCards(gb, cards).copy(deck = newDeck)

  private def resolveCardEffect(cardId: Int) = PatternEffectResolver: (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    val cardOpt = gb.getCurrentPlayer.hand.find(_.id == cardId)
    cardOpt.map(card => resolveCard(gb, card)).getOrElse(gb)

  private def resolveCard(gb: GameBoard, card: Card): GameBoardEffect | PatternEffect =
    def resolveEffect[CardEffect <: ICardEffect](
        resolver: LogicEffectResolver[CardEffect],
        effect: CardEffect
    ): CardComputation =
      val logicEffect = resolver.resolve(effect)
      CardComputation(card.id, logicEffect)

    card.effect match
      case effect: FireEffect      => resolveEffect(fireEffectResolver, effect)
      case effect: FirebreakEffect => resolveEffect(fireBreakEffectResolver, effect)
      case effect: WaterEffect     => resolveEffect(waterEffectResolver, effect)
      case effect: WindEffect      => resolveEffect(windEffectResolver, effect)
      case BucketEffect            => CardComputation(card.id, BucketEffect.bucketEffect)
      case _                       => gb

  val handEffectResolver: GameEffectResolver[IGameEffect, IGameEffect] = GameEffectResolver:
    case DrawCard(nCards)   => resolveDrawCard(nCards)
    case DiscardCard(cards) => resolveDiscardCard(cards)
    case PlayCard(cardId)   => resolveCardEffect(cardId)
