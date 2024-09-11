package it.unibo

import PerformanceUtils.measure
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.{Person, Player}
import it.unibo.model.prolog.PrologEngine
import it.unibo.model.prolog.PrologProgram.distanceProgram
import it.unibo.model.prolog.decisionmaking.AttackDefenseTheory
import it.unibo.model.prolog.decisionmaking.AttackDefense
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term
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
    val myTowerPositions = gb.getCurrentPlayer.towerPositions.map(_.position)
    val opponentPositions = gb.getOpponent.towerPositions.map(_.position)
    println(myTowerPositions)
    println(opponentPositions)
    val updatedGrid = grid.setToken(Position(6,7), Fire).setToken(Position(9,8), Fire).setToken(Position(10,10), Fire)
    val theory = AttackDefenseTheory(
      updatedGrid,
      myTowerPositions,
      opponentPositions
    )
    theory.append(distanceProgram)
    //print(theory)
    val engine = PrologEngine(theory)
    val goal = "closest_tower_to_fire(ClosestTower)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) =>
        val closestTower = solution.getTerm("ClosestTower").toString
        val towerPosition = Position(
          closestTower.substring(1, closestTower.indexOf(',')).toInt,
          closestTower.substring(closestTower.indexOf(',') + 1, closestTower.length - 1).toInt
        )
        val attackDefense = if myTowerPositions.contains(towerPosition) then AttackDefense.Defense else AttackDefense.Attack
        val resultTuple = (attackDefense, towerPosition)
        println(s"Result: $resultTuple")
      case None =>
        println("No solution found")

  @main
  def main(): Unit = println(s"Took ${measure(run()).duration.toSeconds} seconds")