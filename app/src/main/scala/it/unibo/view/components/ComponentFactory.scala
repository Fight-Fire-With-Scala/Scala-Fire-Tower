package it.unibo.view.components

import it.unibo.controller.{ControllerModule, ViewSubject}
import it.unibo.view.GUIType
import it.unibo.view.components.deck.DeckComponent
import it.unibo.view.components.gameboard.GridComponent
import it.unibo.view.components.hand.HandComponent
import it.unibo.view.components.hand.cards.CardComponent
import it.unibo.view.components.menu.MenuComponent
import monix.reactive.subjects.PublishSubject

object ComponentFactory:
  def createFXMLComponent(gui: GUIType)(observableSubject: ViewSubject): GraphicComponent =
    gui match
      case GUIType.Menu => new MenuComponent(observableSubject)
      case GUIType.Grid => new GridComponent(observableSubject)
      case GUIType.Deck => new DeckComponent(observableSubject)
      case GUIType.Hand => new HandComponent()
      case GUIType.Card => new CardComponent()
