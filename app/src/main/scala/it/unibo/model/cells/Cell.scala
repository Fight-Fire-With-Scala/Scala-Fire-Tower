package it.unibo.model.cells

import com.typesafe.scalalogging.Logger

val logger = Logger("cells")

trait Cell:
  def name: String
  def cellStatus: CellState

trait Flammable:
  def ignite(): Cell

trait FlammableCell extends Cell with Flammable

trait Lockable:
  def placeFirebreak(): Cell

trait LockableCell extends Cell with Lockable

object CellConstraints:
  extension (cell: Cell)
    def ignite(): Option[Cell] = cell match
      case flammable: Flammable => Option(flammable.ignite())
      case _ => None

  extension (cell: Cell)
    def placeFirebreak(): Option[Cell] = cell match
      case lockable: Lockable => Option(lockable.placeFirebreak())
      case _ => None

case class EternalFire(cellStatus: CellState) extends Cell:
  val name = "Eternal Fire"

case class Woods(cellStatus: CellState) extends FlammableCell, LockableCell:
  val name = "Woods"
  override def ignite(): Cell = Woods(Fire)
  override def placeFirebreak(): Cell = Woods(Firebreak)

case class Tower(cellStatus: CellState) extends FlammableCell:
  val name = "Tower"
  override def ignite(): Cell = Tower(Fire)
