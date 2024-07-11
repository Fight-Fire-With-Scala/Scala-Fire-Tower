package it.unibo.model.cells

sealed trait CellState:
  def name: String

case object Firebreaker extends CellState:
  val name = "Firebreaker"

case object Fire extends CellState:
  val name = "Fire"

case object Empty extends CellState:
  val name = "Empty"

def parseCellState(state: CellState): Unit = state match
  case Firebreaker => logger.debug(s"This is a cell in state ${Firebreaker.name}")
  case Fire => logger.debug(s"This is a cell in state ${Fire.name}")
  case Empty => logger.debug(s"This is a cell in state ${Empty.name}")
