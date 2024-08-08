package it.unibo.model.cards.effects

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.{Position, Token}

sealed trait GameEffect

sealed case class BoardEffect(board: Board) extends GameEffect

sealed case class GameboardEffect(gameboard: GameBoard) extends GameEffect

trait CardEffect extends GameEffect

sealed case class PatternComputationEffect(patterns: List[Map[Position, Token]]) extends CardEffect

enum WindEffect extends CardEffect:
  case North
  case South
  case East
  case West
