package it.unibo.view.controllers.utils

import it.unibo.controller.ControllerModule.given
import it.unibo.view.controllers.menu.ControllerMenuImpl
import it.unibo.controller.StartGameNotifier

extension (controllerMenuImpl: ControllerMenuImpl)
  def notifyGameStart()(using notifier: StartGameNotifier): Unit =
    notifier.notifyStartGame()