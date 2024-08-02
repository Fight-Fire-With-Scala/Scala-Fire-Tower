package it.unibo.controller.subscribers

import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{ViewMessage, SettingsMessage}
import it.unibo.model.ModelModule.Model

import scala.concurrent.Future

/**
 * This class is subscribed to the View updates and changes the Model accordingly
 **/
class ModelMessageHandler(model: Model) extends Subscriber[ViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ViewMessage): Future[Ack] =
    msg match
      case SettingsMessage(_) => model.initialiseModel()

    println(s"View updated Model with value: $msg")
    // update del model
    Continue

  override def onError(ex: Throwable): Unit = println(s"Received error $ex")

  override def onComplete(): Unit = println(s"Received final event")
