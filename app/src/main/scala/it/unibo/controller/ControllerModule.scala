package it.unibo.controller

import it.unibo.controller.subscribers.ViewMessageHandler
import it.unibo.controller.subscribers.ModelMessageHandler
import it.unibo.model.ModelModule
import it.unibo.view.ViewModule

object ControllerModule:

  trait Controller:
    def notifyStartGame(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider & ViewModule.Provider

  trait Component:
    context: Requirements =>

    class ControllerImpl extends Controller:
      def notifyStartGame(): Unit =
        context.model.getObservable.subscribe(new ViewMessageHandler(context.view))
        context.view.getObservable.subscribe(new ModelMessageHandler(context.model, new GameController))

  trait Interface extends Provider with Component:
    self: Requirements =>
