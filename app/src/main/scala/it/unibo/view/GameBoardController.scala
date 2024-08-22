package it.unibo.view

import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.game.GameComponent

class GameBoardController extends RefreshManager with DiscardManager

trait ComponentManager:
  var gameComponent: Option[GameComponent] = None

  def initialize(component: GameComponent): Unit = gameComponent = Some(component)

trait DiscardManager extends ComponentManager:
  def discard(): Unit = gameComponent.fold(()) { component =>
    println("Discard procedure called")
//    component.handComponent.startDiscardProcedure(println)
  }

trait RefreshManager extends ComponentManager:
  def refresh(gameBoard: GameBoard): Unit =
    gameComponent.fold(()) { component =>
      component.updateGrid(gameBoard.board.grid)
      component.updatePlayer(gameBoard.currentPlayer)
    }
