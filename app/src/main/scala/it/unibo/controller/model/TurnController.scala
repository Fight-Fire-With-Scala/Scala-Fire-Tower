package it.unibo.controller.model

import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.{GameBoard, GamePhase}
import it.unibo.model.gameboard.GamePhase.{
  ExtraActionPhase,
  PlayCardPhase,
  RedrawCardsPhase,
  WaitingPhase,
  WindPhase
}
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

trait TurnController:
  def updateGamePhase(gb: GameBoard, choice: GamePhase): GameBoard = choice match
    case ExtraActionPhase => handleWindPhase(gb)
    case RedrawCardsPhase => gb.copy(gamePhase = RedrawCardsPhase)
    case PlayCardPhase    => gb.copy(gamePhase = PlayCardPhase)
    case WindPhase        => handleWindPhase(gb)
    case WaitingPhase     => gb.copy(gamePhase = WaitingPhase)

  private def handleWindPhase(gb: GameBoard): GameBoard =
    val board = gb.board
    val direction = board.windDirection

    val availablePatternsEffect = PatternComputationResolver(
      VerySmallEffect(Map("a" -> Fire)),
      Rule(Struct.of("fire", Var.of("R"))),
      List(direction)
    ).getAvailableMoves(board)

    val b = board.applyEffect(Some(availablePatternsEffect))
    gb.copy(gamePhase = WindPhase, board = b)
