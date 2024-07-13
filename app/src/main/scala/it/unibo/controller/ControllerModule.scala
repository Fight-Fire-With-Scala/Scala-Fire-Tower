package it.unibo.controller

import it.unibo.model.ModelModule
import it.unibo.view.ViewModule

trait StartGameNotifier{
    def notifyStartGame(): Unit
}

object ControllerModule:
  given StartGameNotifier with
    def notifyStartGame(): Unit = println("Starting game...")

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
