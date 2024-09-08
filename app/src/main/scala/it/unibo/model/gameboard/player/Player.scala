package it.unibo.model.gameboard.player

import it.unibo.model.cards.Card
import it.unibo.model.cards.types.CanBePlayedAsExtra

trait Player:
  val name: String
  def moves: List[Move]
  def hand: List[Card]
  def extraCard: Option[Card]

  def drawCardFromDeck(card: Card): Player = card.cardType.effectType match
    case _: CanBePlayedAsExtra if extraCard.isEmpty => updatePlayer(extraCard = Some(card))
    case _ if hand.size < 5                         => updatePlayer(hand = hand :+ card)
    case _                                          => this

  def playCard(cardId: Int): (Player, Option[Card]) = hand.find(_.id == cardId) match
    case Some(card) => (updatePlayer(hand = hand.filterNot(_.id == cardId)), Some(card))
    case None       => (this, None)

  def logMove(move: Move): Player = updatePlayer(moves = moves :+ move)

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
  def bot: Player = Bot(List.empty, List.empty)
