package it.unibo.model.gameboard

import it.unibo.model.gameboard.grid.ConcreteToken.*
import it.unibo.model.gameboard.grid.{Position, TemplateToken, Token}

import scala.collection.mutable.ArrayBuffer

final class PatternDSL:
  val tokens = new ArrayBuffer[Token]
  def add(c: Token): Unit = tokens += c
  def mapTo(nRows: Int, nCols: Int): Map[Position, Token] = tokens.zipWithIndex
    .map { case (token, index) =>
      val row = index / nCols
      val col = index % nCols
      Position(row, col) -> token
    }.toMap

  override def toString: String = tokens.mkString("Pattern(", ", ", ")")

object PatternDSL:

  // noinspection ScalaUnusedExpression
  def pattern(init: PatternDSL ?=> Unit): PatternDSL =
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
