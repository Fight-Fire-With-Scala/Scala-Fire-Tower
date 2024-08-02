package it.unibo.view.controllers

import it.unibo.controller.{ControllerModule, ViewSubject}
import it.unibo.view.GUIType
import it.unibo.view.controllers.gameboard.GridController
import it.unibo.view.controllers.hand.HandController
import it.unibo.view.controllers.hand.cards.CardController
import it.unibo.view.controllers.menu.MenuController
import it.unibo.view.controllers.utils.StartGameNotifier
import monix.reactive.subjects.PublishSubject

object ControllerFactory:
  def createController(gui: GUIType)(observableSubject: ViewSubject): GraphicController =
    gui match
      case GUIType.Menu => new MenuController(observableSubject)
      case GUIType.Grid => new GridController(observableSubject)
      case GUIType.Hand => new HandController()
      case GUIType.Card => new CardController()
