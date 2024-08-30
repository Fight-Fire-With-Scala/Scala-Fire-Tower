package it.unibo.model

import alice.tuprolog.{Struct, Var}
import it.unibo.controller.{logger, ShowAvailablePatterns}
import it.unibo.model.ModelModule.Model
import it.unibo.model.cards.choices.StepChoice
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.{GameBoard, GamePhase}
import it.unibo.model.gameboard.GamePhase.{PlayCard, RedrawCards, WaitingPhase, WindPhase}
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

case class TurnModelController():
  def handleWindPhase(model: Model): Unit =
    val gameBoard = model.getGameBoard
    val board = model.getGameBoard.board
    val direction = board.windDirection

    val availablePatternsEffect = PatternComputationResolver(
      VerySmallEffect(Map("a" -> Fire)),
      Rule(Struct.of("fire", Var.of("R"))),
      List(direction)
    ).getAvailableMoves(board)
    
    val b = board.applyEffect(Some(availablePatternsEffect))
    model.setGameBoard(gameBoard.copy(board = b))
    model.getObservable.onNext(ShowAvailablePatterns(model.getGameBoard.board.availablePatterns))

  def updateGamePhase(model: Model, choice: GamePhase): Unit =
    val gameBoard = model.getGameBoard
    choice match
      case RedrawCards  => ???
      case PlayCard     => model.setGameBoard(gameBoard.changeTurnPhase(PlayCard))
      case WindPhase    => handleWindPhase(model)
      case WaitingPhase => model.setGameBoard(gameBoard.changeTurnPhase(WaitingPhase))
