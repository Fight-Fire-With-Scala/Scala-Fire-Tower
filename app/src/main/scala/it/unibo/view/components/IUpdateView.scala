package it.unibo.view.components

import scalafx.application.Platform

trait IUpdateView:
  def runOnUIThread(action: => Unit): Unit =
    Platform.runLater { () => action }
