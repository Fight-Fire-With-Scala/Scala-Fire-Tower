package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{ResolvePatternChoice, ViewSubject}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.controller.{InternalViewSubject, SetupActionPhase}
import scalafx.scene.paint.Color

import scala.collection.mutable

class GridEventHandler(
    observableSubject: ViewSubject,
    internalObservable: InternalViewSubject,
    squareMap: mutable.Map[Position, GridSquare]
):
  private val hoveredCellsOriginalColors: mutable.Map[Position, Color] = mutable.Map()
  private var availablePatterns: List[Map[Position, Token]] = List.empty
  private val hoverColor = Color.rgb(255, 0, 0, 0.5)

  def updateAvailablePatterns(ap : List[Map[Position, Token]]): Unit =
    availablePatterns = ap

  def handleCellClick(): Unit =
    val matchedPatterns: Map[Position, Token] = hoveredCellsOriginalColors.keys
      .flatMap { position =>
        availablePatterns.collect {
          case pattern if pattern.contains(position) => position -> pattern(position)
        }
      }.toMap
    if (matchedPatterns.nonEmpty) {
      hoveredCellsOriginalColors.clear()
      observableSubject.onNext(ResolvePatternChoice(matchedPatterns))
      internalObservable.onNext(SetupActionPhase())
    }

  def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    resetHoverColors()
    hoverDirection.direction match
      case Some(dir) =>
        val positionToCheck = checkNeighbor(Position(row, col), dir)
        val candidatePositions = availablePatterns.filter(_.contains(positionToCheck))
        candidatePositions.foreach { pattern =>
          if (pattern.keys.exists(_ == positionToCheck)) {
            val square = squareMap(positionToCheck)
            runOnUIThread {
              hoveredCellsOriginalColors += positionToCheck -> square.getColor
              square.updateColor(hoverColor)
            }
          }
        }
      case None      =>


  private def resetHoverColors(): Unit =
    hoveredCellsOriginalColors.foreach { case (position, color) =>
      val square = squareMap(position)
      runOnUIThread(square.updateColor(color))
    }
    hoveredCellsOriginalColors.clear()

  private def checkNeighbor(startPosition: Position, direction: Direction): Position =
    startPosition + direction.getDelta
