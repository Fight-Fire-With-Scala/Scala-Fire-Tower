package it.unibo.model.grid

import it.unibo.model.grid.GivenExtension.within

import scala.annotation.{tailrec, targetName}

/** Builder of a grid. */
class GridBuilder:
  private var grid: Grid = Grid.empty
  private var indexOfNextCell = GridBuilder.MinIndex

  private def setNextCell(cell: Cell): this.type =
    if (this.indexOfNextCell <= GridBuilder.MaxIndex) {
      this.grid = this.grid.update(this.nextPosition -> cell)
      this.indexOfNextCell += 1
      this
    } else this

  /** Alias for [[GridBuilder.setNextCell]]. */
  @targetName("setNextCellAlias")
  def +(cell: Cell): this.type = this.setNextCell(cell)

  /** Skip the next cells of the current row. Can be repeated multiple times.
    *
    * @param repeats
    *   the number of rows to skip, including the current row
    * @return
    *   this
    */
  @tailrec
  private def nextRow(repeats: Int = 1): this.type = repeats match
    case 0 => this
    case n =>
      this.indexOfNextCell = this.indexOfNextCell + Grid.Size - this.indexOfNextCell % Grid.Size
      nextRow(n - 1)

  /** Alias for [[GridBuilder.nextRow]]. */
  @targetName("nextRowAlias")
  def -(repeats: Int = 1): this.type = nextRow(repeats)

  /** @return the position of the next cell to fill */
  private def nextPosition: Position =
    Position(this.indexOfNextCell % Grid.Size, Grid.Size - 1 - this.indexOfNextCell / Grid.Size)

  /** @return
    *   the grid initialized by this builder
    * @note
    *   all the cells that are not filled before the call to this method will be considered to be
    *   empty
    */
  def build: Grid = this.grid

/** Companion object of [[GridBuilder]]. */
object GridBuilder:
  export GridBuilder.DSL.*

  private val MinIndex = 0

  private val MaxIndex = Grid.positionNumber - 1

  /** @param configuration
    *   the context of the specified configuration
    * @return
    *   a builder configured with the specified configuration
    */
  def configure(configuration: GridBuilder ?=> GridBuilder): GridBuilder =
    within(GridBuilder())(configuration)

  /** A DSL definition for a [[GridBuilder]]. */
  object DSL:

    def T(using b: GridBuilder): GridBuilder = b + Tower()

    def F(using b: GridBuilder): GridBuilder = b + Woods()

    def E(using b: GridBuilder): GridBuilder = b + EternalFire()

    extension (self: GridBuilder)
      /** DSL separator for cells. */
      @targetName("separator")
      def |(b: GridBuilder): GridBuilder = b
