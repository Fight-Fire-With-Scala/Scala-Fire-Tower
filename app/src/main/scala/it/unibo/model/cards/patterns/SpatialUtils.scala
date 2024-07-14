package it.unibo.model.cards.patterns

type SpatialPattern = Matrix[PatternCell]

case class Matrix[T](rows: Int, cols: Int, data: Array[Array[T]]):
  def apply(row: Int, col: Int): T = data(row)(col)

  def prettyPrint(): Unit =
    data.foreach { row =>
      println(row.mkString(" "))
    }