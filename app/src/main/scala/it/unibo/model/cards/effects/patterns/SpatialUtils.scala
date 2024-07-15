package it.unibo.model.cards.effects.patterns

import it.unibo.model.board.Position
import it.unibo.model.cards.GameEffect

case class Matrix[T](rows: Int, cols: Int, data: Array[Array[T]]):
  def apply(row: Int, col: Int): T = data(row)(col)
  def prettyPrint(): Unit = data.foreach(row => println(row.mkString(" ")))

type SpatialPattern = Matrix[PatternCell]

type AppliedSpatialPattern = Matrix[Position]

case class PatternChoice(patterns: List[AppliedSpatialPattern]) extends GameEffect
