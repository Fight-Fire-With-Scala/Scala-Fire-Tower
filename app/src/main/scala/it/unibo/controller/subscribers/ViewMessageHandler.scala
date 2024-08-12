package it.unibo.controller.subscribers

import it.unibo.view.ViewModule.View
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{ModelMessage, StartGameBoardMessage, ShowAvailablePatterns}

import scala.concurrent.Future
import it.unibo.model.logger

/** This class is subscribed to the Model updates and changes the View accordingly */
class ViewMessageHandler(view: View) extends Subscriber[ModelMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ModelMessage): Future[Ack] =
    msg match
      case StartGameBoardMessage(gameBoard) =>
        logger.info(s"Received StartGameBoardMessage")
        view.startGame()
        view.refresh(gameBoard.board.grid)
      case ShowAvailablePatterns(patterns) =>
        logger.info(s"Received ShowAvailablePatterns")
        logger.info(s"patterns $patterns")
        //Enable the hovering of the grid considering the available patterns
        //view.setAvailablePatterns(patterns)

    Continue

  override def onError(ex: Throwable): Unit = println(s"Received error $ex")

  override def onComplete(): Unit = println(s"Received final event")
