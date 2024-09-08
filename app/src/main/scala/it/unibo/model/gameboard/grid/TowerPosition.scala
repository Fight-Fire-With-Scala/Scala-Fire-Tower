package it.unibo.model.gameboard.grid

enum TowerPosition:
  case TOP_RIGHT, BOTTOM_LEFT

  private def center: Position = this match
    case TOP_RIGHT => Position(1, 14)
    case BOTTOM_LEFT => Position(14, 1)

  def squarePositions: Set[Position] =
    val center = this.center
    (for
      row <- center.row - 1 to center.row + 1
      col <- center.col - 1 to center.col + 1
    yield Position(row, col)).toSet