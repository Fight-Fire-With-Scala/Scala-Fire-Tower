package it.unibo.model.gameboard.grid

import scalafx.scene.paint.Color

sealed trait Token:
  def id: String
  def color: Color

enum ConcreteToken(override val id: String, val color: Color) extends Token:
  case Firebreak extends ConcreteToken("k", Color.Purple)
  case Fire extends ConcreteToken("f", Color.DarkOrange)
  case Water extends ConcreteToken("w", Color.Blue)
  case Reforest extends ConcreteToken("r", Color.Green)
  case Empty extends ConcreteToken("e", Color.Gray)

case class TemplateToken(override val id: String, color: Color = Color.Black) extends Token:
  override def toString: String = id
