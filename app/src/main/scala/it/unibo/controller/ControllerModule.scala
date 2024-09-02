package it.unibo.controller

import it.unibo.controller.model.ModelController
import it.unibo.controller.subscribers.ModelSubscriber
import it.unibo.controller.subscribers.ViewSubscriber
import it.unibo.model.ModelModule
import it.unibo.view.ViewModule
import monix.reactive.subjects.PublishSubject

object ControllerModule:

  trait Controller:
    def notifyStartGame(): Unit

  trait Provider:
    val controller: Controller

  private type Requirements = ModelModule.Provider & ViewModule.Provider

  trait Component:
    context: Requirements =>

    class ControllerImpl extends Controller:
      private val modelObserver = PublishSubject[ModelMessage]()
      
      def notifyStartGame(): Unit =
        modelObserver.subscribe(new ModelSubscriber(context.view))
        context.view.getObservable
          .subscribe(new ViewSubscriber(context.model, modelObserver, ModelController()))
        context.view.startMenu()

  trait Interface extends Provider with Component:
    self: Requirements =>
