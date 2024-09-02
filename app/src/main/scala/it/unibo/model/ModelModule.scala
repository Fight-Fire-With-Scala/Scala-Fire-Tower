package it.unibo.model

import it.unibo.model.gameboard.GameBoard

object ModelModule:
  trait Model:
    def setGameBoard(gb: GameBoard): Unit
    def getGameBoard: GameBoard

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var gameBoard: GameBoard = GameBoard()

      override def getGameBoard: GameBoard = gameBoard
      override def setGameBoard(gb: GameBoard): Unit = gameBoard = gb

  trait Interface extends Provider with Component
