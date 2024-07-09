package it.unibo.controller

import it.unibo.model.ModelModule
import monix.execution.Scheduler.Implicits.global

object ControllerModule:

  trait Controller:
    def notifyStart(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider
  
  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      def notifyStart(): Unit =
        context.model.init()

  trait Interface extends Provider with Component:
    self: Requirements =>
