package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Theory}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Cell.EternalFire
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.model.prolog.PrologUtils.{size, given}

import scala.jdk.CollectionConverters.*

final case class GridTheory(
    private val grid: Grid,
    private val patternsAndDirections: Map[Int, (Map[Position, Token], List[Direction])]
)

object GridTheory:
  def apply(
      grid: Grid,
      patternsAndDirections: Map[Int, (Map[Position, Token], List[Direction])]
  ): Theory =
    val cellIterator = getCells(grid)
    val patternIterator = getPatterns(patternsAndDirections)
    val directionIterator = getDirections(patternsAndDirections)
    val deltaIterator = getDeltas(patternsAndDirections)
    val allIterators = cellIterator ++ patternIterator ++ directionIterator ++ deltaIterator

    Theory
      .fromPrologList(Struct.list(allIterators.toList.asJava.asInstanceOf[java.util.List[Term]]))

  private def getCells(grid: Grid): Iterator[Term] =
    val cells = grid.cells.iterator.map { case (pos, cell) =>
      Struct.of("cell", Struct.tuple(pos._1, pos._2), cell)
    }.toSeq.sorted.iterator

    val tokens = grid.tokens.iterator.map { case (pos, token) =>
      Struct.of("token", Struct.tuple(pos._1, pos._2), token)
    }

    val numRows = Iterator.single(Struct.of("numRows", grid.size))
    val numCols = Iterator.single(Struct.of("numCols", grid.size))

    cells ++ tokens ++ numRows ++ numCols

  private def getPatterns(
      patternsAndDirections: Map[Int, (Map[Position, Token], List[Direction])]
  ): Iterator[Term] = patternsAndDirections.iterator.flatMap { case (id, (pattern, _)) =>
    pattern.iterator.map { case (pos, token) =>
      Struct.of("pattern", Struct.tuple(pos._1, pos._2), token, id)
    }
  }

  private def getDirections(
      patternsAndDirections: Map[Int, (Map[Position, Token], List[Direction])]
  ): Iterator[Term] = patternsAndDirections.iterator.flatMap { case (id, (pattern, directions)) =>
    val directionsOfApplication: List[Direction] =
      if (pattern.size <= 1) List(Direction.North) else Direction.values.toList
    val directionNames = directionsOfApplication.map(_.getId)
    Iterator.single(Struct.of(
      "directions",
      Struct.list(directionNames.asJava.asInstanceOf[java.util.List[Term]]),
      id
    ))
  }

  private def getDeltas(
      patternsAndDirections: Map[Int, (Map[Position, Token], List[Direction])]
  ): Iterator[Term] = patternsAndDirections.iterator.flatMap { case (id, (_, directions)) =>
    val directionDeltas = directions.iterator.map(_.getDelta).map(d => Struct.tuple(d.row, d.col))
    Iterator.single(Struct.of(
      "deltas",
      Struct.list(directionDeltas.toList.asJava.asInstanceOf[java.util.List[Term]]),
      id
    ))
  }
