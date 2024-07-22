package it.unibo.model.grid

sealed trait Token

case class Firebreak() extends Token:
  override def toString: String = "b"
  
case class Fire() extends Token:
  override def toString: String = "f"
  
case class Water() extends Token:
  override def toString: String = "w"
  
case class Reforest() extends Token:
  override def toString: String = "r"
  
case class Empty() extends Token:
  override def toString: String = "e"
