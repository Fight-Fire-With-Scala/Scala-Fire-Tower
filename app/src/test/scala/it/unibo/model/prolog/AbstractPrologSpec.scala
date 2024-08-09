package it.unibo.model.prolog

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import alice.tuprolog.{Struct, Term, Theory}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{Cell, Position}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.gameboard.grid.ConcreteToken.*
import it.unibo.model.prolog.PrologProgram.solverProgram
import it.unibo.model.prolog.PrologUtils.given

abstract class AbstractPrologSpec extends AnyWordSpecLike with Matchers:
  given Conversion[Rule, Term] = _.toTerm

  protected val directions: List[String] = Direction.values.map(_.getId).toList
  protected val deltas: List[Struct] = Direction.values.map(_.getDelta)
    .map(d => Struct.tuple(d.x, d.y)).toList
  protected val dummyBoard: (List[String], List[Struct]) => Theory = (directions, deltas) =>
    Theory.fromPrologList(Struct.list(
      // Cells
      Struct.of("cell", Struct.tuple(0, 0), Tower),
      Struct.of("cell", Struct.tuple(0, 1), Woods),
      Struct.of("cell", Struct.tuple(0, 2), Woods),
      Struct.of("cell", Struct.tuple(0, 3), Woods),
      Struct.of("cell", Struct.tuple(0, 4), Tower),
      Struct.of("cell", Struct.tuple(1, 0), Woods),
      Struct.of("cell", Struct.tuple(1, 1), Woods),
      Struct.of("cell", Struct.tuple(1, 2), Woods),
      Struct.of("cell", Struct.tuple(1, 3), Woods),
      Struct.of("cell", Struct.tuple(1, 4), Woods),
      Struct.of("cell", Struct.tuple(2, 0), Woods),
      Struct.of("cell", Struct.tuple(2, 1), Woods),
      Struct.of("cell", Struct.tuple(2, 2), EternalFire),
      Struct.of("cell", Struct.tuple(2, 3), Woods),
      Struct.of("cell", Struct.tuple(2, 4), Woods),
      Struct.of("cell", Struct.tuple(3, 0), Woods),
      Struct.of("cell", Struct.tuple(3, 1), Woods),
      Struct.of("cell", Struct.tuple(3, 2), Woods),
      Struct.of("cell", Struct.tuple(3, 3), Woods),
      Struct.of("cell", Struct.tuple(3, 4), Woods),
      Struct.of("cell", Struct.tuple(4, 0), Tower),
      Struct.of("cell", Struct.tuple(4, 1), Woods),
      Struct.of("cell", Struct.tuple(4, 2), Woods),
      Struct.of("cell", Struct.tuple(4, 3), Woods),
      Struct.of("cell", Struct.tuple(4, 4), Tower),

      // Tokens
      Struct.of("token", Struct.tuple(0, 0), Fire),
      Struct.of("token", Struct.tuple(0, 1), Fire),

      // Grid size
      Struct.of("numRows", 5),
      Struct.of("numCols", 5),

      // Patterns
      Struct.of("pattern", Struct.tuple(0, 0), Fire),
      Struct.of("pattern", Struct.tuple(0, 1), Fire),
      Struct.of("directions", directions),
      Struct.of("deltas", deltas)
    ))

  protected val theory: (List[String], List[Struct]) => Theory = (directions, deltas) =>
    val b = dummyBoard(directions, deltas).toString
    val t: Theory = Theory.parseWithStandardOperators(b)
    t.append(solverProgram)
    t

  protected val engine: PrologEngine = PrologEngine(theory(directions, deltas))

  protected val dummyPattern = "[(0, 1, f), (0, 2, f)]"
  protected val dummyPosition = "(0, 2)"
  protected val dummyTrueRequiredCells = "[w]"
  protected val dummyTrueRequiredTokens = "[f]"
  protected val dummyFalseRequiredCells = "[ef]"
  protected val dummyFalseRequiredTokens = "[k]"
  protected val dummyAllowedCells = "[t]"
  protected val dummyAllowedTokens = "[f]"
