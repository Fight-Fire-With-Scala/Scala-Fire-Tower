package it.unibo.view.components

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

trait ICanBeDisabled:
  def toggleActivation(pane: Pane, eventHandler: EventHandler[MouseEvent]): Unit =
    if (pane.getStyleClass.contains("enabled"))
      pane.setOnMouseEntered(event => pane.getStyleClass.remove("enabled"))
      pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler)
    else
      pane.setOnMouseEntered(event => pane.getStyleClass.add("enabled"))
      pane.setOnMouseClicked(eventHandler)
