package it.unibo.view.components

import javafx.event.{EventHandler, EventType}
import javafx.scene.Node
import javafx.scene.input.MouseEvent

trait ICanBeDisabled:
  protected def toggleActivation(
      node: Node,
      eventHandler: EventHandler[? >: MouseEvent],
      mouseEvent: EventType[MouseEvent]
  ): Unit =
    if (!node.getStyleClass.contains("disabled"))
      node.getStyleClass.add("disabled")
      node.removeEventHandler(mouseEvent, eventHandler)
    else
      node.getStyleClass.remove("disabled")
      node.setOnMouseClicked(eventHandler)
