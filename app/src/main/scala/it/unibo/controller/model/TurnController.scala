package it.unibo.controller.model

import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.{GameBoard, GamePhase}
import it.unibo.model.gameboard.GamePhase.{
  DecisionPhase,
  EndTurnPhase,
  PlaySpecialCardPhase,
  PlayStandardCardPhase,
  RedrawCardsPhase,
  WaitingPhase,
  WindPhase
}
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

trait TurnController:
  def updateGamePhase(gb: GameBoard, choice: GamePhase): GameBoard = choice match
    case WindPhase             => handleWindPhase(gb)
    case WaitingPhase          => gb.copy(gamePhase = WaitingPhase)
    case PlayStandardCardPhase => gb.copy(gamePhase = PlayStandardCardPhase)
    case RedrawCardsPhase      => gb.copy(gamePhase = RedrawCardsPhase)
    case DecisionPhase         =>
      val nextPhase =
        if gb.getCurrentPlayer().extraCard.isDefined then PlaySpecialCardPhase else EndTurnPhase
      updateGamePhase(gb, nextPhase)
    case PlaySpecialCardPhase  => gb.copy(gamePhase = PlaySpecialCardPhase)
    case EndTurnPhase          => handleWindPhase(gb.changeTurn())
      
    

  private def handleWindPhase(gb: GameBoard): GameBoard =
    val board = gb.board
    val direction = board.windDirection

    val availablePatternsEffect = PatternComputationResolver(
      VerySmallEffect(Map("a" -> Fire)),
      List(Rule(Struct.of("fire", Var.of("R")))),
      List(direction)
    ).getAvailableMoves(board)

    val b = board.applyEffect(Some(availablePatternsEffect))
    gb.copy(gamePhase = WindPhase, board = b)
