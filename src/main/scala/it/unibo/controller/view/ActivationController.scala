package it.unibo.controller.view

import it.unibo.model.gameboard.{ Direction, GamePhase }
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.model.gameboard.GamePhase.PlayStandardCardPhase
import it.unibo.model.gameboard.GamePhase.RedrawCardsPhase
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.view.component.game.GameComponent
import it.unibo.view.component.game.gameboard.sidebar.{ BicolumnPaneComponent, DeckComponent, DiceComponent, GameInfoComponent, WindRoseComponent }

trait ActivationController extends GameController:
  protected def hideWindChoices(component: Option[GameComponent]): Unit =
    component.foreach: c =>
      c.sidebarComponent.components.foreach:
        case c: BicolumnPaneComponent =>
          c.rightComponent match
            case c: DiceComponent => c.disableView()
        case c: WindRoseComponent =>
          c.disableView()
          c.disallowInteraction()
        case _ =>

  protected def showWindChoices(component: Option[GameComponent], direction: Direction): Unit =
    component.foreach: c =>
      c.sidebarComponent.components.foreach:
        case c: BicolumnPaneComponent =>
          c.rightComponent match
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

  private def disableBicolumnComponent(component: GameComponent): Unit =
    component.sidebarComponent.components.foreach:
      case c: BicolumnPaneComponent =>
        c.rightComponent match
          case c: DiceComponent => c.disableView()
        c.leftComponent match
          case c: DeckComponent => c.disableView()
      case _ =>

  def updateGamePhaseActivation(choice: GamePhase): Unit = choice match
    case WaitingPhase | RedrawCardsPhase =>
      gameComponent.foreach: component =>
        component.gridComponent.disableView()
        component.handComponent.enableView()
        component.sidebarComponent.components.foreach:
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
          case c: BicolumnPaneComponent =>
            c.rightComponent match
              case c: DiceComponent => c.disableView()
            c.leftComponent match
              case c: DeckComponent => c.enableView()

    case PlayStandardCardPhase =>
      gameComponent.foreach: component =>
        component.gridComponent.enableView()
        component.handComponent.enableView()
        component.handComponent.handleSpecialCardComponents(choice)
        component.sidebarComponent.components
          .foreach:
            case c: WindRoseComponent =>
            case c: GameInfoComponent => c.disableView()
            case c: BicolumnPaneComponent =>
              c.leftComponent match
                case c: DeckComponent => c.disableView()

    case WindPhase =>
      gameComponent.foreach: component =>
        disableBicolumnComponent(component)
        component.gridComponent.enableView()
        component.handComponent.disableView()
        component.sidebarComponent.components
          .foreach:
            case c: GameInfoComponent => c.disableView()
            case c: WindRoseComponent => c.disableView()
            case c: BicolumnPaneComponent =>

    case PlaySpecialCardPhase =>
      gameComponent.foreach: component =>
        disableBicolumnComponent(component)
        component.gridComponent.enableView()
        component.handComponent.enableView()
        component.handComponent.handleSpecialCardComponents(choice)
        component.sidebarComponent.components
          .foreach:
            case c: WindRoseComponent => c.disableView()
            case c: GameInfoComponent => c.enableView()
            case c: BicolumnPaneComponent =>

    case _ =>
      gameComponent.foreach: component =>
        disableBicolumnComponent(component)
        component.gridComponent.disableView()
        component.handComponent.disableView()
        component.sidebarComponent.components.foreach:
          case c: WindRoseComponent => c.disableView()
          case c: GameInfoComponent => c.disableView()
          case c: BicolumnPaneComponent =>
