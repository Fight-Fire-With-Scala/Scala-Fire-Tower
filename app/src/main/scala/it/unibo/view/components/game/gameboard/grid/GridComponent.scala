package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.model.logger
import it.unibo.view.GUIType
import it.unibo.view.components.{IGridComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.Node
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class GridComponent(using
    internalObservable: InternalViewSubject,
    observableSubject: ViewSubject
) extends IGridComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Grid.fxmlPath

  @FXML
  private var container: StackPane = uninitialized
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16
  private val squareSize = 42
  private var squareMap: mutable.Map[Position, GridSquare] = uninitialized
  private var gridInitializer: GridInitializer = uninitialized
  private var gridEventHandler: GridEventHandler = uninitialized

  @FXML
  def initialize(): Unit =
    gridInitializer = new GridInitializer(gridSize, squareSize, handleCellHover, handleCellClick)
    gridPane = new GridPane

    squareMap = gridInitializer.initializeGridSquares(gridPane)
    gridEventHandler = new GridEventHandler(observableSubject, internalObservable, squareMap)
    container.getChildren.add(gridPane)

  private def handleCellClick(): Unit = gridEventHandler.handleCellClick()

  private def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    gridEventHandler.handleCellHover(row, col, hoverDirection)

  def setAvailablePatterns(patterns: Set[Map[Position, Token]]): Unit =
    gridEventHandler.updateAvailablePatterns(patterns)

  override def onEnableView(): Unit = squareMap.foreach { case (_, square) => square.enableView() }

  override def onDisableView(): Unit = squareMap.foreach { case (_, square) =>
    square.disableView()
  }

  override protected def getPane: Node = gridPane

  import it.unibo.model.gameboard.grid.Cell.{EternalFire, Tower, Woods}
  import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}

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
