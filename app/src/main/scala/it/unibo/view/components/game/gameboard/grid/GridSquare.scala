package it.unibo.view.components.game.gameboard.grid

import it.unibo.view.components.ICanBeDisabled
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, Text}
import javafx.scene.input.MouseEvent
import javafx.animation.PauseTransition
import javafx.event.EventHandler
import javafx.util.Duration
import scalafx.scene.layout.Pane

import scala.compiletime.uninitialized

final case class GridSquare(
    row: Int,
    col: Int,
    size: Double,
    onHover: (Int, Int, HoverDirection) => Unit,
    onClick: () => Unit
) extends ICanBeDisabled:

  private val hoverDelayMillis = 2
  private var squareColor: Color = Color.White
  private val onMouseMovedFun: EventHandler[MouseEvent] = (event: MouseEvent) => handleMouseMoved(event)
  private val onMouseExitedFun: EventHandler[MouseEvent] = (_: MouseEvent) => cancelHoverDelay()

  private val rectangle: Rectangle = new Rectangle:
    width = size
    height = size
    stroke = Color.Black
    fill = squareColor
    //styleClass = Seq("disabled")
    //onMouseMoved = onMouseMovedFun
    //onMouseExited = (_: MouseEvent) => cancelHoverDelay()
    //onMouseClicked = (_: MouseEvent) => handleMouseClicked()

  rectangle.getStyleClass.add("disabled")
  def toggleActivation(): Unit =
    super.toggleActivation(rectangle, onMouseMovedFun, MouseEvent.MOUSE_MOVED)
    super.toggleActivation(rectangle, onMouseExitedFun, MouseEvent.MOUSE_EXITED)

  private val text: Text = new Text(s"$row, $col")
  text.setFill(Color.BLACK)
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

  private def handleMouseClicked(): Unit =
    onClick()
  
  def getGraphicPane: Pane = pane

  def updateColor(color: Color): Unit =
    squareColor = color
    rectangle.setFill(color)

  def getColor: Color = squareColor