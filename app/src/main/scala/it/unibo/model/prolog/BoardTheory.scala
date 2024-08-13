package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Theory}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Cell.EternalFire
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.model.prolog.PrologUtils.{size, given}

import scala.jdk.CollectionConverters.*

final case class BoardTheory(
    private val grid: Grid,
    private val pattern: Map[Position, Token],
    private val directions: List[Direction]
)

object BoardTheory:
  def apply(grid: Grid, pattern: Map[Position, Token], directions: List[Direction]): Theory =
    val cellIterator = getCells(grid)
    val patternIterator = getPattern(pattern)
    val directionIterator = getDeltas(directions)
    val allIterators = cellIterator ++ patternIterator ++ directionIterator

    Theory.fromPrologList(Struct.list(allIterators.asJava))

  private def getCells(grid: Grid): Iterator[Term] =
    val cells = grid.cells.iterator.map { case (pos, cell) =>
      Struct.of("cell", Struct.tuple(pos._1, pos._2), cell)
    }.toSeq.sorted.iterator

    val tokens = grid.tokens.iterator.map { case (pos, token) =>
      Struct.of("token", Struct.tuple(pos._1, pos._2), token)
    }

//    val eternalFireTokens = grid.cells.find {
//      case (_, _: EternalFire.type) => true
//      case _                        => false
//    }.map((p, c) => Struct.of("token", Struct.tuple(p.x, p.y), Fire)).iterator

    val numRows = Iterator.single(Struct.of("numRows", grid.size))
    val numCols = Iterator.single(Struct.of("numCols", grid.size))

    cells ++ tokens ++ numRows ++ numCols

  private def getPattern(patterns: Map[Position, Token]): Iterator[Term] =
    val patternTerms = patterns.iterator.map { case (pos, pattern) =>
      Struct.of("pattern", Struct.tuple(pos._1, pos._2), pattern)
    }

    val directionsOfApplication: List[Direction] =
      if (patterns.size <= 1) List(Direction.North) else Direction.values.toList
    val directionNames = directionsOfApplication.map(_.getId)
    val directionsFact = Iterator.single(Struct.of("directions", directionNames))

    patternTerms ++ directionsFact

  private def getDeltas(directions: List[Direction]): Iterator[Term] =
    val directionDeltas = directions.iterator.map(_.getDelta).map(d => Struct.tuple(d.x, d.y))

    Iterator.single(Struct.of("deltas", directionDeltas.toList))
