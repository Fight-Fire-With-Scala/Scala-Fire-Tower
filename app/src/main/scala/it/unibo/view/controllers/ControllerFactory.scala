package it.unibo.view.controllers

import it.unibo.controller.ControllerModule
import it.unibo.view.GUIType
import it.unibo.view.controllers.gameboard.GridController
import it.unibo.view.controllers.hand.HandController
import it.unibo.view.controllers.hand.cards.CardController
import it.unibo.view.controllers.menu.MenuController
import it.unibo.view.controllers.utils.StartGameNotifier

object ControllerFactory:
  def createController(gui: GUIType)(controller: ControllerModule.Controller): (GraphicController) =
    gui match
      case GUIType.Menu =>
        new MenuController(new StartGameNotifier(controller.notifyStartGameSession()))
      case GUIType.Grid => new GridController()
      case GUIType.Hand => new HandController()
      case GUIType.Card => new CardController()
