package it.unibo.model.gameboard.grid

sealed trait Token

case object Firebreak extends Token:
  override def toString: String = "k"
  
case object Fire extends Token:
  override def toString: String = "f"
  
case object Water extends Token:
  override def toString: String = "w"
  
case object Reforest extends Token:
  override def toString: String = "r"
  
case object Empty extends Token:
  override def toString: String = "e"

case class TemplateToken(id: String) extends Token:
  override def toString: String = id