package it.unibo.view

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.components.game.GameComponent

object GameBoardController:
  var gameComponent: Option[GameComponent] = None

  def initialize(component: GameComponent): Unit = gameComponent = Some(component)

  def refresh(gameBoard: GameBoard): Unit = gameComponent.fold(()) { component =>
    component.updateGrid(gameBoard.board.grid)
    component.updatePlayer(gameBoard.currentPlayer)
  }
