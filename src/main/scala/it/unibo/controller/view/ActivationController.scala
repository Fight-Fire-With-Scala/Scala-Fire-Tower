package it.unibo.controller.view

import it.unibo.model.gameboard.{ Direction, GamePhase }
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
  protected def hideWindChoices(component: Option[GameComponent]): Unit =
    component.foreach: c =>
      c.sidebarComponent.components.foreach:
        case c: DiceComponent => c.disableView()
        case c: WindRoseComponent =>
          c.disableView()
          c.disallowInteraction()
        case _ =>

  protected def showWindChoices(component: Option[GameComponent], direction: Direction): Unit =
    component.foreach: c =>
      c.sidebarComponent.components.foreach:
        case c: DiceComponent => c.enableView()
        case c: WindRoseComponent =>
          c.enableView()
          c.allowInteraction(direction)
        case _ =>

  private def showTurnInfoOnly(component: GameComponent): Unit =
    component.sidebarComponent.components.foreach:
      case c: DeckComponent     => c.disableView()
      case c: DiceComponent     => c.disableView()
      case c: WindRoseComponent => c.disableView()
      case c: GameInfoComponent => c.enableView()

  def updateGamePhaseActivation(choice: GamePhase): Unit = choice match
    case WaitingPhase | RedrawCardsPhase =>
      gameComponent.foreach: component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach:
          case c: DeckComponent     => c.enableView()
          case c: DiceComponent     => c.disableView()
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()

    case PlayStandardCardPhase =>
      gameComponent.foreach: component =>
        component.gridComponent.enableView()
        component.handComponent.enableView()
        component.handComponent.handleSpecialCardComponents(choice)
        component.sidebarComponent.components
          .foreach:
            case c: DeckComponent     => c.disableView()
            case c: DiceComponent     =>
            case c: WindRoseComponent =>
            case c: GameInfoComponent => c.disableView()

    case WindPhase =>
      gameComponent.foreach: component =>
        component.gridComponent.enableView()
        component.handComponent.disableView()
        component.sidebarComponent.components
          .foreach:
            case c: DeckComponent     => c.disableView()
            case c: DiceComponent     => c.disableView()
            case c: WindRoseComponent => c.disableView()
            case c: GameInfoComponent => c.disableView()

    case PlaySpecialCardPhase =>
      gameComponent.foreach: component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.handComponent.handleSpecialCardComponents(choice)
        component.sidebarComponent.components
          .foreach:
            case c: DeckComponent     => c.disableView()
            case c: DiceComponent     => c.disableView()
            case c: WindRoseComponent => c.disableView()
            case c: GameInfoComponent => c.enableView()

    case _ =>
      gameComponent.foreach: component =>
        component.gridComponent.disableView()
        component.handComponent.disableView()
        component.sidebarComponent.components.foreach:
          case c: DeckComponent     => c.disableView()
          case c: DiceComponent     => c.disableView()
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
