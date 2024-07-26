package it.unibo.controller

import it.unibo.model.ModelModule
import it.unibo.view.ViewModule

object ControllerModule:

  trait Controller:
    def notifyStartMenu(): Unit
    def notifyStartGameSession(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider & ViewModule.Provider

  trait Component:
    context: Requirements =>

    class ControllerImpl extends Controller:

      def notifyStartMenu(): Unit =
        // context.model.init()
        context.view.show()

      def notifyStartGameSession(): Unit = print("Starting new game...")

  trait Interface extends Provider with Component:
    self: Requirements =>
