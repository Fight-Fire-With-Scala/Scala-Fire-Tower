package it.unibo.view.components

import it.unibo.controller.ViewSubject
import it.unibo.view.GUIType
import it.unibo.view.GUIType.*
import it.unibo.view.components.menu.MenuComponent
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.{CardComponent, HandComponent}

object ComponentFactory:
  def createFXMLComponent(gui: GUIType)(observableSubject: ViewSubject): GraphicComponent =
    gui match
      case Menu => MenuComponent(observableSubject)
      case Game => GameComponent()
