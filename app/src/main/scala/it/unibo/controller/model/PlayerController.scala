package it.unibo.controller.model

import it.unibo.model.gameboard.GameBoard

trait PlayerController extends CardController:
  def fillPlayerHand(gb: GameBoard): GameBoard =
    val cardToDraw = 5 - gb.currentPlayer.hand.size
    drawCards(gb, cardToDraw)
