package it.unibo.controller.view

import it.unibo.model.gameboard.GamePhase.{
  ExtraActionPhase,
  PlayCardPhase,
  RedrawCardsPhase,
  WaitingPhase,
  WindPhase
}
import it.unibo.model.gameboard.GamePhase
import it.unibo.view.components.game.gameboard.sidebar.{
  DeckComponent,
  GameInfoComponent,
  WindRoseComponent
}

trait ActivationController extends GameController:
  def updateGamePhase(choice: GamePhase): Unit = choice match
    case WaitingPhase | RedrawCardsPhase => gameComponent.fold(()) { component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach {
          case d: DeckComponent      => d.enableView()
          case cp: WindRoseComponent => cp.disableView()
          case cp: GameInfoComponent => cp.disableView()
        }
      }
    case PlayCardPhase                   => gameComponent.fold(()) { component =>
        component.gridComponent.enableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach {
          case d: DeckComponent      => d.disableView()
          case cp: WindRoseComponent => cp.enableView() // TODO if wind yes, otherwise no
          case cp: GameInfoComponent => cp.enableView() // TODO if wind yes, otherwise no
        }
      }
    case WindPhase                       => gameComponent.fold(()) { component =>
        component.gridComponent.enableView()
        component.handComponent.disableView()
        component.sidebarComponent.disableView()
      }
    case ExtraActionPhase                => gameComponent.fold(()) { component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.sidebarComponent.disableView()
      }
