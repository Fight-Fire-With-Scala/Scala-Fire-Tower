package it.unibo.subscribers

import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.ViewModule.View
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber

import scala.concurrent.Future

class ModelSubscriber(view: View) extends Subscriber[Grid] {
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(grid: Grid): Future[Ack] = {
    view.updateGrid(grid) // Example update
    Continue
  }

  override def onError(ex: Throwable): Unit = {
    println(s"Received error $ex")
  }

  override def onComplete(): Unit = {
    println(s"Received final event")
  }
}