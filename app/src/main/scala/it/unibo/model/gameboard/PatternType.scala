package it.unibo.model.gameboard

import it.unibo.model.gameboard.PatternDSL.a
import it.unibo.model.gameboard.PatternDSL.b
import it.unibo.model.gameboard.PatternDSL.pattern
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.TemplateToken
import it.unibo.model.gameboard.grid.Token

sealed trait PatternType:
  val tokens: Map[String, Token]
  val template: Map[Position, Token]

  def compilePattern: Map[Position, Token] = PatternType.fillPattern(template, tokens)

object PatternType:
  private def fillPattern(
      skeleton: Map[Position, Token],
      tokens: Map[String, Token]
  ): Map[Position, Token] = skeleton.map:
    case (pos, TemplateToken(id: String, _)) if tokens.contains(id) => pos -> tokens(id)
    case (pos, token)                                               => pos -> token

  case class VeryLargeEffect(tokens: Map[String, Token]) extends PatternType:
    override val template: Map[Position, Token] = pattern { a; a; a; a; b; a; a; a; a }.mapTo(3, 3)

  case class LargeEffect(tokens: Map[String, Token]) extends PatternType:
    override val template: Map[Position, Token] = pattern { a; a; a; a }.mapTo(2, 2)

  case class MediumEffect(tokens: Map[String, Token]) extends PatternType:
    override val template: Map[Position, Token] = pattern { a; a; a }.mapTo(1, 3)

  case class MediumAltEffect(tokens: Map[String, Token]) extends PatternType:
    override val template: Map[Position, Token] = pattern { a; b; a }.mapTo(1, 3)

  case class SmallEffect(tokens: Map[String, Token]) extends PatternType:
    override val template: Map[Position, Token] = pattern { a; a }.mapTo(1, 2)

  case class VerySmallEffect(tokens: Map[String, Token]) extends PatternType:
    override val template: Map[Position, Token] = pattern(a).mapTo(1, 1)
