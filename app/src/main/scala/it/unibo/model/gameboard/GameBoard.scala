package it.unibo.model.gameboard

import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.{GameChoice, StepChoice, WindChoice}
import it.unibo.model.cards.choices.StepChoice.PatternComputation
import it.unibo.model.cards.effects.GameEffect
import it.unibo.model.cards.resolvers.{
  ChoiceResultResolver,
  EffectResolver,
  InstantResolver,
  InstantWindResolver,
  MetaResolver,
  MultiStepResolver,
  PatternApplicationResolver,
  PatternComputationResolver,
  StepResolver,
  WindResolver
}
import it.unibo.model.cards.types.CanBePlayedAsExtra
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.player.Player

enum GamePhase:
  case WindPhase, RedrawCardsPhase, PlayStandardCardPhase, WaitingPhase, PlaySpecialCardPhase, EndTurnPhase

case class GameBoard(
    board: Board,
    deck: Deck,
    private val player1: Player,
    private val player2: Player,
    currentPlayer: Player = null,
    turnNumber: Int = 0,
    gamePhase: GamePhase = WindPhase
):
  def changeTurn(): GameBoard =
    copy(currentPlayer = if currentPlayer == player1 then player2 else player1, gamePhase = WindPhase, turnNumber = turnNumber + 1)

  def resolveCardPlayed(card: Card, choice: GameChoice): GameBoard =
    val resolver = card.cardType.effectType.effect
    val effect = getGameEffect(resolver, choice)
    val newDeck = card.cardType.effectType match
      case _: CanBePlayedAsExtra => deck
      case _                     => deck.copy(playedCards = card :: deck.playedCards)
    copy(board = board.applyEffect(effect), deck = newDeck)

  private def getGameEffect(
      resolver: MetaResolver[? <: GameChoice, ? <: EffectResolver],
      choice: GameChoice
  ): Option[GameEffect] = resolver match
    case r: WindResolver      => choice match
        case c: WindChoice => handleWind(r.resolve(c))
        case _             => None
    case r: MultiStepResolver => choice match
        case c: StepChoice => handleStep(r.resolve(c))
        case _             => None

  private def handleWind(resolver: ChoiceResultResolver) = resolver match
    case mr: MultiStepResolver   => handleStep(mr.resolve(StepChoice.PatternComputation))
    case ir: InstantWindResolver => Some(ir.resolve())

  private def handleFirebreak(resolver: ChoiceResultResolver) = resolver match
    case mr: MultiStepResolver => handleStep(mr.resolve(StepChoice.PatternComputation))
    case _: InstantResolver[_] => None

  private def handleStep(resolver: StepResolver): Option[GameEffect] = resolver match
    case sr: PatternComputationResolver => Some(sr.getAvailableMoves(board))
    case sr: PatternApplicationResolver => Some(sr.applyMove(board))

object GameBoard:
  def apply(): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    val gb = GameBoard(b, Deck("cards.yaml"), Player(""), Player(""), Player(""))
    gb

  def apply(player1: Player, player2: Player): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    val gb = GameBoard(b, Deck("cards.yaml"), player1, player2, player1)
    gb
