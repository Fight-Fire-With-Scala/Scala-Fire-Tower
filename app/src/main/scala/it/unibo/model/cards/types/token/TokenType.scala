package it.unibo.model.cards.types.token

trait TokenType

object TokenType:
  val tokens: Seq[TokenType] = Seq(Firebreaker, Fire)

  private case object Firebreaker extends TokenType
  private case object Fire extends TokenType