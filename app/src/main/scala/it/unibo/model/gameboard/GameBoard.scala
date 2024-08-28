package it.unibo.model.gameboard

import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.{FirebreakChoice, GameChoice, StepChoice, WindChoice}
import it.unibo.model.cards.choices.StepChoice.PatternComputation
import it.unibo.model.cards.effects.GameEffect
import it.unibo.model.cards.resolvers.{
  ChoiceResultResolver,
  EffectResolver,
  FirebreakResolver,
  InstantResolver,
  InstantWindResolver,
  MetaResolver,
  MultiStepResolver,
  PatternApplicationResolver,
  PatternComputationResolver,
  StepResolver,
  WindResolver
}
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.board.Board
import it.unibo.model.logger
import it.unibo.model.players.Player

enum GamePhase:
  case WindPhase, ActionPhase

enum ActionPhaseChoice:
  case RedrawCards, PlayCard

case class GameBoard(
    board: Board,
    deck: Deck,
    private val player1: Player,
    private val player2: Player,
    currentPlayer: Player = null,
    var gamePhase: GamePhase = WindPhase
):
  def changeTurn(): GameBoard =
    copy(currentPlayer = if currentPlayer == player1 then player2 else player1)

  def resolveCardPlayed(card: Card, choice: GameChoice): GameBoard =
    val resolver = card.cardType.effectType.effect
    copy(board = board.applyEffect(getGameEffect(resolver, choice)))

  private def getGameEffect(
      resolver: MetaResolver[? <: GameChoice, ? <: EffectResolver],
      choice: GameChoice
  ): Option[GameEffect] = resolver match
    case r: WindResolver      => choice match
        case c: WindChoice => handleWind(r.resolve(c))
        case _             => None
    case r: FirebreakResolver => choice match
        case c: FirebreakChoice => handleFirebreak(r.resolve(c))
        case _                  => None
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
  def apply(player1: Player, player2: Player): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    logger.info(s"Wind Direction: ${b.windDirection}")
    val gb = GameBoard(b, Deck("cards.yaml"), player1, player2, player1)
    logger.info(s"Player turn: ${gb.currentPlayer}")
    gb
