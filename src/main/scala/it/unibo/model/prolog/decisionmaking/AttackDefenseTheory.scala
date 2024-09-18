package it.unibo.model.prolog.decisionmaking

import scala.jdk.CollectionConverters.*
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.GridCellsProvider
import it.unibo.model.prolog.PrologUtils.given

enum AttackDefense:
  case Attack
  case Defense

final case class AttackDefenseTheory(
    private val grid: Grid,
    private val myTowerPosition: Set[Position],
    private val enemyTowerPosition: Set[Position]
)

object AttackDefenseTheory extends GridCellsProvider:
  def apply(grid: Grid, myTowerPosition: Set[Position], enemyTowerPosition: Set[Position]): Theory =
    val cellIterator               = getCells(grid)
    val myTowerPositionIterator    = getTowerPositions(myTowerPosition, false)
    val enemyTowerPositionIterator = getTowerPositions(enemyTowerPosition, true)
    val allIterators = cellIterator ++ myTowerPositionIterator ++ enemyTowerPositionIterator

    Theory.fromPrologList(Struct.list(allIterators.asJava))

  private def getTowerPositions(towerPosition: Set[Position], isOpponent: Boolean): Iterator[Term] =
    val towerPositionsIterator = towerPosition.iterator
      .map { pos =>
        Struct.of(
          if isOpponent then "enemyTowerPosition" else "towerPosition",
          Struct.tuple(pos.row, pos.col)
        )
      }
      .toSeq
      .iterator

    towerPositionsIterator
