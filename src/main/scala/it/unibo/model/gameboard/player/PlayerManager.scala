package it.unibo.model.gameboard.player

trait Toggle[T]:
  def toggle(): Unit
  def getCurrentState: T

enum PlayerInstance:
  case Player1, Player2

class PlayerManager extends Toggle[PlayerInstance]:
  private var currentPlayer: PlayerInstance = PlayerInstance.Player1

  override def toggle(): Unit = currentPlayer = currentPlayer match
    case PlayerInstance.Player1 => PlayerInstance.Player2
    case PlayerInstance.Player2 => PlayerInstance.Player1

  override def getCurrentState: PlayerInstance = currentPlayer
