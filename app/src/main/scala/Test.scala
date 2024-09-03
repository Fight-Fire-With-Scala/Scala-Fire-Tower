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
  private val gb = GameBoard()

  private val board = Board(Grid.standard, None, List.empty, Direction.North)

  private val availablePatternsEffect = PatternComputationResolver(
    MediumEffect(Map("a" -> Fire)),
    Rule(Struct.of("fire", Var.of("R"))),
    Direction.values.toList
  )

  private def run(): Unit =
    val b = board.applyEffect(Some(availablePatternsEffect.getAvailableMoves(board)))
    gb.copy(gamePhase = WindPhase, board = b)
    println(b.availablePatterns)

  @main
  def main(): Unit = println(s"Took ${measure(run()).duration.toSeconds} seconds")
