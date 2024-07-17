package it.unibo.view.controllers.utils

trait Notifier:
  def doNotify(): Unit

class StartGameNotifier extends Notifier:
  override def doNotify(): Unit = println("Starting game...")
