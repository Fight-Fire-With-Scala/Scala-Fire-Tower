package it.unibo.view.component

import javafx.scene.Node

trait ICanBeDisabled:
  protected var enabled: Boolean = false

  def enableView(): Unit = if !enabled then
//    logger.info(s"[Activation] Enabled ${this.getClass.getName}")
    enabled = true
    onEnableView()

  def disableView(): Unit = if enabled then
//    logger.info(s"[Activation] Disabled ${this.getClass.getName}")
    enabled = false
    onDisableView()

  protected def onEnableView(): Unit =
    getPane.getStyleClass.remove("disabled")
    getPane.getStyleClass.add("enabled")
  
  protected def onDisableView(): Unit =
    getPane.getStyleClass.add("disabled")
    getPane.getStyleClass.remove("enabled")
  
  protected def getPane: Node