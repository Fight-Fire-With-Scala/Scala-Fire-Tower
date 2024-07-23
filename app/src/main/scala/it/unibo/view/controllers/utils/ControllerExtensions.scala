package it.unibo.view.controllers.utils

import it.unibo.controller.ControllerModule
import it.unibo.model.settings.Settings

trait Notifier:
  def doNotify(settings: Settings): Unit

class StartGameNotifier(startGameBlock: => Unit) extends Notifier:
  private lazy val startGame: Unit = startGameBlock
  override def doNotify(settings: Settings): Unit = startGame
