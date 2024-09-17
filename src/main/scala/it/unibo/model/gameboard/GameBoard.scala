package it.unibo.model.gameboard

import it.unibo.model.card.Card
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.grid.TowerPosition
import it.unibo.model.gameboard.player.Bot
import it.unibo.model.gameboard.player.Person
import it.unibo.model.gameboard.player.Player
import it.unibo.model.gameboard.player.PlayerInstance
import it.unibo.model.gameboard.player.PlayerToggle

case class GameBoard(
    board: Board,
    deck: Deck,
    player1: Player,
    player2: Player,
    private val playerManager: PlayerToggle = PlayerToggle(),
    turnNumber: Int = 0,
    gamePhase: GamePhase = WindPhase
) extends GameBoardManager:

  def isGameEnded: Option[Player] =
    val player1TowersOnFire = player1.towerPositions.exists(board.isOnFire)
    val player2TowersOnFire = player2.towerPositions.exists(board.isOnFire)
    if player1TowersOnFire then Some(player2)
    else if player2TowersOnFire then Some(player1)
    else None

  def getCurrentPlayer: Player = playerManager.getCurrentState match
    case PlayerInstance.Player1 => player1
    case PlayerInstance.Player2 => player2

  def getOpponent: Player = playerManager.getCurrentState match
    case PlayerInstance.Player1 => player2
    case PlayerInstance.Player2 => player1

  def updateCurrentPlayer(player: Player): GameBoard = playerManager.getCurrentState match
    case PlayerInstance.Player1 => copy(player1 = player)
    case PlayerInstance.Player2 => copy(player2 = player)

  def changePlayer(): GameBoard =
    playerManager.toggle()
    this

  def solveEffect(effect: IGameEffect): GameBoard = gameBoardEffectSolver(this, effect)

object GameBoard:
  def apply(player1: Player, player2: Player): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    val updatedPlayer1 = player1 match
      case p: Person =>
        p
          .copy(towerPositions = Set(TowerPosition.TOP_RIGHT, TowerPosition.BOTTOM_LEFT))
      case b: Bot =>
        b
          .copy(towerPositions = Set(TowerPosition.TOP_RIGHT, TowerPosition.BOTTOM_LEFT))

    val updatedPlayer2 = player2 match
      case p: Person =>
        p
          .copy(towerPositions = Set(TowerPosition.TOP_LEFT, TowerPosition.BOTTOM_RIGHT))
      case b: Bot =>
        b
          .copy(towerPositions = Set(TowerPosition.TOP_LEFT, TowerPosition.BOTTOM_RIGHT))

    val deck = Deck("cards.yaml")
    GameBoard(b, deck, updatedPlayer1, updatedPlayer2)
