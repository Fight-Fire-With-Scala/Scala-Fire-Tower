package it.unibo.controller.view

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.view.components.game.GameComponent

trait GameController:
  var gameComponent: Option[GameComponent] = None
  val observable: ViewSubject
  val internalObservable: InternalViewSubject

  def initialize(component: GameComponent): Unit = gameComponent = Some(component)
