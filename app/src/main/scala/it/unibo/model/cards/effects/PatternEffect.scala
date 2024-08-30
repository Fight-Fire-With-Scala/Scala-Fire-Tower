package it.unibo.model.cards.effects

import it.unibo.model.gameboard.grid.{Position, TemplateToken, Token}
import it.unibo.model.cards.patterns.*

case class PatternChoiceEffect(pattern: Map[Position, Token]) extends CardEffect

trait PatternEffect extends CardEffect:
  val tokens: Map[String, Token]
  val template: Map[Position, Token]

  def compilePattern: Map[Position, Token] =
    PatternEffect.fillPattern(template, tokens)

object PatternEffect:
  private def fillPattern(
      skeleton: Map[Position, Token],
      tokens: Map[String, Token]
  ): Map[Position, Token] = skeleton.map {
    case (pos, TemplateToken(id: String)) if tokens.contains(id) => pos -> tokens(id)
    case (pos, token)                                            => pos -> token
  }

final case class VeryLargeEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a; a; a; b; a; a; a; a }.mapTo(3, 3)

final case class LargeEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a; a; a }.mapTo(2, 2)

final case class MediumEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a; a }.mapTo(1, 3)

final case class MediumAltEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; b; a }.mapTo(1, 3)

final case class SmallEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern { a; a }.mapTo(1, 2)

final case class VerySmallEffect(tokens: Map[String, Token]) extends PatternEffect:
  override val template: Map[Position, Token] = pattern(a).mapTo(1, 1)
