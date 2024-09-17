package it.unibo.model

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.{Person, Player, PlayerManager}

trait GameBoardInitializer extends PlayerManager:
  
  def initialiseGameBoard(player1: Player, player2: Player): GameBoard =
    val initialGameBoard = GameBoard(player1, player2)
    // Initialize both players
    val (updatedGameBoard, updatedPlayer1) = initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)
    val (finalGameBoard, updatedPlayer2) = initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
    val completeGameBoard = finalGameBoard
      .copy(player1 = updatedPlayer1, player2 = updatedPlayer2)
    completeGameBoard
    