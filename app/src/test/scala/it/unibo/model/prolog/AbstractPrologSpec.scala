package it.unibo.model.prolog

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import alice.tuprolog.{Term, Theory}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{BasicGrid, Cell, Token, Position}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.prolog.PrologProgram.{cardsProgram, solverProgram}

abstract class AbstractPrologSpec extends AnyWordSpecLike with Matchers:

  given Conversion[Rule, Term] = _.term
  given Conversion[Direction, List[Direction]] = List(_)

  protected val defaultDirections: List[Direction] = Direction.values.toList

  private val cells: Map[Position, Cell] = Map(
    Position(0, 0) -> Tower,
    Position(0, 1) -> Woods,
    Position(0, 2) -> Woods,
    Position(1, 0) -> Woods,
    Position(1, 1) -> EternalFire,
    Position(1, 2) -> Woods,
    Position(2, 0) -> Woods,
    Position(2, 1) -> Woods,
    Position(2, 2) -> Tower
  )
  private val tokens: Map[Position, Token] = Map()
  private val grid = BasicGrid(cells, tokens)

  protected val buildTheory: (Map[Position, Token], List[Direction]) => Theory =
    (pattern, direction) =>
      val b = BoardTheory(grid, pattern, direction)
      b.append(cardsProgram)
      b.append(solverProgram)
      b

  protected val buildEngine: (Map[Position, Token], List[Direction]) => PrologEngine =
    (pattern, direction) => PrologEngine(buildTheory(pattern, direction))
