package it.unibo.controller.model

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Player

trait PlayerController extends CardController:
  def fillPlayerHand(gb: GameBoard, player: Player): (GameBoard, Player) =
    val cardToDraw = 5 - player.hand.size
    drawCards(gb, cardToDraw)(player)

  def initializePlayer(gb: GameBoard, player: Player): (GameBoard, Player) =
    val (newGb, newPlayer) = fillPlayerHand(gb, player)
    drawSpecialCards(newGb, 1)(newPlayer)
