package it.unibo.model.prolog

import scala.jdk.CollectionConverters.*
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.PrologUtils.given
import it.unibo.model.prolog.PrologUtils.size

final case class GridTheory(
    private val grid: Grid,
    private val patternsToCompute: Map[Int, List[ILogicEffect]]
)

object GridTheory extends GridCellsProvider:
  given Conversion[ILogicEffect, List[ILogicEffect]] = List(_)

  def apply(grid: Grid, patternsToCompute: Map[Int, List[ILogicEffect]]): Theory =
    val cellIterator      = getCells(grid)
    val patternIterator   = getPatterns(patternsToCompute)
    val directionIterator = getDirections(patternsToCompute)
    val deltaIterator     = getDeltas(patternsToCompute)
    val allIterators      = cellIterator ++ patternIterator ++ directionIterator ++ deltaIterator

    Theory.fromPrologList(Struct.list(allIterators.toList.asJava))

  private def getPatterns(patternsToCompute: Map[Int, List[ILogicEffect]]): Iterator[Term] =
    patternsToCompute.iterator.flatMap: (cardId, ef) =>
      ef.flatMap(_.computations)
        .zipWithIndex
        .flatMap: (m, compId) =>
          m.pattern.map: (pos, token) =>
            Struct.of("pattern", Struct.tuple(pos._1, pos._2), token, cardId, compId)

  private def getDirections(patternsToCompute: Map[Int, List[ILogicEffect]]): Iterator[Term] =
    patternsToCompute.iterator.flatMap: (cardId, ef) =>
      ef.flatMap(_.computations)
        .zipWithIndex
        .flatMap: (m, compId) =>
          val directionsOfApplication: List[Direction] =
            if (m.directions.size <= 1) List(Direction.North)
            else Direction.values.toList
          val directionNames = directionsOfApplication.map(_.getId)
          Iterator.single(Struct.of("directions", directionNames, cardId, compId))

  private def getDeltas(patternsToCompute: Map[Int, List[ILogicEffect]]): Iterator[Term] =
    patternsToCompute.iterator.flatMap: (cardId, ef) =>
      ef.flatMap(_.computations)
        .zipWithIndex
        .flatMap: (m, compId) =>
          val directionDeltas = m.directions
            .map(_.getDelta)
            .map(d => Struct.tuple(d.row, d.col))
          Iterator.single(Struct.of("deltas", directionDeltas.toList, cardId, compId))
