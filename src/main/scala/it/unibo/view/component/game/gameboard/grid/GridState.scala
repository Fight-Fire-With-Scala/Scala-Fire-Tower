package it.unibo.view.component.game.gameboard.grid

import scala.collection.mutable
import scala.compiletime.uninitialized

import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.Position
import scalafx.scene.paint.Color
import it.unibo.model.gameboard.Pattern

/**
 * The GridState class maintains the state of the grid, including hovered cells, fixed cells, available patterns, and the current game phase.
 *
 * @param squareMap the map of grid positions to GridSquare objects
 */
final class GridState(val squareMap: mutable.Map[Position, GridSquare]):
  val hoveredCells: mutable.Map[Position, Color]               = mutable.Map()
  val fixedCell: mutable.Map[Position, Color]                  = mutable.Map()
  var availablePatterns: Set[Pattern]             = Set.empty
  var availablePatternsClickFixed: Set[Pattern]   = Set.empty
  var availablePatternsClickHovered: Set[Pattern] = Set.empty
  var effectCode: Int                                          = uninitialized
  var currentGamePhase: GamePhase                              = uninitialized

  def resetHoverColors(): Unit =
    hoveredCells.foreach((position, color) => runOnUIThread(squareMap(position).updateColor(color)))
    hoveredCells.clear()
