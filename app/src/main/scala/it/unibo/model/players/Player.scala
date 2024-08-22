package it.unibo.model.players

import it.unibo.model.cards.Card

case class Move(name: String)

sealed trait Player:
  val name: String
  def moves: List[Move]
  def hand: List[Card]

  def drawCardFromDeck(card: Card): Player = this match
    case person: Person if person.hand.size < 5 => person.copy(hand = person.hand :+ card)
    case bot: Bot if bot.hand.size < 5 => bot.copy(hand = bot.hand :+ card)
    case _ => this

  def playCard(cardId: Int): (Player, Option[Card]) = this.hand.find(_.id == cardId) match
    case Some(card) =>
      val updatedHand = this.hand.filterNot(_.id == cardId)
      this match
        case person: Person => (person.copy(hand = updatedHand), Option(card))
        case bot: Bot => (bot.copy(hand = updatedHand), Option(card))
    case None => (this, Option.empty)

  def logMove(move: Move): Player = this match
    case person: Person => person.copy(moves = person.moves :+ move)
    case bot: Bot => bot.copy(moves = bot.moves :+ move)


object Player:
  def apply(name: String): Player = Person(name, List.empty, List.empty)
  def bot: Player = Bot(List.empty, List.empty)

case class Person(override val name: String, moves: List[Move], hand: List[Card]) extends Player

case class Bot(moves: List[Move], hand: List[Card]) extends Player:
  override val name: String = "BOT"

  