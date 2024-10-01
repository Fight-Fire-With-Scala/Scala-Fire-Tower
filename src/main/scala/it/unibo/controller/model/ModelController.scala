package it.unibo.controller.model

import it.unibo.controller.subscriber.BotSubscriber
import it.unibo.controller.{BotMessage, ModelSubject, RefreshMessage}
import it.unibo.controller.view.RefreshType
import it.unibo.model.ModelModule.Model
import it.unibo.model.effect.core.GameEffect
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.{GameBoard, GameBoardConfig}
import it.unibo.model.gameboard.player.PlayerManager
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import monix.execution.Scheduler.Implicits.global

final case class ModelController(model: Model, modelObserver: ModelSubject) extends PlayerManager:

  def applyEffect(ef: GameEffect, refreshType: RefreshType): Unit =
    val newGb = model.getGameBoard.solveEffect(ef)
    model.setGameBoard(newGb)
    modelObserver.onNext(RefreshMessage(newGb, refreshType))

  def initializeGameBoard(settings: GameBoardConfig): GameBoard =
    val initialGameBoard = GameBoard(settings.getPlayerOne, settings.getPlayerTwo)

    // Initialize both players
    val (updatedGameBoard, updatedPlayer1) =
      initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)

    val (finalGameBoard, updatedPlayer2) = settings.gameMode match
      case GameBoardConfig.GameMode.HumanVsHuman =>
        initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
      case GameBoardConfig.GameMode.HumanVsBot =>
        val botObservable = ConcurrentSubject[BotMessage](MulticastStrategy.replay)
        val botSubscriber = BotSubscriber(this)
        botObservable.subscribe(botSubscriber)
        initializeBot(updatedGameBoard, updatedGameBoard.getOpponent, botObservable)

    // Create the fully initialized game board
    val completeGameBoard = finalGameBoard.copy(player1 = updatedPlayer1, player2 = updatedPlayer2)

    // Update the game board in the model and notify observers
    completeGameBoard.solveEffect(PhaseEffect(completeGameBoard.gamePhase))
