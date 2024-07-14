package it.unibo.model.cells

sealed trait CellState:
  def name: String

case object Firebreak extends CellState:
  val name = "Firebreak"

case object Fire extends CellState:
  val name = "Fire"

case object Empty extends CellState:
  val name = "Empty"

def parseCellState(state: CellState): Unit = state match
  case Firebreak => logger.debug(s"This is a cell in state ${Firebreak.name}")
  case Fire => logger.debug(s"This is a cell in state ${Fire.name}")
  case Empty => logger.debug(s"This is a cell in state ${Empty.name}")
