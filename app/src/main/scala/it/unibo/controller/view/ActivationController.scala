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
  DiceComponent,
  GameInfoComponent,
  WindRoseComponent
}

trait ActivationController extends GameController:
  def updateGamePhase(choice: GamePhase): Unit = choice match
    case WaitingPhase | RedrawCardsPhase => gameComponent.fold(()) { component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach {
          case c: DeckComponent     => c.enableView()
          case c: DiceComponent     => c.disableView()
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
        }
      }
    case PlayCardPhase                   => gameComponent.fold(()) { component =>
        component.gridComponent.enableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach {
          case c: DeckComponent     => c.disableView()
          case c: DiceComponent     => c.enableView()
          case c: WindRoseComponent => c.enableView() // TODO if wind yes, otherwise no
          case c: GameInfoComponent => c.enableView() // TODO if wind yes, otherwise no
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
        component.sidebarComponent.components.foreach {
          case c: DeckComponent     => c.disableView()
          case c: DiceComponent     => c.disableView()
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
        }
      }
