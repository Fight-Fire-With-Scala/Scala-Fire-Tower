package it.unibo

import PerformanceUtils.measure
import alice.tuprolog.Struct
import it.unibo.model.gameboard.{Direction, GameBoard, GamePhase}
import it.unibo.model.prolog.Rule
import alice.tuprolog.Var
import it.unibo.model.cards.effects.MediumEffect
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.player.{Bot, Person, Player}
import it.unibo.model.prolog.decisionmaking.AttackDefenseTheory

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

object PerformanceUtils:
  case class MeasurementResults[T](result: T, duration: FiniteDuration)
      extends Ordered[MeasurementResults[?]]:
    override def compare(that: MeasurementResults[?]): Int = duration.toNanos
      .compareTo(that.duration.toNanos)

  def measure[T](msg: String)(expr: => T): MeasurementResults[T] =
    val startTime = System.nanoTime()
    val res = expr
    val duration = FiniteDuration(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
    if (msg.nonEmpty) println(s"${duration.toNanos} nanos")
    MeasurementResults(res, duration)

  def measure[T](expr: => T): MeasurementResults[T] = measure("")(expr)

object Test:
  val player1: Person = Person("tone", List.empty, List.empty)
  val player2: Player = Player.bot
  private val gb = GameBoard(player1, player2)

  private def run(): Unit =
    val grid = gb.board.grid
    val myTowerPositions = gb.getCurrentPlayer().towerPositions
    val opponentPositions = gb.getOpponent().towerPositions
    val myTowerCells = grid.getTowerCells(myTowerPositions)
    val opponentTowerCells = grid.getTowerCells(opponentPositions)
    val theory = AttackDefenseTheory(
      grid,
      myTowerCells,
      opponentTowerCells
    )
    val a = 3

  @main
  def main(): Unit = println(s"Took ${measure(run()).duration.toSeconds} seconds")
