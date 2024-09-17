package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.{ BotMessage, ChoseCardToPlay, ConfirmCardPlayMessage, DiscardCardMessage, DrawCardMessage, GameBoardInitialization, RefreshMessage, ResolveCardReset, ResolvePatternChoice, StartGameMessage, UpdateGamePhase, UpdateWindDirection, ViewMessage }
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType
import it.unibo.controller.view.RefreshType.{ CardDeselected, CardDiscard, CardDraw, CardSelected, EndGameUpdate, PatternChosen, PhaseUpdate, WindUpdate }
import it.unibo.model.ModelModule.Model
import it.unibo.model.effect.card.WindUpdateEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.hand.HandEffect.DiscardCard
import it.unibo.model.effect.hand.HandEffect.DrawCard
import it.unibo.model.effect.hand.HandEffect.PlayCard
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.PatternApplication
import it.unibo.model.effect.pattern.PatternEffect.ResetPatternComputation
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig
import it.unibo.model.gameboard.GameBoardConfig.GameMode.HumanVsBot
import it.unibo.model.gameboard.GameBoardConfig.GameMode.HumanVsHuman
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.model.gameboard.GamePhase.{ DecisionPhase, EndGamePhase, PlaySpecialCardPhase, PlayStandardCardPhase, WaitingPhase, WindPhase }
import it.unibo.model.gameboard.player.Bot
import monix.reactive.subjects.PublishSubject

/** This class is subscribed to the View updates and changes the Model accordingly */
final class ViewSubscriber(controller: ModelController) extends BaseSubscriber[ViewMessage]:

  given Conversion[Model, GameBoard] = _.getGameBoard
  given Model                        = controller.model

  override val logger: Logger = Logger("View -> ViewSubscriber")

  override def onMessageReceived(msg: ViewMessage): Unit = msg match
    case UpdateGamePhase(ef: PhaseEffect) =>
      logger.info(s"[PHASE] ${ef.newPhase}")
      val gb = controller.model.getGameBoard
      controller.applyEffect(ef, PhaseUpdate)
      gb.getCurrentPlayer match
        case b: Bot => b.think
        case _      =>

    case UpdateWindDirection(ef: WindUpdateEffect) => controller.applyEffect(ef, WindUpdate)

    case ChoseCardToPlay(ef: PlayCard) => controller.applyEffect(ef, CardSelected)

    case ResolvePatternChoice(ef: PatternApplication) =>
      controller.applyEffect(ef, RefreshType.PatternChosen)
      controller.modelObserver.onNext(RefreshMessage(controller.model.getGameBoard, CardDeselected))
//      controller.applyEffect(ResetPatternComputation, CardDeselected)
      controller.modelObserver.onNext(ConfirmCardPlayMessage())
      controller.model.getGameBoard.isGameEnded match
        case Some(_) =>
          controller.applyEffect(PhaseEffect(EndGamePhase), PhaseUpdate)
          controller.modelObserver.onNext(
            RefreshMessage(controller.model.getGameBoard, EndGameUpdate)
          )
        case None =>
          controller.model.getGameBoard.gamePhase match
            case WindPhase =>
              controller.applyEffect(PhaseEffect(WaitingPhase), PhaseUpdate)
            case PlayStandardCardPhase | PlaySpecialCardPhase =>
              controller.applyEffect(PhaseEffect(DecisionPhase), PhaseUpdate)
            case _ =>

    case ResolveCardReset() => controller.applyEffect(ResetPatternComputation, CardDeselected)

    case DrawCardMessage(ef: DrawCard) => controller.applyEffect(ef, CardDraw)

    case DiscardCardMessage(ef: DiscardCard) => controller.applyEffect(ef, CardDiscard)

    case GameBoardInitialization(settings) =>
      val initialGameBoard = GameBoard(settings.getPlayerOne, settings.getPlayerTwo)

      // Initialize both players
      val (updatedGameBoard, updatedPlayer1) = controller
        .initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)

      val (finalGameBoard, updatedPlayer2) = settings.gameMode match
        case GameBoardConfig.GameMode.HumanVsHuman =>
          controller
            .initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
        case GameBoardConfig.GameMode.HumanVsBot =>
          val botObservable = PublishSubject[BotMessage]()
          val botSubscriber = BotSubscriber(controller)
          botObservable.subscribe(botSubscriber)
          controller.initializeBot(updatedGameBoard, updatedGameBoard.getOpponent, botObservable)

      // Create the fully initialized game board
      val completeGameBoard = finalGameBoard
        .copy(player1 = updatedPlayer1, player2 = updatedPlayer2)

      // Update the game board in the model and notify observers
      val solvedGameBoard = completeGameBoard
        .solveEffect(PhaseEffect(completeGameBoard.gamePhase))
      controller.model.setGameBoard(solvedGameBoard)
      controller.modelObserver.onNext(StartGameMessage(solvedGameBoard))
