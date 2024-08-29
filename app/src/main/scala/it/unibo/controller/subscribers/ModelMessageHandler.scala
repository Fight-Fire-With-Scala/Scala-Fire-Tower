package it.unibo.controller.subscribers

import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{
  DiscardTheseCardsMessage,
  DrawCardMessage,
  EndWindPhase,
  GameController,
  ResolvePatternChoice,
  SettingsMessage,
  SetupWindPhase,
  ShowAvailablePatterns,
  UpdateWindDirection,
  ViewMessage
}
import it.unibo.model.ModelModule.Model
import it.unibo.controller.logger
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.GamePhase.{ActionPhase, WindPhase}

import scala.concurrent.Future
import it.unibo.model.prolog.Rule
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import alice.tuprolog.{Struct, Var}
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.gameboard.ActionPhaseChoice.PlayCard
import it.unibo.model.gameboard.Direction

/** This class is subscribed to the View updates and changes the Model accordingly */
class ModelMessageHandler(model: Model, controller: GameController) extends Subscriber[ViewMessage]:
  override def scheduler: Scheduler = Scheduler.global

  override def onNext(msg: ViewMessage): Future[Ack] =
    msg match
      case SettingsMessage(settings) =>
        logger.info(s"Received Settings Message")
        model.initialiseModel(settings)

      case DrawCardMessage(nCards: Int) =>
        logger.info(s"Draw $nCards cards")
        model.drawCards(nCards)

      case SetupWindPhase() =>
        logger.info(s"Received ResolveWindPhase Message")
        controller.handleWindPhase(model)

      case EndWindPhase() =>
        logger.info(s"Received EndWindPhase Message")
        val gameBoard = model.getGameBoard
        model.setGameBoard(gameBoard.copy(gamePhase = ActionPhase))

      case UpdateWindDirection(windDirection: Direction) =>
        logger.info(s"Received UpdateWindDirection Message")
        val gameBoard = model.getGameBoard
        val board = gameBoard.board
        model.setGameBoard(gameBoard.copy(board = board.copy(windDirection = windDirection)))
        controller.handleWindPhase(model)

      case ResolvePatternComputation(cardId: Int) =>
        logger.info(s"Received ResolvePatternComputation Message")
        controller.handleActionPhase(cardId, model, ActionPhaseChoice.PlayCard)

      case ResolvePatternChoice(pattern) =>
        logger.info(s"Received ResolvePatternChoice Message")
        val gameBoard = model.getGameBoard
        val board = gameBoard.board
        model.setGameBoard(
          gameBoard.copy(board = board.copy(grid = board.grid.setTokens(pattern.toSeq*)))
        )

      case DiscardTheseCardsMessage(cards) =>
        logger.info(s"Received DiscardTheseCardsMessage with cards: $cards")
        model.discardCards(cards)

      case it.unibo.controller.UpdateWindDirection(_) => ???
      case it.unibo.controller.ResolvePatternComputation() => ???
    Continue

  override def onError(ex: Throwable): Unit =
    logger.error(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      logger.error(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    logger.error(s"Full description: ${ex.toString}")

  override def onComplete(): Unit = println(s"Received final event")
