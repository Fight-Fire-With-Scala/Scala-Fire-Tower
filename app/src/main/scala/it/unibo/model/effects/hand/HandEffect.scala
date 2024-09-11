package it.unibo.model.effects.hand

import it.unibo.model.cards.Card
import it.unibo.model.effects.GameBoardEffect
import it.unibo.model.effects.PatternEffect.CardComputation
import it.unibo.model.effects.cards.*
import it.unibo.model.effects.core.*
import it.unibo.model.gameboard.player.Player
import it.unibo.model.gameboard.GameBoard

enum HandEffect extends IGameEffect:
  case PlayCard(cardId: Int)
  case DrawCard(nCards: Int)
  case DiscardCard(cards: List[Int])

object HandEffect extends HandManager:
  private def resolveDrawCard(nCards: Int) = GameBoardEffectResolver { (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    val (newGb, player) = drawCards(gb, nCards)(gb.getCurrentPlayer)
    GameBoardEffect(newGb.updateCurrentPlayer(player))
  }

  private def resolveDiscardCard(cards: List[Int]) =
    GameBoardEffectResolver { (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val newGb = discardCards(gb, cards)
      GameBoardEffect(newGb)
    }

  private def resolveCardEffect(cardId: Int) = GameLogicEffectResolver { (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    val card = gb.getCurrentPlayer.hand.find(_.id == cardId)
    card match
      case Some(c) =>
        c.effect match
          case effect: FireEffect      =>
            val logicEffect = FireEffect.fireEffectResolver.resolve(effect)
            CardComputation(cardId, logicEffect)
          case effect: FirebreakEffect =>
            val logicEffect = FirebreakEffect.fireBreakEffectResolver.resolve(effect)
            CardComputation(cardId, logicEffect)
          case effect: WaterEffect     =>
            val logicEffect = WaterEffect.waterEffectResolver.resolve(effect)
            CardComputation(cardId, logicEffect)
          case effect: WindEffect      => 
            val logicEffect = WindEffect.windEffectResolver.resolve(effect)
            CardComputation(cardId, logicEffect)
          case BucketEffect            =>
            val logicEffect = BucketEffect.bucketEffect
            CardComputation(cardId, logicEffect)
          case _ => GameBoardEffect(gb)
      case None    => GameBoardEffect(gb)
  }

  val handEffectResolver: GameEffectResolver[IGameEffect, IGameEffect] = GameEffectResolver {
    case DrawCard(nCards)   => resolveDrawCard(nCards)
    case DiscardCard(cards) => resolveDiscardCard(cards)
    case PlayCard(cardId)   => resolveCardEffect(cardId)
  }
