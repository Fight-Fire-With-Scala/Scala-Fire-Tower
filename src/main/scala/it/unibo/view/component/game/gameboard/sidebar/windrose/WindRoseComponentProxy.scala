package it.unibo.view.component.game.gameboard.sidebar.windrose

import it.unibo.controller.{ UpdateGamePhaseMessage, UpdateWindDirectionMessage, ViewSubject }
import it.unibo.model.effect.card.WindUpdateEffect
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.{ East, North, South, West }
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.view.component.game.gameboard.sidebar.svg.{ WindRoseArrow, WindRoseDirection }
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

final class WindRoseComponentProxy(using observable: ViewSubject) extends WindRoseComponentService:

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

  def initialize(
      northPane: Pane,
      westPane: Pane,
      centerPane: Pane,
      eastPane: Pane,
      southPane: Pane
  ) =
    windRosePanes = Map(North -> northPane, South -> southPane, West -> westPane, East -> eastPane)
    windRosePanes.foreach((dir, pane) => pane.getChildren.add(windRoseDirections(dir).svgPath))
    centerPane.getChildren.add(windRoseArrow.svgPath)

  def updateWindRoseDirection(direction: Direction): Unit = windRoseArrow.updateDirection(direction)

  def onWindDirectionRequest(direction: Direction): Unit = windRosePanes
    .filter((dir, _) => dir == direction)
    .foreach((dir, pane) =>
      pane.addEventHandler(MouseEvent.MOUSE_CLICKED, windRoseEventHandler(dir))
    )

  def allowInteraction(pane: Pane, dir: Direction): Unit =
    currentAllowedDirection = dir
    pane.addEventHandler(
      MouseEvent.MOUSE_CLICKED,
      windRoseEventHandler(currentAllowedDirection)
    )

  def disallowInteraction(pane: Pane): Unit =
    pane.removeEventHandler(
      MouseEvent.MOUSE_CLICKED,
      windRoseEventHandler(currentAllowedDirection)
    )

  def onEnableView(): Unit =
    windRoseArrow.enableView()
    windRosePanes.foreach((dir, _) => windRoseDirections(dir).enableView())

  def onDisableView(): Unit =
    windRoseArrow.disableView()
    windRosePanes.foreach((dir, pane) =>
      windRoseDirections(dir).disableView()
      pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, windRoseEventHandler(dir))
    )
