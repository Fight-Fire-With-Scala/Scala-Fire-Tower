package it.unibo.model.prolog.decisionmaking

import alice.tuprolog.{Struct, Term, Theory}
import it.unibo.model.gameboard.grid.{Grid, Position}
import it.unibo.model.prolog.PrologUtils.{size, given}

import scala.jdk.CollectionConverters.*

enum AttackDefense:
  case Attack
  case Defense

final case class AttackDefenseTheory(
    private val grid: Grid,
    private val myTowerPosition: Set[Position],
    private val enemyTowerPosition: Set[Position]
)

object AttackDefenseTheory:
  def apply(grid: Grid, myTowerPosition: Set[Position], enemyTowerPosition: Set[Position]): Theory =
    val cellIterator = getCells(grid)
    val myTowerPositionIterator = getTowerPositions(myTowerPosition, false)
    val enemyTowerPositionIterator = getTowerPositions(enemyTowerPosition, true)
    val allIterators = cellIterator ++ myTowerPositionIterator ++ enemyTowerPositionIterator

    Theory.fromPrologList(Struct.list(allIterators.asJava))

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

  private def getTowerPositions(towerPosition: Set[Position], isOpponent: Boolean): Iterator[Term] =
    val towerPositionsIterator = towerPosition.iterator.map { pos =>
      Struct.of(
        if isOpponent then "enemyTowerPosition" else "towerPosition",
        Struct.tuple(pos.row, pos.col)
      )
    }.toSeq.sorted.iterator

    towerPositionsIterator
