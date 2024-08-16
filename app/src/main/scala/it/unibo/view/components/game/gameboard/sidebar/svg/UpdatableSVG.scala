package it.unibo.view.components.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction

trait UpdatableSVG[T] extends IHaveSVGView[T]:
  def updateDirection(direction: Direction): T
