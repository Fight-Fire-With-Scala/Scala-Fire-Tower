package it.unibo.view.components.game.gameboard.grid

import it.unibo.view.components.game.gameboard.hand.CardHighlightState.Unhighlighted
import it.unibo.view.components.game.gameboard.hand.CardState
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

enum GridState {
  case WindPhase, CardPhase
}

final case class GridSquare(
    row: Int,
    col: Int,
    size: Double,
    onHover: (Int, Int, HoverDirection) => Unit,
    onClick: () => Unit
) extends ICanBeDisabled, ICanToggleHandler[GridState]:


  protected var currentState: GridState = GridState.WindPhase
  protected val defaultState: GridState = GridState.WindPhase
  private var eventHandlers: Map[EventType[MouseEvent], List[EventHandler[MouseEvent]]] = Map()

  
  private val hoverDelayMillis = 5
  private var squareColor: Color = Color.White
  private val onMouseMovedFun: EventHandler[MouseEvent] =
    (event: MouseEvent) => handleMouseMoved(event)
  private val onMouseExitedFun: EventHandler[MouseEvent] = (_: MouseEvent) => cancelHoverDelay()
  private val onMouseClickedFun: EventHandler[MouseEvent] = (_: MouseEvent) => handleMouseClicked()
  private val startEventHandlers = List(
    MouseEvent.MOUSE_MOVED -> onMouseMovedFun,
    MouseEvent.MOUSE_EXITED -> onMouseExitedFun
  )

  addHandler(GridState.WindPhase, MouseEvent.MOUSE_CLICKED, onMouseClickedFun)
  addHandler(GridState.CardPhase, MouseEvent.MOUSE_CLICKED, (_: MouseEvent) => logger.info("Differend handler going for click"))

  protected def applyState(state: GridState): Unit = logger.info("Applying state")

  protected def onToggle(state: GridState): Unit = 
    eventHandlers.get(MouseEvent.MOUSE_CLICKED)
    .foreach(_.foreach(pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, _)))
    eventHandlers += MouseEvent.MOUSE_CLICKED -> getHandlers(state, MouseEvent.MOUSE_CLICKED)
    eventHandlers(MouseEvent.MOUSE_CLICKED)
      .foreach(pane.addEventHandler(MouseEvent.MOUSE_CLICKED, _))

  override def enableView(): Unit =
//    logger.info("Enable view")
    rectangle.setOpacity(0.9)
    startEventHandlers.foreach((ev, h) => pane.addEventHandler(ev, h))
    toggle(currentState)

  override def disableView(): Unit =
//    logger.info("Disable view")
    rectangle.setOpacity(0.7)
    startEventHandlers.foreach((ev, h) => pane.removeEventHandler(ev, h))
    eventHandlers.get(MouseEvent.MOUSE_CLICKED)
      .foreach(_.foreach(pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, _)))
  
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

  private def handleMouseClicked(): Unit = onClick()

  def getGraphicPane: Pane = pane

  def updateColor(color: Color): Unit =
    squareColor = color
    rectangle.setFill(color)

  def getColor: Color = squareColor
  

  override protected def getPane: Node = pane
