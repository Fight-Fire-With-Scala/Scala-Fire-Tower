package it.unibo.model.cards.effects.patterns

trait PatternCell

case class Firebreak() extends PatternCell:
  override def toString: String = "b"
case class Fire() extends PatternCell:
  override def toString: String = "f"
case class Water() extends PatternCell:
  override def toString: String = "w"
case class Empty() extends PatternCell:
  override def toString: String = "e"
