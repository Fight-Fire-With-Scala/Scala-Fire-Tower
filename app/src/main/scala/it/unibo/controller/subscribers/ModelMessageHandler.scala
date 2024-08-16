package it.unibo.controller.subscribers

import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{
  DrawCardMessage,
  ResolveWindPhase,
  SettingsMessage,
  ShowAvailablePatterns,
  ViewMessage
}
import it.unibo.model.ModelModule.Model
import it.unibo.model.logger
import it.unibo.model.cards.resolvers.PatternComputationResolver

import scala.concurrent.Future
import it.unibo.model.prolog.Rule
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.cards.effects.PatternComputationEffect
import it.unibo.model.players.drawCardFromDeck

/** This class is subscribed to the View updates and changes the Model accordingly */
class ModelMessageHandler(model: Model) extends Subscriber[ViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ViewMessage): Future[Ack] =
    msg match
      case SettingsMessage(settings)    =>
        logger.info(s"Received Settings Message")
        model.initialiseModel(settings)
      case DrawCardMessage(nCards: Int) =>
        logger.info(s"Draw $nCards cards")
        val gameBoard = model.getGameBoard
        val deck = gameBoard.deck
        val player = gameBoard.currentPlayer

        val (finalDeck, finalPlayer) = (1 to nCards).foldLeft((deck, player)) {
          case ((currentDeck, currentPlayer), _) =>
            val (card, newDeck) = currentDeck.drawCard()
            val newPlayer = currentPlayer.drawCardFromDeck(card)
            (newDeck, newPlayer)
        }
        model.setGameBoard(gameBoard.copy(deck = finalDeck, currentPlayer = finalPlayer))

      case ResolveWindPhase() =>
        logger.info(s"Received Settings Message")
        val board = model.getGameBoard.board
        val direction = board.windDirection

        val availablePatterns = PatternComputationResolver(
          VerySmallEffect(Map("a" -> Fire)),
          Rule(Struct.of("fire", Var.of("R"))),
          List(direction)
        ).getAvailableMoves(board).patterns

        model.getObservable.onNext(ShowAvailablePatterns(availablePatterns))

    Continue

  override def onError(ex: Throwable): Unit =
    println(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      println(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    println(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
