package it.unibo.model.gameboard

import it.unibo.model.board.Board
import it.unibo.model.cards.resolvers.{
  ChoiceResolver,
  MultiStepResolver,
  PatternApplicationResolver,
  PatternComputationResolver,
  StepResolver
}
import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.{GameChoice, PatternApplication, PatternComputation, StepChoice}
import it.unibo.model.cards.effects.GameEffect

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

  private def getGameEffect(card: Card, choice: Option[GameChoice]): Option[GameEffect] =
    choice match
      case Some(c: StepChoice) => handleStepChoice(card, c)
      case Some(c: GameChoice) => handleGameChoice(card, c)
      case None                => None

  private def handleStepChoice(card: Card, choice: StepChoice): Option[GameEffect] =
    card.cardType.effectType.effect match
      case _: ChoiceResolver[GameChoice] => None
      case mr: MultiStepResolver         => matchStepResolver(mr.resolve(choice), choice)
      case _                             => None

  private def handleGameChoice(card: Card, choice: GameChoice): Option[GameEffect] =
    card.cardType.effectType.effect match
      case cr: ChoiceResolver[GameChoice] => cr.resolve(choice) match
          case sr: StepResolver => matchStepResolver(sr, choice)
          case _                => None
      case _: MultiStepResolver           => None
      case _                              => None

  private def matchStepResolver(resolver: StepResolver, choice: GameChoice): Option[GameEffect] =
    resolver match
      case sr: PatternComputationResolver => Some(sr.getAvailableMoves(board))
      case sr: PatternApplicationResolver => choice match
          case PatternComputation          => None
          case PatternApplication(pattern) => Some(sr.applyMove(board, pattern))
          case _                           => None
      case _                              => None
//  private def getChoice(
//      choice: Option[GameChoice],
//      r: ChoiceResolver[GameChoice]
//  ): Option[StepChoice => StepResolver] = choice match
//    case Some(c) => Some(r.resolve(c).resolver)
//    case None    => None
//
//  private def getLambda(
//      choice: Option[GameChoice],
//      cr: ChoiceResolver[GameChoice]
//  ): Option[StepResolver] = getChoice(choice, cr) match
//    case Some(c) => Some(c(PatternComputation))
//    case None    => None
//
//  private def getStepResolver(
//      choice: Option[GameChoice],
//      sr: Option[StepResolver]
//  ): Option[CardEffect] = sr match
//    case PatternComputationResolver => sr.getAvailableMoves(board)
//    case PatternApplicationResolver => sr.applyMove(board, c)
//
////  Some(sr.applyMove(board, choice.))
//  private def getStepChoice(choice: Option[GameChoice], mr: Option[MultiStepResolver]) = c match
//    case PatternComputation    => getStepResolver(r, c).getAvailableMoves(board)
//    case PatternApplication(p) => getStepResolver(r, c).applyMove(board, p)
