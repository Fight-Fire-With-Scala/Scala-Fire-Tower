package it.unibo.model.cards.effects

import it.unibo.model.gameboard.grid.{Position, TemplateToken, Token}
import it.unibo.model.cards.patterns.*

trait PatternEffect extends CardEffect:
  val tokens: Map[String, Token]
  val template: Map[Position, Token]

object PatternEffect:
  private def fillPattern(
      skeleton: Map[Position, Token],
      tokens: Map[String, Token]
  ): Map[Position, Token] = skeleton.map {
    case (pos, TemplateToken(id: String)) if tokens.contains(id) => pos -> tokens(id)
    case (pos, token)                                            => pos -> token
  }
  def unapply(tokens: Map[String, Token], template: Map[Position, Token]): Map[Position, Token] =
    PatternEffect.fillPattern(template, tokens)

case class VeryLargeEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a; a; a; b; a; a; a; a }.mapTo(3, 3)

case class LargeEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a; a; a }.mapTo(2, 2)

case class MediumEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a; a }.mapTo(1, 3)

case class MediumAltEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; b; a }.mapTo(1, 3)

case class SmallEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a }.mapTo(1, 2)

case class VerySmallEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern(a).mapTo(1, 1)
