package it.unibo.model

import com.typesafe.scalalogging.Logger

val logger = Logger("name")

sealed trait CellType {
  def name: String
}

case object EternalFire extends CellType {
  val name = "Eternal Fire"
}

case object Woods extends CellType {
  val name = "Woods"
}

sealed trait CellState {
  def name: String
}

case object Firebreaker extends CellState {
  val name = "Firebreaker"
}

case class Cell(cellType: CellType, cellStatus: CellState)

def parseCellState(state: CellState): Unit = state match
  case Firebreaker => logger.debug(s"This is a cell in state ${Firebreaker.name}")

def parseCellType(cellType: CellType): Unit = cellType match
  case EternalFire => logger.debug(s"This is a cell of type ${EternalFire.name}")
  case Woods => logger.debug(s"This is a cell of type ${Woods.name}")
