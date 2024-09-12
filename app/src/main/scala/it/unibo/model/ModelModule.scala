package it.unibo.model

import it.unibo.model.gameboard.GameBoard
import scala.compiletime.uninitialized

object ModelModule:
  trait Model:
    def setGameBoard(gb: GameBoard): Unit
    def getGameBoard: GameBoard

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var gameBoard: GameBoard = uninitialized

      override def getGameBoard: GameBoard = gameBoard
      override def setGameBoard(gb: GameBoard): Unit = gameBoard = gb

  trait Interface extends Provider with Component
