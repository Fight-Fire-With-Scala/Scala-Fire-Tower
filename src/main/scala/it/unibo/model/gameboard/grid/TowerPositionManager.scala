package it.unibo.model.gameboard.grid

import it.unibo.model.gameboard.grid.Cell.Tower
import it.unibo.model.gameboard.grid.Grid.Size

enum TowerPosition(val position: Position):
  case TOP_LEFT extends TowerPosition(Position(0, 0))
  case BOTTOM_RIGHT extends TowerPosition(Position(Grid.Size - 1, Grid.Size - 1))
  case TOP_RIGHT extends TowerPosition(Position(0, Grid.Size - 1))
  case BOTTOM_LEFT extends TowerPosition(Position(Grid.Size - 1, 0))

object TowerPositionManager:

  def getTowerCells(cells: Map[Position, Cell], towerPositions: Set[TowerPosition]): Set[Position] =
    val halfSize = Size / 2

    def isInDiagonal(position: Position, towerPosition: TowerPosition): Boolean =
      towerPosition match
        case TowerPosition.TOP_LEFT     => position.row < halfSize && position.col < halfSize
        case TowerPosition.BOTTOM_RIGHT => position.row >= halfSize && position.col >= halfSize
        case TowerPosition.TOP_RIGHT    => position.row < halfSize && position.col >= halfSize
        case TowerPosition.BOTTOM_LEFT  => position.row >= halfSize && position.col < halfSize

    cells.collect {
      case (position, cell: Tower.type) if towerPositions.exists(isInDiagonal(position, _)) =>
        position
    }.toSet