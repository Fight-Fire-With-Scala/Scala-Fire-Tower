package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{ResolvePatternChoice, ViewSubject}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.gameboard.grid.ConcreteToken.*
import it.unibo.view.components.{GraphicComponent, IHaveView, IUpdateView}
import javafx.fxml.FXML
import it.unibo.view.{logger, GUIType}
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.scene.Node
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized
import scala.language.postfixOps

//noinspection VarCouldBeVal
final class GridComponent(observableSubject: ViewSubject)
    extends GraphicComponent with IHaveView with IUpdateView:

  override val fxmlPath: String = GUIType.Grid.fxmlPath

  @FXML
  private var container: StackPane = uninitialized
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16
  private val squareSize = 42
  private val squareMap: mutable.Map[Position, GridSquare] = mutable.Map()
  private var hoveredCellsOriginalColors: mutable.Map[Position, Color] = mutable.Map()
  var availablePatterns: List[Map[Position, Token]] = List.empty
  private var hoverEnabled: Boolean = true

  @FXML
  def initialize(): Unit =
    gridPane = new GridPane
    for {
      row <- 0 until gridSize
      col <- 0 until gridSize
    } {
      val square = GridSquare(row, col, squareSize, handleCellHover, handleCellClick)
      GridPane.setRowIndex(square.getGraphicPane, row)
      GridPane.setColumnIndex(square.getGraphicPane, col)
      gridPane.children.add(square.getGraphicPane)
      squareMap(Position(row, col)) = square
    }
    squareMap.foreach { case (_, square) =>
      square.toggleRectangleActivation()
    }
    container.getChildren.add(gridPane)

  private def handleCellClick(): Unit =
    val matchedPatterns: Map[Position, Token] = hoveredCellsOriginalColors.keys
      .flatMap { position =>
        availablePatterns.collect {
          case pattern if pattern.contains(position) => position -> pattern(position)
        }
      }.toMap
    if (matchedPatterns.nonEmpty) {
      hoverEnabled = false
      hoveredCellsOriginalColors.clear()
      observableSubject.onNext(ResolvePatternChoice(matchedPatterns))
    }

  private def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    if (!hoverEnabled) return
    resetHoverColors()
    hoverDirection.direction match
      case Some(dir) =>
        val hoverColor = Color.rgb(255, 0, 0, 0.5)

        val positionToCheck = checkNeighbor(Position(row, col), dir)
        val candidatePositions = availablePatterns.filter(_.contains(positionToCheck))

        candidatePositions.foreach { pattern =>
          if (pattern.keys.exists(_ == positionToCheck))
            val square = squareMap(positionToCheck)
            runOnUIThread {
              hoveredCellsOriginalColors += positionToCheck -> square.getColor
              square.updateColor(hoverColor)
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

  def updateGrid(grid: Grid): Unit = squareMap.foreach { case (position, square) =>
    val cellColor = grid.getCell(position) match
      case Some(_: Woods.type)       => Color.DarkGreen
      case Some(_: Tower.type)       => Color.rgb(76, 39, 3)
      case Some(_: EternalFire.type) => Color.Red
      case _                         => Color.White

    val tokenColor = grid.getToken(position) match
      case Some(Fire)      => Color.Orange
      case Some(Firebreak) => Color.Blue
      case _               => cellColor

    runOnUIThread(square.updateColor(tokenColor))
  }
