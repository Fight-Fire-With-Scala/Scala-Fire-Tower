package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.view.GUIType
import it.unibo.view.components.{GraphicComponent, IHaveView, IUpdateView}
import javafx.fxml.FXML
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized

final class GridComponent(observableSubject: ViewSubject)
    extends GraphicComponent with IHaveView with IUpdateView:

  override val fxmlPath: String = GUIType.Grid.fxmlPath

  @FXML
  private var container: StackPane = uninitialized
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16
  private val squareSize = 42
  var availablePatterns: List[Map[Position, Token]] = List.empty

  private val gridInitializer = new GridInitializer(gridSize, squareSize, handleCellHover, handleCellClick)
  private val gridEventHandler = new GridEventHandler(observableSubject, gridInitializer.squareMap, availablePatterns)

  @FXML
  def initialize(): Unit = {
    gridPane = new GridPane
    gridInitializer.initializeGridSquares(gridPane)
    container.getChildren.add(gridPane)
  }

  private def handleCellClick(): Unit = gridEventHandler.handleCellClick()

  private def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit = gridEventHandler.handleCellHover(row, col, hoverDirection)

  import it.unibo.model.gameboard.grid.Cell.{Woods, Tower, EternalFire}
  import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}

  def updateGrid(grid: Grid): Unit = gridInitializer.squareMap.foreach { case (position, square) =>
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