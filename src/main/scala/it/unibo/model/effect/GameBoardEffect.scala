package it.unibo.model.effect

import it.unibo.model.effect.core.GameEffect
import it.unibo.model.gameboard.GameBoard

final case class GameBoardEffect(gameBoard: GameBoard) extends GameEffect
