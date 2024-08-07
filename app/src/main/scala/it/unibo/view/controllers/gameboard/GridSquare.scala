package it.unibo.view.controllers.gameboard

import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import javafx.scene.input.MouseEvent
import javafx.animation.PauseTransition
import javafx.util.Duration

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

  def getGraphicRectangle: Rectangle = rectangle

  def updateColor(color: Color): Unit = rectangle.setFill(color)
