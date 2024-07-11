package it.unibo.model.cells

import com.typesafe.scalalogging.Logger

val logger = Logger("name")

trait Cell:
  def name: String
  def cellStatus: CellState

trait Flammable extends Cell:
  def ignite(): Cell

trait Lockable extends Cell:
  def placeFirebreak(): Cell

object CellConstraints:
  extension (cell: Cell)
    def ignite(): Option[Cell] = cell match
      case flammable: Flammable => Option(flammable.ignite())
      case _ => Option.empty

  extension (cell: Cell)
    def placeFirebreak(): Option[Cell] = cell match
      case lockable: Lockable => Option(lockable.placeFirebreak())
      case _ => Option.empty
        
case class EternalFire(cellStatus: CellState) extends Cell:
  val name = "Eternal Fire"

case class Woods(cellStatus: CellState) extends Flammable, Lockable:
  val name = "Woods"
  override def ignite(): Cell = Woods(Fire)
  override def placeFirebreak(): Cell = Woods(Firebreaker)

case class Tower(cellStatus: CellState) extends Flammable:
  val name = "Tower"
  override def ignite(): Cell = Tower(Fire)
