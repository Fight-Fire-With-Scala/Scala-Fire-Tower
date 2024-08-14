package it.unibo.model

import monix.reactive.subjects.PublishSubject
import it.unibo.controller.{ModelMessage, ModelSubject, RefreshMessage, StartGameBoardMessage}
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.players.Player
import it.unibo.model.settings.Settings

object ModelModule:
  trait Model:
    def initialiseModel(settings: Settings): Unit
    def getObservable: ModelSubject
    def getGameBoard: GameBoard
    def setGameBoard(newGameBoard: GameBoard): Unit

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var gameBoard: GameBoard = _
      private val observerSubject = PublishSubject[ModelMessage]()

      def getObservable: ModelSubject = observerSubject
      override def getGameBoard: GameBoard = gameBoard
      override def setGameBoard(newGameBoard: GameBoard): Unit =
        gameBoard = newGameBoard
        observerSubject.onNext(RefreshMessage(gameBoard))
      def initialiseModel(settings: Settings): Unit =
        val playerOne = settings.getPlayerOne
        val playerTwo = settings.getPlayerTwo
        gameBoard = GameBoard(playerOne, playerTwo)
        observerSubject.onNext(StartGameBoardMessage(gameBoard))

  trait Interface extends Provider with Component
