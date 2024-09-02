package it.unibo.controller.view

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.view.components.game.GameComponent

trait GameController:
  var gameComponent: Option[GameComponent] = None
  val viewObservable: ViewSubject
  val internalObservable: InternalViewSubject

  def initialize(component: GameComponent): Unit = gameComponent = Some(component)
