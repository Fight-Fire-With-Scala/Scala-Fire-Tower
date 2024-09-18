package it.unibo.model.prolog

import scala.jdk.CollectionConverters.*
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Var
import alice.tuprolog.Theory
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.PrologUtils.{ defaultId, size, given }

final case class GridTheory(
    private val grid: Grid,
    private val patternsToCompute: Map[Option[Int], List[ILogicEffect]]
)

object GridTheory extends GridCellsProvider:
  given Conversion[ILogicEffect, List[ILogicEffect]] = List(_)

  def apply(grid: Grid, patternsToCompute: Map[Option[Int], List[ILogicEffect]]): Theory =
    val cellIterator      = getCells(grid)
    val patternIterator   = getPatterns(patternsToCompute)
    val directionIterator = getDirections(patternsToCompute)
    val deltaIterator     = getDeltas(patternsToCompute)
    val allIterators      = cellIterator ++ patternIterator ++ directionIterator ++ deltaIterator

    Theory.fromPrologList(Struct.list(allIterators.toList.asJava))

  private def getPatterns(patternsToCompute: Map[Option[Int], List[ILogicEffect]]): Iterator[Term] =
    patternsToCompute.iterator.flatMap: (cardId, ef) =>
      ef.flatMap(_.computations)
        .zipWithIndex
        .flatMap: (m, compId) =>
          m.pattern.map: (pos, token) =>
            cardId match
              case Some(id) => Struct.of("pattern", Struct.tuple(pos._1, pos._2), token, id, compId)
              case None =>
                Struct.of("pattern", Struct.tuple(pos._1, pos._2), token, defaultId, compId)

  private def getDirections(
      patternsToCompute: Map[Option[Int], List[ILogicEffect]]
  ): Iterator[Term] =
    patternsToCompute.iterator.flatMap: (cardId, ef) =>
      ef.flatMap(_.computations)
        .zipWithIndex
        .flatMap: (m, compId) =>
          val directionsOfApplication: List[Direction] =
            if (m.directions.size <= 1) List(Direction.North)
            else Direction.values.toList
          val directionNames = directionsOfApplication.map(_.getId)
          val struct = cardId match
            case Some(id) => Struct.of("directions", directionNames, id, compId)
            case None     => Struct.of("directions", directionNames, defaultId, compId)
          Iterator.single(struct)

  private def getDeltas(patternsToCompute: Map[Option[Int], List[ILogicEffect]]): Iterator[Term] =
    patternsToCompute.iterator.flatMap: (cardId, ef) =>
      ef.flatMap(_.computations)
        .zipWithIndex
        .flatMap: (m, compId) =>
          val directionDeltas = m.directions
            .map(_.getDelta)
            .map(d => Struct.tuple(d.row, d.col))
          val struct = cardId match
            case Some(id) => Struct.of("deltas", directionDeltas.toList, id, compId)
            case None     => Struct.of("deltas", directionDeltas.toList, defaultId, compId)
          Iterator.single(struct)
