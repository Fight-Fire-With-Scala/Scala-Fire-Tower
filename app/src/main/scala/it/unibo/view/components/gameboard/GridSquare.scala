package it.unibo.view.components.gameboard

import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, Text}
import javafx.scene.input.MouseEvent
import javafx.animation.PauseTransition
import javafx.util.Duration
import scalafx.scene.layout.Pane

import scala.compiletime.uninitialized

case class GridSquare(
    row: Int,
    col: Int,
    size: Double,
    onHover: (Int, Int, HoverDirection) => Unit
):
  private val hoverDelayMillis = 250

  private val rectangle: Rectangle = new Rectangle:
    width = size
    height = size
    stroke = Color.Black
    onMouseMoved = (event: MouseEvent) => handleMouseMoved(event)
    onMouseExited = (_: MouseEvent) => cancelHoverDelay()

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

  def getGraphicPane: Pane = pane

  def updateColor(color: Color): Unit = rectangle.setFill(color)