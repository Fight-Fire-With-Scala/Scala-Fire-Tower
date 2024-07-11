package it.unibo.controller

import it.unibo.model.ModelModule
import it.unibo.view.ViewModule

object ControllerModule:

  trait Controller:
    def notifyStart(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider & ViewModule.Provider

  trait Component:
    context: Requirements =>

    class ControllerImpl extends Controller:

      def notifyStart(): Unit =
        // context.model.init()
        context.view.show()

  trait Interface extends Provider with Component:
    self: Requirements =>
