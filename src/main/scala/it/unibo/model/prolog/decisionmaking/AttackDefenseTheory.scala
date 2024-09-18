package it.unibo.model.prolog.decisionmaking

import scala.jdk.CollectionConverters.*
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.GridCellsProvider
import it.unibo.model.prolog.PrologUtils.given
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.prolog.SolverType

enum AttackDefense:
  case Attack
  case Defense

final case class AttackDefenseTheory(
    private val grid: Grid,
    private val myTowerPosition: Set[Position],
    private val enemyTowerPosition: Set[Position],
    private val botBehaviour: BotBehaviour
)

object AttackDefenseTheory extends GridCellsProvider:
  def apply(
      grid: Grid,
      myTowerPosition: Set[Position],
      enemyTowerPosition: Set[Position],
      botBehaviour: BotBehaviour
  ): Theory =
    val cellIterator               = getCells(grid)
    val myTowerPositionIterator    = getTowerPositions(myTowerPosition, false)
    val enemyTowerPositionIterator = getTowerPositions(enemyTowerPosition, true)
    val allIterators = cellIterator ++ myTowerPositionIterator ++ enemyTowerPositionIterator

    val theory = Theory.fromPrologList(Struct.list(allIterators.asJava))

    val biasVariable = Struct.of("biasFactor", botBehaviour.biasFactor)
    val biasTheory   = Theory.fromPrologList(Struct.list(Iterator.single(biasVariable).asJava))
    theory.append(biasTheory)
    theory.append(SolverType.DistanceSolver)
    theory.append(SolverType.ManhattanSolver)

    theory

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
