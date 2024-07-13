package it.unibo.model.cards

import scala.collection.mutable.ArrayBuffer

case class Position(x: Int, y: Int)

class Pattern:
  val cells = new ArrayBuffer[PatternCell]
  def add(c: PatternCell): Unit = cells += c
  def toMatrix(rows: Int, cols: Int): Matrix[PatternCell] =
    val dims = rows * cols
    if (cells.length < dims || cells.length > dims) {
      throw new IllegalArgumentException(s"Array size ${cells.length} is smaller than $dims")
    }
    Matrix(rows, cols, Array.tabulate(rows, cols)((i, j) => cells(i * cols + j)))

  override def toString: String = cells.mkString("Pattern(", ", ", ")")

trait PatternCell

class Firebreak extends PatternCell:
  override def toString: String = "b"
class Fire extends PatternCell:
  override def toString: String = "f"
class Water extends PatternCell:
  override def toString: String = "w"
class Empty extends PatternCell:
  override def toString: String = "e"

//noinspection ScalaUnusedExpression
def pattern(init: Pattern ?=> Unit) =
  given t: Pattern = Pattern()
  init
  t

def b(using t: Pattern): Unit = t.add(Firebreak())
def f(using t: Pattern): Unit = t.add(Fire())
def w(using t: Pattern): Unit = t.add(Water())
def e(using t: Pattern): Unit = t.add(Empty())