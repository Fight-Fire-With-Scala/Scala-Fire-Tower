package it.unibo.model.effect

import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.gameboard.GameBoard

final case class GameBoardEffect(gameBoard: GameBoard) extends IGameEffect
