package it.unibo.view

import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.components.game.GameComponent

object GameBoardController:
  var gameComponent: Option[GameComponent] = None

  def initialize(component: GameComponent): Unit =
    gameComponent = Some(component)

  def refresh(grid: Grid): Unit =
    gameComponent.foreach(_.updateGrid(grid))