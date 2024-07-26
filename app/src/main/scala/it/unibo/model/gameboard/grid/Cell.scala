package it.unibo.model.gameboard.grid

sealed trait Cell

case class EternalFire() extends Cell:
  override def toString: String = "Eternal Fire"

case class Woods() extends Cell:
  override def toString: String = "Woods"

case class Tower() extends Cell:
  override def toString: String = "Tower"
