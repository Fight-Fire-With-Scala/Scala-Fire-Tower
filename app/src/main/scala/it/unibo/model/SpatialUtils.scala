package it.unibo.model

import it.unibo.model.cards.effects.patterns.PatternCell

case class Position(x: Int, y: Int)

case class Matrix[T](rows: Int, cols: Int, data: Array[Array[T]]):
  def apply(row: Int, col: Int): T = data(row)(col)
  def prettyPrint(): Unit = data.foreach(row => println(row.mkString(" ")))

type SpatialPattern = Matrix[PatternCell]

type AppliedSpatialPattern = Matrix[Position]
