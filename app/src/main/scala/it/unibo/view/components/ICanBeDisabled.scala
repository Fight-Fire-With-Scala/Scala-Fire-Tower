package it.unibo.view.components

import javafx.event.{EventHandler, EventType}
import javafx.scene.Node
import javafx.scene.input.MouseEvent

trait ICanBeDisabled:
  private var enabled: Boolean = false

  protected def toggleActivation(
      node: Node,
      eventHandlers: List[(EventType[MouseEvent], EventHandler[MouseEvent])],
      graphicChangeDisabled: () => Unit,
      graphicChangeEnabled: () => Unit
  ): Unit =
    if enabled then
      eventHandlers.foreach { case (eventType, handler) =>
        graphicChangeDisabled()
        node.removeEventHandler(eventType, handler)
      }
    else
      eventHandlers.foreach { case (eventType, handler) =>
        graphicChangeEnabled()
        node.addEventHandler(eventType, handler)
      }
    enabled = !enabled