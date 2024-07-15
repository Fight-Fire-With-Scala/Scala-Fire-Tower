package it.unibo.model.cells

object CellConstraints:
  extension (cell: Cell)
    def isFlammable: Boolean = cell match
      case flammable: Flammable => flammable.isFlammable
      case _                    => false

  extension (cell: Cell)
    def isLockable: Boolean = cell match
      case lockable: Lockable => lockable.isLockable
      case _                  => false
