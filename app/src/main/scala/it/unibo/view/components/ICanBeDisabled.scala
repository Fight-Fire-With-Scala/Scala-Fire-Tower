package it.unibo.view.components

import javafx.event.{EventHandler, EventType}
import javafx.scene.Node
import javafx.scene.input.MouseEvent

trait ICanBeDisabled:
  protected var enabled: Boolean = false

  def enableView(): Unit = if !enabled then 
    enabled = true
    onEnableView()

  def disableView(): Unit = if enabled then 
    enabled = false
    onDisableView()

  protected def onEnableView(): Unit =
    getPane.getStyleClass.remove("disabled")
    getPane.getStyleClass.add("enabled")
  
  protected def onDisableView(): Unit =
    getPane.getStyleClass.add("disabled")
    getPane.getStyleClass.remove("enabled")
  
  protected def getPane: Node