package it.unibo.view.components.game.gameboard.grid.handler

import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.components.game.gameboard.grid.{EffectType, GridSquare}
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized

class GridState(val squareMap: mutable.Map[Position, GridSquare]):
  val hoveredCells: mutable.Map[Position, Color] = mutable.Map()
  val fixedCell: mutable.Map[Position, Color] = mutable.Map()
  var availablePatterns: Set[Map[Position, Token]] = Set.empty
  var availablePatternsClickFixed: Set[Map[Position, Token]] = Set.empty
  var effectCode: EffectType = uninitialized
  var currentGamePhase: GamePhase = uninitialized

  def resetHoverColors(): Unit =
    hoveredCells.foreach((position, color) => runOnUIThread(squareMap(position).updateColor(color)))
    hoveredCells.clear()