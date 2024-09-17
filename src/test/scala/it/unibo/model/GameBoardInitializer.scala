package it.unibo.model

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.{ Player, PlayerManager }

trait GameBoardInitializer extends PlayerManager:
  def initialiseGameBoardPlayers(gb: GameBoard, player1: Player, player2: Player): GameBoard =
    val (updatedGameBoard, updatedPlayer1) = initializePlayer(gb, gb.getCurrentPlayer)
    val (finalGameBoard, updatedPlayer2) =
      initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
    val completeGameBoard = finalGameBoard.copy(player1 = updatedPlayer1, player2 = updatedPlayer2)
    completeGameBoard
