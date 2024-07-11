package it.unibo.model

sealed trait CellType {
  def name: String
}

case object EternalFire extends CellType {
  val name = "Eternal Fire"
}

sealed trait CellState {
  def name: String
}

case object Firebreaker extends CellState {
  val name = "Firebreaker"
}

case class Cell(cellType: CellType, cellStatus: CellState)

def parseCellState(state: CellState): Unit =
  state match
    case Firebreaker => println(s"This is a cell in state ${Firebreaker.name}")

def parseCellType(cellType: CellType): Unit =
  cellType match
    case EternalFire => println(s"This is a cell of type ${EternalFire.name}")
