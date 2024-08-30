package it.unibo.view

import it.unibo.model.gameboard.GamePhase.{PlayCard, RedrawCards, WaitingPhase, WindPhase}
import it.unibo.model.gameboard.{GameBoard, GamePhase}
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.game.gameboard.sidebar.{DeckComponent, GameInfoComponent, WindRoseComponent}

class TurnViewController extends RefreshManager with DiscardManager with EnableDisableManager

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
  def updateGamePhase(choice: GamePhase): Unit =
    choice match
      case WaitingPhase =>
        gameComponent.fold(()) { component =>
          component.gridComponent.disableView()
          component.handComponent.enableView()
          component.sidebarComponent.components.foreach {
            case d: DeckComponent => d.enableView()
            case cp: WindRoseComponent => cp.disableView()
            case cp: GameInfoComponent => cp.disableView()
          }
        }
      case RedrawCards =>
        gameComponent.fold(()) { component =>
          component.gridComponent.disableView()
          component.handComponent.enableView()
          component.sidebarComponent.components.foreach {
            case d: DeckComponent => d.enableView()
            case cp: WindRoseComponent => cp.disableView()
            case cp: GameInfoComponent => cp.disableView()
          }
        }
      case PlayCard =>
        gameComponent.fold(()) { component =>
          component.gridComponent.enableView()
          component.handComponent.enableView()
          component.sidebarComponent.components.foreach {
            case d: DeckComponent => d.disableView()
            case cp: WindRoseComponent => cp.enableView() // TODO if wind yes, otherwise no
            case cp: GameInfoComponent => cp.enableView() // TODO if wind yes, otherwise no
          }
        }
      case WindPhase =>
        gameComponent.fold(()) { component =>
          component.gridComponent.enableView()
          component.handComponent.disableView()
          component.sidebarComponent.components.foreach {
            case d: DeckComponent => d.disableView()
            case cp: WindRoseComponent => cp.disableView()
            case cp: GameInfoComponent =>  cp.disableView()
          }
        }

trait RefreshManager extends ComponentManager:
  def refresh(gameBoard: GameBoard): Unit =
    val currentGamePhase = gameBoard.gamePhase
    logger.info(s"$currentGamePhase")
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