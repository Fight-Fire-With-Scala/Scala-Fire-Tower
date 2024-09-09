package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.model.ModelController
import it.unibo.controller.{ConfirmCardPlayMessage, DiscardCardMessage, DrawCardMessage, GameBoardInitialization, ModelSubject, RefreshMessage, ResetPatternComputation, ResolvePatternChoice, ResolvePatternComputation, UpdateGamePhaseModel, UpdateWindDirection, ViewMessage}
import it.unibo.controller.StartGameMessage
import it.unibo.model.ModelModule.Model
import it.unibo.model.gameboard.player.Bot
import it.unibo.model.gameboard.{Direction, GameBoard, GamePhase}

/** This class is subscribed to the View updates and changes the Model accordingly */
final class ViewSubscriber(model: Model, modelObserver: ModelSubject, controller: ModelController)
    extends BaseSubscriber[ViewMessage]:

  given Conversion[Model, GameBoard] = _.getGameBoard

  override val logger: Logger = Logger("View -> ViewSubscriber")

  override def onMessageReceived(msg: ViewMessage): Unit = msg match
    case GameBoardInitialization(settings) =>
      val playerOne = settings.getPlayerOne
      val playerTwo = settings.getPlayerTwo
      val gameBoard = GameBoard(playerOne, playerTwo)
      val (newGb, newPlayer) = controller.initializePlayer(gameBoard, gameBoard.getCurrentPlayer())
      model.setGameBoard(newGb.copy(player1 = newPlayer))
      modelObserver.onNext(StartGameMessage(newGb))

    case DrawCardMessage(nCards: Int) =>
      val (gb, player) = controller.drawCards(model, nCards)(model.getGameBoard.getCurrentPlayer())
      val newGb = gb.updateCurrentPlayer(player)
      model.setGameBoard(newGb)

    case UpdateGamePhaseModel(choice: GamePhase) =>
      model.setGameBoard(controller.updateGamePhase(model, choice))
      model.getGameBoard.getCurrentPlayer() match
        case bot: Bot => bot.think()
        case _ =>
      modelObserver.onNext(RefreshMessage(model.getGameBoard))

    case UpdateWindDirection(windDirection: Direction) =>
      val gb = model.getGameBoard
      val board = gb.board
      val newGb = gb.copy(board = board.copy(windDirection = windDirection))
      model.setGameBoard(newGb)
      modelObserver.onNext(RefreshMessage(newGb))

    case ResolvePatternComputation(cardId: Int) =>
      val gb = model.getGameBoard
      val currentGb = controller.setCurrentCardId(gb, cardId)
      val newGb = controller.resolvePatternComputation(currentGb, cardId)
      model.setGameBoard(newGb)
      modelObserver.onNext(RefreshMessage(newGb))

    case ResolvePatternChoice(pattern) =>
      val gb = model.getGameBoard
      val newGb = controller.resolvePatternChoice(gb, pattern)
      model.setGameBoard(newGb)
      modelObserver.onNext(ConfirmCardPlayMessage())
      modelObserver.onNext(RefreshMessage(newGb))

    case DiscardCardMessage(cards) =>
      val gb = controller.discardCards(model, cards)
      model.setGameBoard(gb)

    case ResetPatternComputation() =>
      val gb = model.getGameBoard
      val newGb = gb
        .copy(board = gb.board.copy(currentCardId = None, availablePatterns = Set.empty))
      modelObserver.onNext(RefreshMessage(newGb))
