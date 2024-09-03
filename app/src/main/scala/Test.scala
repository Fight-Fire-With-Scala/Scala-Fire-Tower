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

object Test:
  private val gb = GameBoard()

  private val board = Board(Grid.standard, None, List.empty, Direction.North)

  private val availablePatternsEffect = PatternComputationResolver(
    MediumEffect(Map("a" -> Fire)),
    Rule(Struct.of("fire", Var.of("R"))),
    Direction.values.toList
  )

  @main
  def main(): Unit =
    val b = board.applyEffect(Some(availablePatternsEffect.getAvailableMoves(board)))
    gb.copy(gamePhase = WindPhase, board = b)
    println(b.availablePatterns)
