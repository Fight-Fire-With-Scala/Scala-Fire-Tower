package it.unibo.view

import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.game.GameComponent

class GameBoardController extends RefreshManager with DiscardManager

trait ComponentManager:
  var gameComponent: Option[GameComponent] = None

  def initialize(component: GameComponent): Unit = gameComponent = Some(component)

trait DiscardManager extends ComponentManager:
  def confirmDiscard(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.discardCards()
  }

  def cancelDiscard(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.endDiscardProcedure()
  }

  def initDiscardProcedure(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.initDiscardProcedure()
  }

  def toggleCardInDiscardList(cardId: Int): Unit = gameComponent.fold(()) { component =>
    component.handComponent.toggleCardInDiscardList(cardId)
  }

trait RefreshManager extends ComponentManager:
  def refresh(gameBoard: GameBoard): Unit = gameComponent.fold(()) { component =>
    component.updateGrid(gameBoard.board.grid)
    component.updatePlayer(gameBoard.currentPlayer)
  }
