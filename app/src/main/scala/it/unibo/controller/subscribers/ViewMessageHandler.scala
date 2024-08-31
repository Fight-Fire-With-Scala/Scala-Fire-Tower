package it.unibo.controller.subscribers

import it.unibo.view.ViewModule.View
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{
  ModelMessage,
  RefreshMessage,
  ShowAvailablePatterns,
  StartGameBoardMessage,
  ChangeTurnPhase
}

import scala.concurrent.Future
import it.unibo.controller.logger
import it.unibo.controller.subscribers.SubscriberUtils.{onCompleteHandler, onErrorHandler}

/** This class is subscribed to the Model updates and changes the View accordingly */
class ViewMessageHandler(view: View) extends Subscriber[ModelMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ModelMessage): Future[Ack] =
    msg match
      case StartGameBoardMessage(gameBoard) =>
        logger.info(s"Received StartGameBoardMessage")
        view.startGame(gameBoard)
        
      case ShowAvailablePatterns(patterns)  =>
        logger.info(s"Received ShowAvailablePatterns")
        logger.info(s"patterns $patterns")
        view.setAvailablePatterns(patterns)

      case ChangeTurnPhase(gamePhase) =>
        logger.info(s"Received ChangeTurnPhase")
        view.setTurnPhase(gamePhase.toString)
        
      case RefreshMessage(gameBoard)        =>
        logger.info(s"Received RefreshMessage")
        view.refresh(gameBoard)
    Continue

  override def onError(ex: Throwable): Unit = onErrorHandler(ex)
  override def onComplete(): Unit = onCompleteHandler()
