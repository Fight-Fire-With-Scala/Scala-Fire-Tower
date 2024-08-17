package it.unibo.view.components

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

trait ICanBeDisabled:
  def toggleActivation(pane: Pane, eventHandler: EventHandler[MouseEvent]): Unit =
    if (!pane.getStyleClass.contains("disabled"))
      pane.setOnMouseEntered(event => pane.getStyleClass.add("disabled"))
      pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler)
    else
      pane.setOnMouseEntered(event => pane.getStyleClass.remove("disabled"))
      pane.setOnMouseClicked(eventHandler)
