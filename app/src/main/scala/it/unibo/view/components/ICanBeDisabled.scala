package it.unibo.view.components

import javafx.event.{EventHandler, EventType}
import javafx.scene.Node
import javafx.scene.input.MouseEvent

trait ICanBeDisabled:

  def toggleActivation(
      node: Node,
      graphicChangeDisabled: () => Unit,
      graphicChangeEnabled: () => Unit,
      enabled: Boolean,
      eventHandlers: (EventType[MouseEvent], EventHandler[MouseEvent])*
  ): Unit =
    if !enabled then
      eventHandlers.foreach { case (eventType, handler) =>
        graphicChangeDisabled()
        node.removeEventHandler(eventType, handler)
      }
    else
      eventHandlers.foreach { case (eventType, handler) =>
        graphicChangeEnabled()
        node.addEventHandler(eventType, handler)
      }
