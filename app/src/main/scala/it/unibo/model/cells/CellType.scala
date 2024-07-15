package it.unibo.model.cells

trait Cell:
  def name: String

trait Flammable:
  def isFlammable: Boolean = true

trait FlammableCell extends Cell with Flammable

trait Lockable:
  def isLockable: Boolean = true

trait LockableCell extends Cell with Lockable

case class EternalFire() extends Cell:
  val name = "Eternal Fire"

case class Woods() extends FlammableCell, LockableCell:
  val name = "Woods"

case class Tower() extends FlammableCell:
  val name = "Tower"
