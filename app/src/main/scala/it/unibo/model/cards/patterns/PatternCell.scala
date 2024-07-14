package it.unibo.model.cards.patterns

trait PatternCell

class Firebreak extends PatternCell:
  override def toString: String = "b"
class Fire extends PatternCell:
  override def toString: String = "f"
class Water extends PatternCell:
  override def toString: String = "w"
class Empty extends PatternCell:
  override def toString: String = "e"
