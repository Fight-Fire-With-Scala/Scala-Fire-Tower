package it.unibo.model.gameboard

import it.unibo.model.board.Board
import it.unibo.model.cards.resolvers.{LinearResolver, ResolverWithChoice}
import it.unibo.model.cards.{Card, Deck, GameChoice, GameEffect}

sealed trait Player
case object Player1 extends Player
case object Player2 extends Player

/**
 *
 * @param board
 * @param deck
 * @param currentPlayer
 * @param Player MISSING
 */
case class GameBoard(board: Board, deck: Deck, var currentPlayer: Player = Player1):
  def changeTurn(): GameBoard =
    currentPlayer = currentPlayer match
      case Player1 => Player2
      case Player2 => Player1
    this

  def resolveCardPlayed(card: Card, cardChoice: Option[GameChoice]): GameBoard =
    copy(board = board.applyEffect(getGameEffect(card, cardChoice)))

  private def getGameEffect(card: Card, cardChoice: Option[GameChoice]): Option[GameEffect] =
    card.cardType.resolve match
      case r: LinearResolver[GameEffect]                   => Some(r.resolve())
      case res: ResolverWithChoice[GameChoice, GameEffect] => getGameChoice(res, cardChoice)
      case _                                               => Option.empty

  private def getGameChoice(
      resolver: ResolverWithChoice[GameChoice, GameEffect],
      cardChoice: Option[GameChoice]
  ): Option[GameEffect] = cardChoice match
    case Some(choice) => Some(resolver.resolve(choice))
    case None         => None
