package it.unibo.view

import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.game.gameboard.sidebar.{DeckComponent, GameInfoComponent, WindRoseComponent}

//change name into InternalViewController
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
  def handleStartWindPhase(): Unit = gameComponent.fold(()) { component =>
    component.gridComponent.enableView()
  }

  def handleStartActionPhase(): Unit = gameComponent.fold(()) { component =>
    component.gridComponent.disableView()
    component.sidebarComponent.components.foreach {
      case d: DeckComponent      => d.disableView()
      case cp: WindRoseComponent => cp.disableView()
      case cp: GameInfoComponent => cp.disableView()
    }
  }

  // def handleActionPhaseChoice(choice: ActionPhaseChoice): Unit = ???
//    choice match
//      case WaitingAction =>
//        gameComponent.fold(()) { component =>
//          component.gridComponent.disableView()
//          component.handComponent.enableView()
//          component.sidebarComponent.components.foreach {
//            case d: DeckComponent => d.enableView()
//            case cp: WindRoseComponent => cp.disableView()
//            case cp: GameInfoComponent => cp.disableView()
//          }
//        }
//      case RedrawCards =>
//        gameComponent.fold(()) { component =>
//          component.gridComponent.disableView()
//          component.handComponent.enableView()
//          component.sidebarComponent.components.foreach {
//            case d: DeckComponent => d.enableView()
//            case cp: WindRoseComponent => cp.disableView()
//            case cp: GameInfoComponent => cp.disableView()
//          }
//        }
//      case PlayCard =>
//        gameComponent.fold(()) { component =>
//          component.gridComponent.enableView()
//          component.handComponent.enableView()
//          component.sidebarComponent.components.foreach {
//            case d: DeckComponent => d.disableView()
//            case cp: WindRoseComponent => // if wind yes, otherwise no
//            case cp: GameInfoComponent => // if wind yes, otherwise no
//          }
//        }

trait RefreshManager extends ComponentManager:
  def refresh(gameBoard: GameBoard): Unit =
    val currentGamePhase = gameBoard.gamePhase
    gameComponent.fold(()) { component =>
      component.updateGrid(gameBoard.board.grid)
      component.updatePlayer(gameBoard.currentPlayer)
      component.sidebarComponent.components.collectFirst { case component: GameInfoComponent => component } match
        case Some(component) => component.updateTurnPhase(currentGamePhase.toString)
        case None => logger.warn(s"Component not found")
    }

  def candidateCardToPlay(cardId: Int): Unit = gameComponent.fold(()) { component =>
    component.handComponent.cardToPlay_=(cardId)
  }