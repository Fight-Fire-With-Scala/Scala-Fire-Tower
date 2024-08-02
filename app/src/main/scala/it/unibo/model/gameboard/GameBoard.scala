package it.unibo.model.gameboard

import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.{FirebreakChoice, GameChoice, StepChoice, WindChoice}
import it.unibo.model.cards.choices.StepChoice.PatternComputation
import it.unibo.model.cards.effects.GameEffect
import it.unibo.model.cards.resolvers.{ChoiceResultResolver, EffectResolver, FirebreakResolver, InstantResolver, InstantWindResolver, MetaResolver, MultiStepResolver, PatternApplicationResolver, PatternComputationResolver, StepResolver, WindResolver}
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.Grid

sealed trait Player
case object Player1 extends Player
case object Player2 extends Player

case class GameBoard(board: Board, deck: Deck, var currentPlayer: Player = Player1):
  def changeTurn(): GameBoard =
    currentPlayer = currentPlayer match
      case Player1 => Player2
      case Player2 => Player1
    this

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
  def apply(): GameBoard = GameBoard(Board.withRandomWindAndStandardGrid, Deck("cards.yaml"))