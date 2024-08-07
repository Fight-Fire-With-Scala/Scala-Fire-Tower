package it.unibo.model.cards.patterns

import it.unibo.model.gameboard.grid.{TemplateToken, Empty, Fire, Firebreak, Position, Reforest, Token, Water}

import scala.collection.mutable.ArrayBuffer

class PatternDSL:
  val tokens = new ArrayBuffer[Token]
  def add(c: Token): Unit = tokens += c
  def mapTo(nRows: Int, nCols: Int): Map[Position, Token] =
    tokens.zipWithIndex.map { case (token, index) =>
      val row = index / nCols
      val col = index % nCols
      Position(row, col) -> token
    }.toMap

  override def toString: String = tokens.mkString("Pattern(", ", ", ")")

//noinspection ScalaUnusedExpression
def pattern(init: PatternDSL ?=> Unit) =
  given t: PatternDSL = PatternDSL()
  init
  t

def a(using t: PatternDSL): Unit = t.add(TemplateToken("a"))
def b(using t: PatternDSL): Unit = t.add(TemplateToken("b"))
def k(using t: PatternDSL): Unit = t.add(Firebreak)
def f(using t: PatternDSL): Unit = t.add(Fire)
def w(using t: PatternDSL): Unit = t.add(Water)
def r(using t: PatternDSL): Unit = t.add(Reforest)
def e(using t: PatternDSL): Unit = t.add(Empty)
