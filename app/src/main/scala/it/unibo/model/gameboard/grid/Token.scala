package it.unibo.model.gameboard.grid

sealed trait Token:
  def id: String

enum ConcreteToken(override val id: String) extends Token:
  case Firebreak extends ConcreteToken("k")
  case Fire extends ConcreteToken("f")
  case Water extends ConcreteToken("w")
  case Reforest extends ConcreteToken("r")
  case Empty extends ConcreteToken("e")

case class TemplateToken(override val id: String) extends Token:
  override def toString: String = id
