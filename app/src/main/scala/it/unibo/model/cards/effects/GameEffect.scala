package it.unibo.model.cards.effects

import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.{Position, Token}

sealed trait GameEffect

sealed case class BoardEffect(board: Board) extends GameEffect

sealed trait CardEffect extends GameEffect

sealed case class PatternComputationEffect(patterns: List[Map[Position, Token]]) extends CardEffect

sealed case class PatternChoiceEffect(pattern: Map[Position, Token]) extends CardEffect

enum WindEffect extends CardEffect:
  case North
  case South
  case East
  case West
