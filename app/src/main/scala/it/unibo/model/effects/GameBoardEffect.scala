package it.unibo.model.effects

import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.gameboard.GameBoard

final case class GameBoardEffect(gameBoard: GameBoard) extends IGameEffect
