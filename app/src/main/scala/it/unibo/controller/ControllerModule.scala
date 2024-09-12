package it.unibo.controller

import it.unibo.controller.model.ModelController
import it.unibo.controller.subscriber.ModelSubscriber
import it.unibo.controller.subscriber.ViewSubscriber
import it.unibo.controller.view.ViewController
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
      private val modelObservable = PublishSubject[ModelMessage]()
      private val viewObservable = PublishSubject[ViewMessage]()
      private val intObservable = PublishSubject[InternalViewMessage]()

      private lazy val modelController = ModelController(context.model, modelObservable)
      private lazy val viewController = ViewController(context.view, intObservable, viewObservable)

      private lazy val modelSubscriber = ModelSubscriber(viewController)
      private lazy val viewSubscriber = ViewSubscriber(modelController)

      override def notifyStartGame(): Unit =
        modelObservable.subscribe(modelSubscriber)
        viewObservable.subscribe(viewSubscriber)
        viewController.startMenu()

  trait Interface extends Provider with Component:
    self: Requirements =>
