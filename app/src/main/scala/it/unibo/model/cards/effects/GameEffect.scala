package it.unibo.model.cards.effects

import it.unibo.model.gameboard.{Direction, GameBoard}
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.{Position, Token}

sealed trait GameEffect

sealed case class BoardEffect(board: Board) extends GameEffect

sealed case class GameboardEffect(gameboard: GameBoard) extends GameEffect

trait CardEffect extends GameEffect

sealed case class PatternComputationEffect(patterns: Set[Map[Position, Token]]) extends CardEffect

case class WindEffect(direction: Direction) extends CardEffect
