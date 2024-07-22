package it.unibo.model.gameboard

import it.unibo.model.board.Board
import it.unibo.model.cards.resolvers.{ChoiceResolver, MultiStepResolver, Resolver, StepResolver}
import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.{CardChoice, GameChoice}
import it.unibo.model.cards.effects.{CardEffect, GameEffect}

sealed trait Player
case object Player1 extends Player
case object Player2 extends Player

/** @param board
  * @param deck
  * @param currentPlayer
  * @param Player
  *   MISSING
  */
case class GameBoard(board: Board, deck: Deck, var currentPlayer: Player = Player1):
  def changeTurn(): GameBoard =
    currentPlayer = currentPlayer match
      case Player1 => Player2
      case Player2 => Player1
    this

  def resolveCardPlayed(card: Card, cardChoice: Option[GameChoice]): GameBoard =
    copy(board = board.applyEffect(getGameEffect(card, cardChoice)))

  private def getGameEffect(card: Card, choice: Option[GameChoice]): Option[CardEffect] =
    card.cardType.effect match
      case r: ChoiceResolver[_] => ???
      case r: MultiStepResolver => ???

//  private def getGameChoice(
//                             resolver: CompositeResolver[CardChoice, Resolver],
//                             cardChoice: Option[CardChoice]
//  ): Option[CardEffect] = cardChoice match
//    case Some(choice) => Some(resolver.resolve(choice))
//    case None         => None
