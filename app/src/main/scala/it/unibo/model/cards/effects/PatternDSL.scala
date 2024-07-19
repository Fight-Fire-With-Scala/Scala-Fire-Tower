package it.unibo.model.cards.effects

import it.unibo.model.grid.Position
import scala.collection.mutable.ArrayBuffer

class Pattern:
  val tokens = new ArrayBuffer[Token]
  def add(c: Token): Unit = tokens += c
  def toMap(nRows: Int, nCols: Int): Map[Position, Token] = {
    tokens.zipWithIndex.map { case (token, index) =>
      val row = index / nCols
      val col = index % nCols
      Position(row, col) -> token
    }.toMap
  }
  override def toString: String = tokens.mkString("Pattern(", ", ", ")")

//noinspection ScalaUnusedExpression
def pattern(init: Pattern ?=> Unit) =
  given t: Pattern = Pattern()
  init
  t

def b(using t: Pattern): Unit = t.add(Firebreak())
def f(using t: Pattern): Unit = t.add(Fire())
def w(using t: Pattern): Unit = t.add(Water())
def e(using t: Pattern): Unit = t.add(Empty())
