package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.{UpdateWindDirection, ViewSubject}
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.{East, North, South, West}
import it.unibo.view.GUIType
import it.unibo.view.components.{IHaveConditionalView, IUpdateView}
import it.unibo.view.components.game.gameboard.sidebar.svg.{WindRoseArrow, WindRoseDirection}
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class WindRoseComponent(using observable: ViewSubject) extends IHaveConditionalView with IUpdateView:
  override val fxmlPath: String = GUIType.WindRose.fxmlPath

  @FXML
  private var northPane, westPane, centerPane, eastPane, southPane: Pane = uninitialized

  private val windRoseArrow = WindRoseArrow.create(South)

  private val windRoseDirections: Map[Direction, WindRoseDirection] = 
    Direction.values.map(d => d -> WindRoseDirection.create(d)).toMap

  private val windRoseEventHandler: Direction => EventHandler[MouseEvent] = dir =>
    ev =>
      windRoseArrow.updateDirection(dir)
      observable.onNext(UpdateWindDirection(dir))

  private var windRosePanes: Map[Direction, Pane] = Map.empty

  @FXML
  def initialize(): Unit =
    windRosePanes = Map(North -> northPane, South -> southPane, West -> westPane, East -> eastPane)
    windRosePanes.foreach((dir, pane) => pane.getChildren.add(windRoseDirections(dir).svgPath))
    centerPane.getChildren.add(windRoseArrow.svgPath)
    generalToggle()

  def updateWindRoseDirection(direction: Direction): Unit =
    runOnUIThread(windRoseArrow.updateDirection(direction))

  override def generalToggle(): Unit = windRosePanes.foreach((dir, pane) =>
    windRoseDirections(dir).toggleActivation(
      pane,
      () => pane.getStyleClass.add("disabled"),
      () => pane.getStyleClass.remove("disabled"),
      MouseEvent.MOUSE_CLICKED -> windRoseEventHandler(dir)
    )
  )
