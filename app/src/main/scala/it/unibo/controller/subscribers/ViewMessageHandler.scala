package it.unibo.controller.subscribers

import it.unibo.view.ViewModule.View
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{ModelMessage, StartGameBoardMessage}

import scala.concurrent.Future

/**
  This class is subscribed to the Model updates and changes the View accordingly
 **/
class ViewMessageHandler(view: View) extends Subscriber[ModelMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ModelMessage): Future[Ack] =
    msg match
      case StartGameBoardMessage(gameboard) =>
        view.startGame()
        view.refresh(gameboard.board.grid)



    Continue

  override def onError(ex: Throwable): Unit = println(s"Received error $ex")

  override def onComplete(): Unit = println(s"Received final event")
