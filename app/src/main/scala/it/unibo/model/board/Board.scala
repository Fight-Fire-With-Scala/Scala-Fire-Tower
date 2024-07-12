package it.unibo.model.board

import it.unibo.model.cells.Cell

case class Position(x: Int, y: Int)

trait Board:
  def cells: Map[Position, Cell]

  def getCell(position: Position): Option[Cell]

  def setCell(position: Position, cell: Cell): Board

  //Depending on the implementation this is needed or not
  //def removeCell(position: Position): Board

  /**
   * Update the board with the given cells.
   * Depending on the implementation we could also update the position with a None cell but this would change a little
   * @param updates
   * @return
   */
  def update(updates: (Position, Cell)*): Board = updates.foldLeft(this) {
    case (board, (position, cell)) => board.setCell(position, cell)
  }

object Board:
  export BoardBuilder.*
  val Size: Int = 16
  val positionNumber: Int = Board.Size * Board.Size

  lazy val Positions: Iterable[Position] =
    for
      i <- 0 until Board.Size
      j <- 0 until Board.Size
    yield Position(i, j)

  def apply(): Board = Board.empty

  def apply(builderConfiguration: BoardBuilder ?=> BoardBuilder): Board = BoardBuilder
    .configure(builderConfiguration).build

  def empty: Board = BasicBoard()

  def standard: Board = Board {
    T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
    T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
    T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | E | E | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | E | E | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
    T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
    T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
    T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
  }

  def fromMap(cells: Map[Position, Cell]): Board = BasicBoard(cells)

  private case class BasicBoard(private val _cells: Map[Position, Cell] = Map.empty) extends Board:
    override def cells: Map[Position, Cell] = this._cells

    override def setCell(position: Position, cell: Cell): Board =
      BasicBoard(this._cells + (position -> cell))

//    override def removeCell(position: Position): Board = BasicBoard(this._cells - position)

    override def getCell(position: Position): Option[Cell] = this._cells.get(position)

//  override def toString: String =
//    Board.positions
//      .map(this.cells.get(_))
//      .map(_.map(pieceToString).getOrElse("*"))
//      .grouped(Board.size)
//      .toList
//      .reverse
//      .map(line => line.mkString(" | "))
//      .mkString("\n", "\n", "\n")
//
//  private def pieceToString(cell: Cell): String =
//    val unboundRepresentation: (String, String) =
//      cell match
//        case _: EternalFire.type => ("E", "e")
//        case _: Queen => ("Q", "q")
//        case _ => ("W", "x")
//    if piece.team == Team.WHITE then unboundRepresentation._1 else unboundRepresentation._2
