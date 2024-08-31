package it.unibo.controller.subscribers

import it.unibo.controller.subscribers.SubscriberUtils.{onCompleteHandler, onErrorHandler}
import monix.execution.Ack.Continue
import monix.execution.{Ack, Scheduler}
import monix.reactive.observers.Subscriber
import it.unibo.controller.{DiscardTheseCardsMessage, DrawCardMessage, ResetPatternComputation, ResolvePatternChoice, ResolvePatternComputation, SettingsMessage, SetupWindPhase, ShowAvailablePatterns, UpdateGamePhaseModel, UpdateWindDirection, ViewMessage, logger}
import it.unibo.model.ModelModule.Model
import it.unibo.model.TurnModelController
import it.unibo.model.cards.choices.{StepChoice, WindChoice}
import it.unibo.model.cards.effects.{CardEffect, PatternComputationEffect}
import it.unibo.model.cards.resolvers.PatternApplicationResolver
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.WaitingPhase

import scala.concurrent.Future
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.cards.effects.PatternChoiceEffect
import it.unibo.model.cards.types.{FireCard, FirebreakCard, WaterCard, WindCard}

/** This class is subscribed to the View updates and changes the Model accordingly */
final class ModelMessageHandler(model: Model, controller: TurnModelController)
    extends Subscriber[ViewMessage]:
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
        
      case UpdateGamePhaseModel(choice: GamePhase) =>
        logger.info(s"Received UpdateGamePhaseModel Message")
        controller.updateGamePhase(model, choice)
        
      case UpdateWindDirection(windDirection: Direction) =>
        logger.info(s"Received UpdateWindDirection Message")
        val gameBoard = model.getGameBoard
        val board = gameBoard.board
        model.setGameBoard(gameBoard.copy(board = board.copy(windDirection = windDirection)))
        controller.handleWindPhase(model)

      case ResolvePatternComputation(cardId: Int) =>
        logger.info(s"Received ResolvePatternComputation Message")
        val gameBoard = model.getGameBoard
        val card = gameBoard.currentPlayer.hand.find(_.id == cardId)
        card match
          case Some(c)=>
            c.cardType.effectType match
              case card: FireCard => ???
              case card: FirebreakCard => ???
              case card: WaterCard => ???
              case card: WindCard =>
                val gb = gameBoard.resolveCardPlayed(c, WindChoice.PlaceFire)
                model.getObservable.onNext(ShowAvailablePatterns(gb.board.availablePatterns))
              case _ => ???
          case None    => logger.warn("No card found")

      case ResolvePatternChoice(pattern) =>
        logger.info(s"Received ResolvePatternChoice Message")
        val gameBoard = model.getGameBoard
        val effect = PatternChoiceEffect(pattern)
        val board = gameBoard.board.applyEffect(Some(effect))
        model.setGameBoard(gameBoard.copy(board = board))

      case DiscardTheseCardsMessage(cards) =>
        logger.info(s"Received DiscardTheseCardsMessage with cards: $cards")
        model.discardCards(cards)

      case ResetPatternComputation() => logger.info(s"Received ResetPatternComputation")

    Continue

  override def onError(ex: Throwable): Unit = onErrorHandler(ex)
  override def onComplete(): Unit = onCompleteHandler()
