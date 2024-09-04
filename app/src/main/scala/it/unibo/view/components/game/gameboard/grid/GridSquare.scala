package it.unibo.view.components.game.gameboard.grid

import it.unibo.model.gameboard.GamePhase
import it.unibo.view.components.game.gameboard.hand.CardHighlightState.Unhighlighted
import it.unibo.view.components.{ICanBeDisabled, ICanToggleHandler}
import it.unibo.view.logger
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, Text}
import javafx.scene.input.MouseEvent
import javafx.animation.PauseTransition
import javafx.event.{EventHandler, EventType}
import javafx.scene.Node
import javafx.util.Duration
import scalafx.scene.layout.Pane

import scala.compiletime.uninitialized

final case class GridSquare(
    row: Int,
    col: Int,
    size: Double,
    onHover: (Int, Int, HoverDirection) => Unit,
    onClick: (Int, Int) => Unit,
) extends ICanBeDisabled:

  private val hoverDelayMillis = 5
  private var squareColor: Color = Color.White
  private val onMouseMovedFun: EventHandler[MouseEvent] =
    (event: MouseEvent) => handleMouseMoved(event)
  private val onMouseExitedFun: EventHandler[MouseEvent] = (_: MouseEvent) => cancelHoverDelay()
  private val onMouseClickedFun: EventHandler[MouseEvent] =
    (_: MouseEvent) => onClick(row, col)

  private val fixedEventHandlers =
    List(MouseEvent.MOUSE_MOVED -> onMouseMovedFun, MouseEvent.MOUSE_EXITED -> onMouseExitedFun,
      MouseEvent.MOUSE_CLICKED -> onMouseClickedFun)

  override def enableView(): Unit =
    rectangle.setOpacity(0.9)
    fixedEventHandlers.foreach((ev, h) => pane.addEventHandler(ev, h))

  override def disableView(): Unit =
    rectangle.setOpacity(0.7)
    fixedEventHandlers.foreach((ev, h) => pane.removeEventHandler(ev, h))

  private val rectangle: Rectangle = new Rectangle:
    width = size
    height = size
    stroke = Color.Black
    fill = squareColor

  private val text: Text = new Text(s"$row, $col")
  text.setFill(Color.Black)
  text.setFont(new Font(size / 4)) // Adjust font size to fit within the rectangle
  text.setX(size / 2 - text.getLayoutBounds.getWidth / 2)
  text.setY(size / 2 + text.getLayoutBounds.getHeight / 4)

  private val pane: Pane = new Pane
  pane.getChildren.addAll(rectangle, text)

  private val initialDelay = new PauseTransition(Duration.millis(hoverDelayMillis))
  private val hoverDelay = new PauseTransition(Duration.millis(hoverDelayMillis))
  hoverDelay.setOnFinished(_ => triggerHover())

  private var lastEvent: MouseEvent = uninitialized

  initialDelay.setOnFinished(_ => hoverDelay.playFromStart())

  private def handleMouseMoved(event: MouseEvent): Unit =
    lastEvent = event
    initialDelay.playFromStart()
    hoverDelay.stop()

  private def cancelHoverDelay(): Unit =
    initialDelay.stop()
    hoverDelay.stop()

  private def triggerHover(): Unit =
    val direction = HoverDirection
      .fromCoordinates(lastEvent.getX, lastEvent.getY, rectangle.getWidth, rectangle.getHeight)
    onHover(row, col, direction)

  def getGraphicPane: Pane = pane

  def updateColor(color: Color): Unit =
    squareColor = color
    rectangle.setFill(color)

  def getColor: Color = squareColor

  override protected def getPane: Node = pane
