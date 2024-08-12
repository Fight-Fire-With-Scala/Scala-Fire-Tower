package it.unibo.controller.subscribers

import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{DrawCardMessage, SettingsMessage, ViewMessage}
import it.unibo.model.ModelModule.Model
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.players.Player

import scala.concurrent.Future

/** This class is subscribed to the View updates and changes the Model accordingly */
class ModelMessageHandler(model: Model) extends Subscriber[ViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ViewMessage): Future[Ack] =
    msg match
      case SettingsMessage(_)           => model.initialiseModel()
      case DrawCardMessage(nCards: Int) => println(s"Draw $nCards cards")
//        val gameBoard = model.getGameBoard
//        val deck = gameBoard.deck.drawCard(nCards)
//        val player = gameBoard.currentPlayer
//        player.
//        model.setGameBoard(gameBoard.copy(deck = deck))

    println(s"View updated Model with value: $msg")
    // update del model
    Continue

  override def onError(ex: Throwable): Unit = println(s"Received error $ex")

  override def onComplete(): Unit = println(s"Received final event")
