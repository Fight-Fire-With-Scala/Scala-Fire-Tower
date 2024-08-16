package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.{East, North, South, West}
import it.unibo.view.components.{ICanBeDisabled, IHaveView}
import it.unibo.view.components.game.gameboard.sidebar.svg.{WindRoseArrow, WindRoseDirection}
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class WindRoseComponent(using observable: ViewSubject) extends IHaveView with ICanBeDisabled:
  override val fxmlPath: String = "/pages/windRose.fxml"

  @FXML
  private var northPane, westPane, centerPane, eastPane, southPane: Pane = uninitialized

  private val windRoseArrow = WindRoseArrow.create(South)

  private val windRoseDirections = Direction.values.map(d => d -> WindRoseDirection.create(d)).toMap

  private val windRoseEventHandler: Direction => EventHandler[MouseEvent] =
    dir => ev => windRoseArrow.updateDirection(dir)

  private var windRosePanes: Map[Direction, Pane] = Map.empty

  @FXML
  def initialize(): Unit =
    windRosePanes = Map(North -> northPane, South -> southPane, West -> westPane, East -> eastPane)
    windRosePanes.foreach((dir, pane) =>
      pane.getChildren.add(windRoseDirections(dir).svgPath)
      toggleActivation(pane, windRoseEventHandler(dir))
    )

    windRoseArrow.updateDirection(South)
    centerPane.getChildren.add(windRoseArrow.svgPath)

  def updateWindRoseDirection(direction: Direction): Unit = windRoseArrow.updateDirection(direction)