package it.unibo.model.gameboard.grid

import it.unibo.model.gameboard.grid.ConcreteToken.*
import it.unibo.model.gameboard.grid.Cell.*

trait Grid:
  def cells: Map[Position, Cell]

  def tokens: Map[Position, Token]

  def getCell(position: Position): Option[Cell]

  def getToken(position: Position): Option[Token]

  def setCell(position: Position, cell: Cell): Grid

  def setToken(position: Position, token: Token): Grid
  
  def setTokens(tokens: (Position, Token)*): Grid =
    tokens.foldLeft(this) { case (grid, (position, token)) => grid.setToken(position, token) }

  def update(updates: (Position, Cell)*): Grid = updates
    .foldLeft(this) { case (grid, (position, cell)) => grid.setCell(position, cell) }

  override def toString: String

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

final case class BasicGrid(
    private val _cells: Map[Position, Cell] = Map.empty,
    private val _tokens: Map[Position, Token] = Map.empty
) extends Grid:
  override def cells: Map[Position, Cell] = this._cells
  override def tokens: Map[Position, Token] = this._tokens

  override def setToken(position: Position, token: Token): Grid = getCell(position) match
    case Some(_: EternalFire.type) => this
    case Some(_: Woods.type)       => handleTokenForWoods(position, token)
    case _                         => BasicGrid(this._cells, this._tokens + (position -> token))

  private def handleTokenForWoods(position: Position, token: Token): Grid =
    getToken(position) match
      case Some(Fire) =>
        token match
          case Water => BasicGrid(this._cells, this._tokens - position)
          case _ => BasicGrid(this._cells, this._tokens + (position -> token))
      case Some(Firebreak)   =>
        token match
          case Fire => this
          case _ => BasicGrid(this._cells, this._tokens + (position -> token))
      case Some(Water) | Some(Reforest) | Some(Empty) =>
        BasicGrid(this._cells, this._tokens + (position -> token))
      case _ =>
        if token == Water || token == Empty then this
        else BasicGrid(this._cells, this._tokens + (position -> token))

  override def getToken(position: Position): Option[Token] = this._tokens.get(position)

  override def setCell(position: Position, cell: Cell): Grid =
    BasicGrid(this._cells + (position -> cell), this._tokens)

  override def getCell(position: Position): Option[Cell] = this._cells.get(position)

  override def toString: String =
    val gridRepresentation = (for
      i <- 0 until Grid.Size
      j <- 0 until Grid.Size
      position = Position(i, j)
      cell = getCell(position).orNull
      token = getToken(position)
      cellChar = cell match
        case _: Woods.type       => "W"
        case _: Tower.type       => "T"
        case _: EternalFire.type => "E"
      tokenChar = token match
        case Some(Fire)      => "f"
        case Some(Firebreak) => "b"
        case _               => cellChar // Use cellChar if there's no token
      displayChar =
        if (token.isDefined) tokenChar
        else cellChar // Display tokenChar without "W" if token exists
      separator = if (j == Grid.Size - 1) "\n" else " | "
    yield s"$displayChar$separator").mkString

    gridRepresentation.stripMargin
