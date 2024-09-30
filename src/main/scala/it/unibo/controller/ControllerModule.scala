package it.unibo.controller

import it.unibo.controller.model.ModelController
import it.unibo.controller.subscriber.ModelSubscriber
import it.unibo.controller.subscriber.ViewSubscriber
import it.unibo.controller.view.ViewController
import it.unibo.model.ModelModule
import it.unibo.view.ViewModule
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.{ ConcurrentSubject, PublishSubject }
import monix.execution.Scheduler.Implicits.global

object ControllerModule:

  trait Controller:
    def notifyStartGame(): Unit

  trait Provider:
    val controller: Controller

  private type Requirements = ModelModule.Provider & ViewModule.Provider

  trait Component:
    context: Requirements =>

    class ControllerImpl extends Controller:
      private val modelObservable = ConcurrentSubject[ModelMessage](MulticastStrategy.replay)
      private val viewObservable  = ConcurrentSubject[ViewMessage](MulticastStrategy.replay)
      private val intObservable   = ConcurrentSubject[InternalViewMessage](MulticastStrategy.replay)

      override def notifyStartGame(): Unit =
        val modelController = ModelController(context.model, modelObservable)
        val viewController  = ViewController(context.view, intObservable, viewObservable)

        val modelSubscriber = ModelSubscriber(viewController)
        val viewSubscriber  = ViewSubscriber(modelController)

        modelObservable.subscribe(modelSubscriber)
        viewObservable.subscribe(viewSubscriber)
        viewController.startMenu()

  trait Interface extends Provider with Component:
    self: Requirements =>
