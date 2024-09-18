package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import it.unibo.controller.UpdateGamePhaseMessage
import it.unibo.controller.UpdateWindDirectionMessage
import it.unibo.controller.ViewSubject
import it.unibo.model.effect.card.WindUpdateEffect
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.East
import it.unibo.model.gameboard.Direction.North
import it.unibo.model.gameboard.Direction.South
import it.unibo.model.gameboard.Direction.West
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.view.{ logger, GUIType }
import it.unibo.view.component.ISidebarComponent
import it.unibo.view.component.IUpdateView
import it.unibo.view.component.game.gameboard.sidebar.svg.WindRoseArrow
import it.unibo.view.component.game.gameboard.sidebar.svg.WindRoseDirection
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

//noinspection VarCouldBeVal
final class WindRoseComponent(using observable: ViewSubject)
    extends ISidebarComponent
    with IUpdateView:
  override val fxmlPath: String = GUIType.WindRose.fxmlPath

  @FXML
  private var basePane: Pane = uninitialized

  @FXML
  private var northPane, westPane, centerPane, eastPane, southPane: Pane = uninitialized

  private val windRoseArrow = WindRoseArrow.create(South)

  private val windRoseDirections: Map[Direction, WindRoseDirection] = Direction.values
    .map(d => d -> WindRoseDirection.create(d))
    .toMap

  private val windRoseEventHandler: Direction => EventHandler[MouseEvent] = dir =>
    ev =>
      observable.onNext(UpdateWindDirectionMessage(WindUpdateEffect.UpdateWind(dir)))
      observable.onNext(UpdateGamePhaseMessage(PhaseEffect(PlaySpecialCardPhase)))

  private var windRosePanes: Map[Direction, Pane] = Map.empty

  private var currentAllowedDirection: Direction = uninitialized

  @FXML
  def initialize(): Unit =
    windRosePanes = Map(North -> northPane, South -> southPane, West -> westPane, East -> eastPane)
    windRosePanes.foreach((dir, pane) => pane.getChildren.add(windRoseDirections(dir).svgPath))
    centerPane.getChildren.add(windRoseArrow.svgPath)

  def updateWindRoseDirection(direction: Direction): Unit =
    runOnUIThread(windRoseArrow.updateDirection(direction))

  def onWindDirectionRequest(direction: Direction): Unit = windRosePanes
    .filter((dir, _) => dir == direction)
    .foreach((dir, pane) =>
      pane.addEventHandler(MouseEvent.MOUSE_CLICKED, windRoseEventHandler(dir))
    )

  def allowInteraction(dir: Direction): Unit =
    currentAllowedDirection = dir
    basePane.addEventHandler(
      MouseEvent.MOUSE_CLICKED,
      windRoseEventHandler(currentAllowedDirection)
    )

  def disallowInteraction(): Unit =
    basePane.addEventHandler(
      MouseEvent.MOUSE_CLICKED,
      windRoseEventHandler(currentAllowedDirection)
    )

  override def onEnableView(): Unit =
    super.onEnableView()
    windRoseArrow.enableView()
    windRosePanes.foreach((dir, _) => windRoseDirections(dir).enableView())

  override def onDisableView(): Unit =
    super.onDisableView()
    windRoseArrow.disableView()
    windRosePanes.foreach((dir, pane) =>
      windRoseDirections(dir).disableView()
      pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, windRoseEventHandler(dir))
    )

  override protected def getPane: Node = basePane
