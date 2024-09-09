package it.unibo

import PerformanceUtils.measure
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.{Direction, GameBoard, GamePhase}
import it.unibo.model.gameboard.player.{Bot, Person, Player}
import it.unibo.model.prolog.{PrologEngine, Rule}
import it.unibo.model.prolog.PrologProgram.distanceProgram
import it.unibo.model.prolog.decisionmaking.AttackDefenseTheory
import it.unibo.model.prolog.PrologUtils.given_Conversion_Rule_Term
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
    val myTowerPositions = gb.getCurrentPlayer().towerPositions.map(_.position)
    val opponentPositions = gb.getOpponent().towerPositions.map(_.position)
    val updatedGrid = grid.setToken(Position(6,7), Fire)
    val theory = AttackDefenseTheory(
      updatedGrid,
      myTowerPositions,
      opponentPositions
    )
    theory.append(distanceProgram)
    print(theory)
    val engine = PrologEngine(theory)
    val rule: Rule = Rule("closest_tower_to_fire")
    val res = engine.solve(rule).headOption
    res match
      case Some(value) => println(value.getBindingVars)
      case None => println("No solution found")

  @main
  def main(): Unit = println(s"Took ${measure(run()).duration.toSeconds} seconds")
