package it.unibo.model.gameboard

import it.unibo.model.gameboard.PatternDSL.a
import it.unibo.model.gameboard.PatternDSL.b
import it.unibo.model.gameboard.PatternDSL.pattern
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.TemplateToken
import it.unibo.model.gameboard.grid.Token

type Pattern = Map[Position, Token]

sealed trait PatternType:
  val tokens: Map[String, Token]
  val template: Pattern

  private def compilePattern: Pattern = PatternType.fillPattern(template, tokens)

object PatternType:
  given Conversion[PatternType, Pattern] = _.compilePattern

  private def fillPattern(
      skeleton: Pattern,
      tokens: Map[String, Token]
  ): Pattern = skeleton.map:
    case (pos, TemplateToken(id: String, _)) if tokens.contains(id) => pos -> tokens(id)
    case (pos, token)                                               => pos -> token

  final case class VeryLargePattern(tokens: Map[String, Token]) extends PatternType:
    override val template: Pattern = pattern { a; a; a; a; b; a; a; a; a }.mapTo(3, 3)

  final case class LargePattern(tokens: Map[String, Token]) extends PatternType:
    override val template: Pattern = pattern { a; a; a; a }.mapTo(2, 2)

  final case class MediumPattern(tokens: Map[String, Token]) extends PatternType:
    override val template: Pattern = pattern { a; a; a }.mapTo(1, 3)

  final case class MediumAltPattern(tokens: Map[String, Token]) extends PatternType:
    override val template: Pattern = pattern { a; b; a }.mapTo(1, 3)

  final case class SmallPattern(tokens: Map[String, Token]) extends PatternType:
    override val template: Pattern = pattern { a; a }.mapTo(1, 2)

  final case class VerySmallPattern(tokens: Map[String, Token]) extends PatternType:
    override val template: Pattern = pattern(a).mapTo(1, 1)
