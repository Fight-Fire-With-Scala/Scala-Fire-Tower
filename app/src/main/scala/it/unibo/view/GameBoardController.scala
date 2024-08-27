package it.unibo.view

import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.game.GameComponent
import it.unibo.model.gameboard.GamePhase.ActionPhase
import it.unibo.view.components.game.gameboard.sidebar.{DeckComponent, GameInfoComponent, WindRoseComponent}

class GameBoardController extends RefreshManager with DiscardManager with EnableDisableManager

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

trait EnableDisableManager extends ComponentManager:
  def handleStartWindPhase(): Unit =
    gameComponent.fold(()) { component =>
      component.gridComponent.enableView()
    }

  def handleStartActionPhase(): Unit =
    gameComponent.fold(()) { component =>
      component.gridComponent.disableView()
//      component.sidebarComponent.components.foreach {
//        case d: DeckComponent =>
//        case cp: WindRoseComponent => cp.disableView()
//        case cp: GameInfoComponent =>
//          cp.disableView()
//          cp.updateTurnPhase(ActionPhase.toString)
//      }
    }

trait RefreshManager extends ComponentManager:
  def refresh(gameBoard: GameBoard): Unit = gameComponent.fold(()) { component =>
    component.updateGrid(gameBoard.board.grid)
    component.updatePlayer(gameBoard.currentPlayer)
  }
