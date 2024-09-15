package it.unibo.view.component.game.gameboard.grid

import scala.collection.mutable
import scala.compiletime.uninitialized

import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import scalafx.scene.paint.Color

class GridState(val squareMap: mutable.Map[Position, GridSquare]):
  val hoveredCells: mutable.Map[Position, Color]               = mutable.Map()
  val fixedCell: mutable.Map[Position, Color]                  = mutable.Map()
  var availablePatterns: Set[Map[Position, Token]]             = Set.empty
  var availablePatternsClickFixed: Set[Map[Position, Token]]   = Set.empty
  var availablePatternsClickHovered: Set[Map[Position, Token]] = Set.empty
  var effectCode: Int                                          = uninitialized
  var currentGamePhase: GamePhase                              = uninitialized

  def resetHoverColors(): Unit =
    hoveredCells.foreach((position, color) => runOnUIThread(squareMap(position).updateColor(color)))
    hoveredCells.clear()
