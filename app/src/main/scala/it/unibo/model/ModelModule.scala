package it.unibo.model

import monix.reactive.subjects.PublishSubject
import it.unibo.controller.{ModelMessage, ModelSubject, StartGameBoardMessage}
import it.unibo.model.gameboard.GameBoard

object ModelModule:
  trait Model:
    def initialiseModel(): Unit
    def getObservable: ModelSubject
    def getGameBoard: GameBoard
    def setGameBoard(newGameBoard: GameBoard): Unit

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var gameBoard: GameBoard = GameBoard()
      private val observerSubject = PublishSubject[ModelMessage]()

      def getObservable: ModelSubject = observerSubject
      override def getGameBoard: GameBoard = gameBoard
      override def setGameBoard(newGameBoard: GameBoard): Unit = gameBoard = newGameBoard
      def initialiseModel(): Unit = observerSubject.onNext(StartGameBoardMessage(gameBoard))

  trait Interface extends Provider with Component
