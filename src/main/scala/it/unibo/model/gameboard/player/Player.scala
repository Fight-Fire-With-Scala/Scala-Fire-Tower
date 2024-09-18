package it.unibo.model.gameboard.player

import it.unibo.model.card.{ Card, ICanBePlayedAsExtra }
import it.unibo.model.effect.MoveEffect
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.grid.TowerPosition

trait Player:
  val name: String
  def moves: List[Move]
  def hand: List[Card]
  def extraCard: Option[Card]
  def towerPositions: Set[TowerPosition]

  def drawCardFromDeck(card: Card): Player = card.effect match
    case _: ICanBePlayedAsExtra if extraCard.isEmpty => updatePlayer(extraCard = Some(card))
    case _ if hand.size < 5                          => updatePlayer(hand = hand :+ card)
    case _                                           => this

  def playCard(cardId: Int): (Player, Option[Card]) = hand.find(_.id == cardId) match
    case Some(card) => (updatePlayer(hand = hand.filterNot(_.id == cardId)), Some(card))
    case None =>
      extraCard match
        case Some(card) if card.id == cardId => (updatePlayer(extraCard = None), Some(card))
        case _ => (this, None)

  def logMove(move: Move): Player = updatePlayer(moves = moves :+ move)

  val lastCardsRedrawn: Option[Move] = moves.collect { m =>
    m.effect match
      case effect: MoveEffect.CardsRedrawn => m
  }.lastOption

  val lastCardChosen: Option[Move] = moves.collect { m =>
    m.effect match
      case effect: MoveEffect.CardChosen => m
  }.lastOption

  val lastBotChoice: Option[Move] = moves.collect { m =>
    m.effect match
      case effect: MoveEffect.BotChoice => m
  }.lastOption

  val lastPatternChosen: Option[Move] = moves.collect { m =>
    m.effect match
      case effect: MoveEffect.PatternChosen => m
  }.lastOption

  val lastPatternPlayed: Option[Move] = moves.collect { m =>
    m.effect match
      case effect: MoveEffect.PatternApplied => m
  }.lastOption

  def discardCards(cardIds: List[Int]): Player =
    val updatedHand = hand.filterNot(card => cardIds.contains(card.id))
    updatePlayer(hand = updatedHand)

  protected def updatePlayer(
      moves: List[Move] = this.moves,
      hand: List[Card] = this.hand,
      extraCard: Option[Card] = this.extraCard
  ): Player

object Player:
  def apply(name: String): Player = Person(name, List.empty, List.empty)
  def apply(name: String, towerPositions: Set[TowerPosition]): Player =
    Person(name, List.empty, List.empty, towerPositions)
  def bot(botBehaviour: BotBehaviour): Player = Bot(List.empty, List.empty, botBehaviour, None)
