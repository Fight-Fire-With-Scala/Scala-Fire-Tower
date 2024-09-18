package it.unibo.view.component

import it.unibo.view.logger
import javafx.scene.Node

trait ICanBeDisabled:
  protected var enabled: Boolean = false

  def enableView(): Unit = if !enabled then
    logger.debug(s"[Activation] Enabled ${this.getClass.getSimpleName}")
    enabled = true
    onEnableView()

  def disableView(): Unit = if enabled then
    logger.debug(s"[Activation] Disabled ${this.getClass.getSimpleName}")
    enabled = false
    onDisableView()

  protected def onEnableView(): Unit =
    getPane.getStyleClass.remove("disabled")
    getPane.getStyleClass.add("enabled")

  protected def onDisableView(): Unit =
    getPane.getStyleClass.add("disabled")
    getPane.getStyleClass.remove("enabled")

  protected def getPane: Node
