package it.unibo.model.gameboard.player

import it.unibo.controller.BotSubject
import it.unibo.model.effect.hand.HandManager
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.{ Bot, Person, Player }

trait PlayerManager extends HandManager:
  protected def fillPlayerHand(gb: GameBoard, player: Player): (GameBoard, Player) =
    val cardToDraw = 5 - player.hand.size
    drawCards(gb, cardToDraw)(player)

  protected def initializeBot(gb: GameBoard, player: Player, observable: BotSubject): (
      GameBoard,
      Player
  ) =
    val (newGb, pl) = initializePlayer(gb, player)
    pl match
      case b: Bot => (newGb, b.copy(botObservable = Some(observable)))
      case _      => (gb, pl)

  protected def initializePlayer(gb: GameBoard, player: Player): (GameBoard, Player) =
    val (newGb, newPlayer) = fillPlayerHand(gb, player)
    drawSpecialCards(newGb, 1)(newPlayer)

  protected def updatePlayer(gb: GameBoard, move: Move): GameBoard =
    val updatedPlayerMoves = gb.getCurrentPlayer.moves.filter(m => m != move)
    gb.getCurrentPlayer match
      case b: Bot =>
        val updatedPlayer = b.updatePlayer(moves = updatedPlayerMoves)
        gb.updateCurrentPlayer(updatedPlayer)
      case p: Person =>
        val updatedPlayer = p.updatePlayer(moves = updatedPlayerMoves)
        gb.updateCurrentPlayer(updatedPlayer)
      case _ => gb
