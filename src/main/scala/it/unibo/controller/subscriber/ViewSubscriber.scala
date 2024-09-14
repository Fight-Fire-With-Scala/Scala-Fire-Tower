package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.BotMessage
import it.unibo.controller.ChoseCardToPlay
import it.unibo.controller.ConfirmCardPlayMessage
import it.unibo.controller.DiscardCardMessage
import it.unibo.controller.DrawCardMessage
import it.unibo.controller.GameBoardInitialization
import it.unibo.controller.ResolveCardReset
import it.unibo.controller.ResolvePatternChoice
import it.unibo.controller.StartGameMessage
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.UpdateWindDirection
import it.unibo.controller.ViewMessage
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType
import it.unibo.controller.view.RefreshType.CardDeselected
import it.unibo.controller.view.RefreshType.CardDiscard
import it.unibo.controller.view.RefreshType.CardDraw
import it.unibo.controller.view.RefreshType.CardSelected
import it.unibo.controller.view.RefreshType.PatternChosen
import it.unibo.controller.view.RefreshType.PhaseUpdate
import it.unibo.controller.view.RefreshType.WindUpdate
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
import it.unibo.model.gameboard.player.Bot
import monix.reactive.subjects.PublishSubject

/** This class is subscribed to the View updates and changes the Model accordingly */
final class ViewSubscriber(controller: ModelController) extends BaseSubscriber[ViewMessage]:

  given Conversion[Model, GameBoard] = _.getGameBoard

  override val logger: Logger = Logger("View -> ViewSubscriber")

  override def onMessageReceived(msg: ViewMessage): Unit = msg match
    case UpdateGamePhase(ef: PhaseEffect) =>
      logger.info(s"[PHASE] ${ef.newPhase}")
      val gb = controller.model.getGameBoard
      controller.applyEffect(ef, PhaseUpdate)
      gb.getCurrentPlayer match
        case b: Bot => b.think(controller)
        case _      =>

    case UpdateWindDirection(ef: WindUpdateEffect) => controller.applyEffect(ef, WindUpdate)

    case ChoseCardToPlay(ef: PlayCard) => controller.applyEffect(ef, CardSelected)

    case ResolvePatternChoice(ef: PatternApplication) =>
      controller.applyEffect(ef, RefreshType.PatternChosen)
      controller.applyEffect(ResetPatternComputation, CardDeselected)
      controller.modelObserver.onNext(ConfirmCardPlayMessage())

    case ResolveCardReset() => controller.applyEffect(ResetPatternComputation, CardDeselected)

    case DrawCardMessage(ef: DrawCard) => controller.applyEffect(ef, CardDraw)

    case DiscardCardMessage(ef: DiscardCard) => controller.applyEffect(ef, CardDiscard)

    case GameBoardInitialization(settings) =>
      val initialGameBoard = GameBoard(settings.getPlayerOne, settings.getPlayerTwo)

      // Initialize both players
      val (updatedGameBoard, updatedPlayer1) = controller
        .initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)

      val (finalGameBoard, updatedPlayer2) = settings.gameMode match
        case GameBoardConfig.GameMode.HumanVsHuman => controller
            .initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
        case GameBoardConfig.GameMode.HumanVsBot   =>
          val botObservable = PublishSubject[BotMessage]()
          val botSubscriber = BotSubscriber(controller)
          botObservable.subscribe(botSubscriber)
          controller.initializeBot(updatedGameBoard, updatedGameBoard.getOpponent, botObservable)

      // Create the fully initialized game board
      val completeGameBoard = finalGameBoard
        .copy(player1 = updatedPlayer1, player2 = updatedPlayer2)

      // Update the game board in the model and notify observers
      val resolvedGameBoard = completeGameBoard
        .resolveEffect(PhaseEffect(completeGameBoard.gamePhase))
      controller.model.setGameBoard(resolvedGameBoard)
      controller.modelObserver.onNext(StartGameMessage(resolvedGameBoard))
