package it.unibo.model

import scala.compiletime.uninitialized

import it.unibo.model.gameboard.GameBoard

object ModelModule:
  trait Model:
    def setGameBoard(gb: GameBoard): Unit
    def getGameBoard: GameBoard

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var gameBoard: GameBoard = uninitialized

      override def getGameBoard: GameBoard           = gameBoard
      override def setGameBoard(gb: GameBoard): Unit = gameBoard = gb

  trait Interface extends Provider with Component
