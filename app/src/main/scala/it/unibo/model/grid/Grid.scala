package it.unibo.model.grid

import it.unibo.model.cards.effects.{Token, Fire, Firebreak}

trait Grid:
  def cells: Map[Position, Cell]
  
  def tokens: Map[Position, Token]
  
  def getCell(position: Position): Option[Cell]
  
  def getToken(position: Position): Option[Token]

  def setCell(position: Position, cell: Cell): Grid
  
  def setToken(position: Position, token: Token): Grid

  def update(updates: (Position, Cell)*): Grid = updates.foldLeft(this) {
    case (grid, (position, cell)) => grid.setCell(position, cell)
  }

object Grid:
  export GridBuilder.*
  val Size: Int = 16
  val positionNumber: Int = Grid.Size * Grid.Size

  lazy val Positions: Iterable[Position] =
    for
      i <- 0 until Grid.Size
      j <- 0 until Grid.Size
    yield Position(i, j)

  def apply(): Grid = Grid.empty

  def apply(builderConfiguration: GridBuilder ?=> GridBuilder): Grid = GridBuilder
    .configure(builderConfiguration).build

  def empty: Grid = BasicGrid()

  // noinspection DuplicatedCode
  def standard: Grid = Grid {
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

  private case class BasicGrid(private val _cells: Map[Position, Cell] = Map.empty,
                               private val _tokens: Map[Position, Token] = Map.empty) extends Grid:
    override def cells: Map[Position, Cell] = this._cells
    override def tokens: Map[Position, Token] = this._tokens

    /**
     * Set a token in the grid at the given position checking if it's possible. If it's invalid do nothing
     * @param position
     * @param token
     * @return
     */
    override def setToken(position: Position, token: Token): Grid =
      ???
      
    override def getToken(position: Position): Option[Token] = ???
    
    override def setCell(position: Position, cell: Cell): Grid =
      BasicGrid(this._cells + (position -> cell))

    override def getCell(position: Position): Option[Cell] = this._cells.get(position)

    override def toString: String =
      val gridRepresentation = (for
        i <- 0 until Grid.Size
        j <- 0 until Grid.Size
        position = Position(i, j)
        cell = getCell(position).orNull
        token = getToken(position)
        cellChar = cell match
          case _: Woods => "W"
          case _: Tower => "T"
          case _: EternalFire => "E"
        tokenChar = token match
          case Some(Fire()) => "f"
          case Some(Firebreak()) => "b"
          case _ => cellChar // Use cellChar if there's no token
        displayChar =
          if (token.isDefined) tokenChar
          else cellChar // Display tokenChar without "W" if token exists
        separator = if (j == Grid.Size - 1) "\n" else " | "
      yield s"$displayChar$separator").mkString

      gridRepresentation.stripMargin
    
    