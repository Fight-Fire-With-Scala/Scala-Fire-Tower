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

  private def resolveCardEffect(cardId: Int, effect: IStandardCardEffect) =
    GameLogicEffectResolver { (gbe: GameBoardEffect) =>
      val logicEffect = effect match
        case effect: FireEffect      => FireEffect.fireEffectResolver.resolve(effect)
        case effect: FirebreakEffect => FirebreakEffect.fireBreakEffectResolver.resolve(effect)
        case effect: WaterEffect     => WaterEffect.waterEffectResolver.resolve(effect)
        case effect: WindEffect      => WindEffect.windEffectResolver.resolve(effect)
      CardComputation(cardId, logicEffect)
    }

  private def resolveCardEffect(cardId: Int, effect: ISpecialCardEffect) =
    GameLogicEffectResolver { (gbe: GameBoardEffect) =>
      val logicEffect = effect match
        case BucketEffect => BucketEffect.bucketEffect
      CardComputation(cardId, logicEffect)
    }

  private def resolveCardPlay(cardId: Int) = GameEffectResolver { (gbe: GameBoardEffect) =>
    val card = gbe.gameBoard.getCurrentPlayer.hand.find(_.id == cardId)
    card match
      case None    => gbe
      case Some(c) => c.effect match
          case effect: IStandardCardEffect => resolveCardEffect(cardId, effect)
          case effect: ISpecialCardEffect  => resolveCardEffect(cardId, effect)
  }

  val handEffectResolver: GameEffectResolver[IGameEffect, IGameEffect] = GameEffectResolver {
    case DrawCard(nCards)   => resolveDrawCard(nCards)
    case DiscardCard(cards) => resolveDiscardCard(cards)
    case PlayCard(cardId)   => resolveCardPlay(cardId)
  }
