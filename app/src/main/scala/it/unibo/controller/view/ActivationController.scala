package it.unibo.controller.view

import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.model.gameboard.GamePhase.PlayStandardCardPhase
import it.unibo.model.gameboard.GamePhase.RedrawCardsPhase
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.view.component.game.GameComponent
import it.unibo.view.component.game.gameboard.sidebar.DeckComponent
import it.unibo.view.component.game.gameboard.sidebar.DiceComponent
import it.unibo.view.component.game.gameboard.sidebar.GameInfoComponent
import it.unibo.view.component.game.gameboard.sidebar.WindRoseComponent

trait ActivationController extends GameController:
  private def showTurnInfoOnly(component: GameComponent): Unit = component.sidebarComponent.components
    .foreach:
      case c: DeckComponent     => c.disableView()
      case c: DiceComponent     => c.disableView()
      case c: WindRoseComponent => c.disableView()
      case c: GameInfoComponent => c.enableView()

  def updateGamePhaseActivation(choice: GamePhase): Unit = choice match
    case WaitingPhase | RedrawCardsPhase => gameComponent.fold(()) { component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach:
          case c: DeckComponent     => c.enableView()
          case c: DiceComponent     => c.disableView()
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
      }
    case PlayStandardCardPhase           => gameComponent.fold(()) { component =>
        component.gridComponent.enableView()
        component.handComponent.enableView()
        component.handComponent.handleSpecialCardComponents(choice)
        showTurnInfoOnly(component)
      }
    case WindPhase                       => gameComponent.fold(()) { component =>
        component.gridComponent.enableView()
        component.handComponent.disableView()
        component.sidebarComponent.disableView()
      }
    case PlaySpecialCardPhase            => gameComponent.fold(()) { component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.handComponent.handleSpecialCardComponents(choice)
        showTurnInfoOnly(component)
      }
    case _                               => gameComponent.fold(()) { component =>
        component.gridComponent.disableView()
        component.handComponent.disableView()
        component.sidebarComponent.components.foreach:
          case c: DeckComponent     => c.disableView()
          case c: DiceComponent     => c.disableView()
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
      }
