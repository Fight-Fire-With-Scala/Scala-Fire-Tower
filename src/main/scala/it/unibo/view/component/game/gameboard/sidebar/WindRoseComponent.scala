package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.UpdateWindDirection
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
import scala.compiletime.uninitialized

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
      observable.onNext(UpdateWindDirection(WindUpdateEffect.UpdateWind(dir)))
      observable.onNext(UpdateGamePhase(PhaseEffect(PlaySpecialCardPhase)))

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
    .filter((dir, pane) => dir == direction)
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
    logger.info(s"[ACTIVATION] Enabled ${this.getClass.getSimpleName}")
    windRoseArrow.enableView()
    windRosePanes.foreach((dir, pane) => windRoseDirections(dir).enableView())

  override def onDisableView(): Unit =
    logger.info(s"[ACTIVATION] Disabled ${this.getClass.getSimpleName}")
    super.onDisableView()
    windRoseArrow.disableView()
    windRosePanes.foreach((dir, pane) =>
      windRoseDirections(dir).disableView()
      pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, windRoseEventHandler(dir))
    )

  override protected def getPane: Node = basePane
