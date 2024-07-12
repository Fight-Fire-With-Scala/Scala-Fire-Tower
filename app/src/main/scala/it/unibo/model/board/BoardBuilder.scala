package it.unibo.model.board

import it.unibo.model.board.GivenExtension.within
import it.unibo.model.cells.{Cell, Empty, EternalFire, Tower, Woods}

import scala.annotation.targetName

/** Builder of a chess board. */
class BoardBuilder:
  private var board: Board = Board.empty
  private var indexOfNextCell = BoardBuilder.MinIndex

  def setNextCell(cell: Cell): this.type =
    if (this.indexOfNextCell <= BoardBuilder.MaxIndex) {
      this.board = this.board.update(this.nextPosition -> cell)
      this.indexOfNextCell += 1
      this
    } else this

  /** Alias for [[BoardBuilder.setNextCell]]. */
  @targetName("setNextCellAlias")
  def +(cell: Cell): this.type = this.setNextCell(cell)

  /** Skip the next cells of the current row. Can be repeated multiple times.
    * @param repeats
    *   the number of rows to skip, including the current row
    * @return
    *   this
    */
  def nextRow(repeats: Int = 1): this.type = repeats match
    case 0 => this
    case n =>
      this.indexOfNextCell = this.indexOfNextCell + Board.Size - this.indexOfNextCell % Board.Size
      nextRow(n - 1)

  /** Alias for [[BoardBuilder.nextRow]]. */
  @targetName("nextRowAlias")
  def -(repeats: Int = 1): this.type = nextRow(repeats)

  /** @return the position of the next cell to fill */
  private def nextPosition: Position =
    Position(this.indexOfNextCell % Board.Size, Board.Size - 1 - this.indexOfNextCell / Board.Size)

  /** @return
    *   the chess board initialized by this builder
    * @note
    *   all the cells that are not filled before the call to this method will be considered to be
    *   empty
    */
  def build: Board = this.board

/** Companion object of [[BoardBuilder]]. */
object BoardBuilder:
  export BoardBuilder.DSL.*

  /** The starting index of a [[BoardBuilder]]. */
  private val MinIndex = 0

  /** The last index of a [[BoardBuilder]]. */
  private val MaxIndex = Board.positionNumber - 1

  /** @param configuration
    *   the context of the specified configuration
    * @return
    *   a builder configured with the specified configuration
    */
  def configure(configuration: BoardBuilder ?=> BoardBuilder): BoardBuilder =
    within(BoardBuilder())(configuration)

  /** A DSL definition for a [[BoardBuilder]]. */
  object DSL:
    /** The towers. */
    def T(using b: BoardBuilder): BoardBuilder = b + Tower(Empty)

    /** A white pawn. */
    def F(using b: BoardBuilder): BoardBuilder = b + Woods(Empty)

    /** A white knight. */
    def E(using b: BoardBuilder): BoardBuilder = b + EternalFire(Empty)
//
//    /** Skip the next cells of the row, making them empty. */
//    @targetName("nextRow")
//    def **(using b: BoardBuilder): BoardBuilder = **()
//
//    /**
//     * Skip the next cells of the row. Can be repeated multiple times.
//     * @param repeats the number of rows to skip, including this row
//     */
//    @targetName("nextRowRepeated")
//    def **(repeats: Int = 1)(using b: BoardBuilder): BoardBuilder = b - repeats

    extension (self: BoardBuilder)
      /** DSL separator for cells. */
      @targetName("separator")
      def |(b: BoardBuilder): BoardBuilder = b
