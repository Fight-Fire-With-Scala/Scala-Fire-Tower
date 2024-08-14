package it.unibo.model.prolog

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import alice.tuprolog.{Term, Theory}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{BasicGrid, Cell, Grid, Position, Token}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.prolog.PrologProgram.{cardsProgram, solverProgram}

abstract class AbstractPrologSpec extends AnyWordSpecLike with Matchers:

  given Conversion[Rule, Term] = _.term
  given Conversion[Direction, List[Direction]] = List(_)

  protected val defaultDirections: List[Direction] = Direction.values.toList
  protected val defaultTokens: Map[Position, Token] = Map.empty

  private val cells: Map[Position, Cell] = Map(
      Position(0, 0) -> Tower,
      Position(0, 1) -> Woods,
      Position(0, 2) -> Woods,
      Position(0, 3) -> Woods,
      Position(0, 4) -> Tower,
      Position(1, 0) -> Woods,
      Position(1, 1) -> Woods,
      Position(1, 2) -> Woods,
      Position(1, 3) -> Woods,
      Position(1, 4) -> Woods,
      Position(2, 0) -> Woods,
      Position(2, 1) -> Woods,
      Position(2, 2) -> EternalFire,
      Position(2, 3) -> Woods,
      Position(2, 4) -> Woods,
      Position(3, 0) -> Woods,
      Position(3, 1) -> Woods,
      Position(3, 2) -> Woods,
      Position(3, 3) -> Woods,
      Position(3, 4) -> Woods,
      Position(4, 0) -> Tower,
      Position(4, 1) -> Woods,
      Position(4, 2) -> Woods,
      Position(4, 3) -> Woods,
      Position(4, 4) -> Tower
    )

  protected def buildTheory(
      pattern: Map[Position, Token],
      tokens: Map[Position, Token] = defaultTokens,
      directions: List[Direction] = defaultDirections
  ): Theory =
    val b = BoardTheory(BasicGrid(cells, tokens), pattern, directions)
    b.append(cardsProgram)
    b.append(solverProgram)
    b

  protected def buildEngine(
      pattern: Map[Position, Token],
      tokens: Map[Position, Token] = defaultTokens,
      directions: List[Direction] = defaultDirections
  ): PrologEngine = PrologEngine(buildTheory(pattern, tokens, directions))
