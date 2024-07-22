package it.unibo.model.cards.types

import it.unibo.model.cards.effects.PatternChoiceEffect
import it.unibo.model.grid.{Empty, Fire, Firebreak, Position, Reforest, Token, Water}

import scala.collection.mutable.ArrayBuffer

class PatternDSL:
  val tokens = new ArrayBuffer[Token]
  def add(c: Token): Unit = tokens += c
  def mapTo(nRows: Int, nCols: Int): PatternChoiceEffect =
    val map = tokens.zipWithIndex.map { case (token, index) =>
      val row = index / nCols
      val col = index % nCols
      Position(row, col) -> token
    }.toMap
    PatternChoiceEffect(map)

  override def toString: String = tokens.mkString("Pattern(", ", ", ")")

//noinspection ScalaUnusedExpression
def pattern(init: PatternDSL ?=> Unit) =
  given t: PatternDSL = PatternDSL()
  init
  t

def b(using t: PatternDSL): Unit = t.add(Firebreak())
def f(using t: PatternDSL): Unit = t.add(Fire())
def w(using t: PatternDSL): Unit = t.add(Water())
def r(using t: PatternDSL): Unit = t.add(Reforest())
def e(using t: PatternDSL): Unit = t.add(Empty())
