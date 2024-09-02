package it.unibo.view.components

import monix.eval.Task
import scalafx.application.Platform

trait IUpdateView:
  def runOnUIThread(action: => Unit): Unit =
    Platform.runLater { () => action }

  def runTaskOnUIThread[A](task: => A): Task[A] = {
    Task.async { callback =>
      Platform.runLater(() => {
        try {
          val result = task
          callback.onSuccess(result)
        } catch {
          case ex: Throwable => callback.onError(ex)
        }
      })
    }
  }
