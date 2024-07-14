package it.unibo.model.cards.patterns

import it.unibo.model.cards.patterns.SpatialPattern
import scala.collection.mutable.ArrayBuffer

class Pattern:
  val cells = new ArrayBuffer[PatternCell]
  def add(c: PatternCell): Unit = cells += c
  def toMatrix(rows: Int, cols: Int): Option[SpatialPattern] =
    val dims = rows * cols
    if (cells.length < dims || cells.length > dims) None
    else Some(Matrix(rows, cols, Array.tabulate(rows, cols)((i, j) => cells(i * cols + j))))

  override def toString: String = cells.mkString("Pattern(", ", ", ")")

//noinspection ScalaUnusedExpression
def pattern(init: Pattern ?=> Unit) =
  given t: Pattern = Pattern()
  init
  t

def b(using t: Pattern): Unit = t.add(Firebreak())
def f(using t: Pattern): Unit = t.add(Fire())
def w(using t: Pattern): Unit = t.add(Water())
def e(using t: Pattern): Unit = t.add(Empty())
