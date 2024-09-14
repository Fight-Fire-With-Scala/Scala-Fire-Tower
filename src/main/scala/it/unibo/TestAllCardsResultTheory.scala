//package it.unibo
//
//import java.util.concurrent.TimeUnit
//import scala.concurrent.duration.FiniteDuration
//import alice.tuprolog.Theory
//import it.unibo.model.effect.card.BucketEffect
//import it.unibo.model.effect.card.FireEffect
//import it.unibo.model.effect.card.FirebreakEffect
//import it.unibo.model.effect.card.WindEffect
//import it.unibo.model.effect.core.IGameEffect
//import it.unibo.model.effect.core.ILogicEffect
//import it.unibo.model.gameboard.Deck
//import it.unibo.model.gameboard.GameBoard
//import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
//import it.unibo.model.gameboard.grid.{Position, Token}
//import it.unibo.model.gameboard.player.Person
//import it.unibo.model.gameboard.player.Player
//import it.unibo.model.prolog.GridTheory
//import it.unibo.model.prolog.PrologEngine
//import it.unibo.model.prolog.PrologUtils.given_Conversion_SolverType_Theory
//import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term
//import it.unibo.model.prolog.PrologUtils
//import it.unibo.model.prolog.SolverType
//import it.unibo.model.prolog.decisionmaking.AllCardsResultTheory
//
//object TestAllCardsResultTheory:
//  object PerformanceUtils:
//    case class MeasurementResults[T](result: T, duration: FiniteDuration)
//        extends Ordered[MeasurementResults[?]]:
//      override def compare(that: MeasurementResults[?]): Int = duration.toNanos
//        .compareTo(that.duration.toNanos)
//
//    private def measure[T](msg: String)(expr: => T): MeasurementResults[T] =
//      val startTime = System.nanoTime()
//      val res = expr
//      val duration = FiniteDuration(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
//      if (msg.nonEmpty) println(s"${duration.toNanos} nanos")
//      MeasurementResults(res, duration)
//
//    def measure[T](expr: => T): MeasurementResults[T] = measure("")(expr)
//
//  private val deck = Deck("cards.yaml")
//  private val enemy: Person = Person("tone", List.empty, List.empty)
//  val player: Player = Player.bot(Balanced)
//  val (card1, newDeck) = deck.drawCard()
//  private val newPlayer = player.drawCardFromDeck(card1.get)
//  val (card2, newDeck2) = newDeck.drawCard()
//  private val newPlayer2 = newPlayer.drawCardFromDeck(card2.get)
//  private val gb = GameBoard(enemy, newPlayer2)
//  private val newGb = gb.changePlayer()
//
//  private def resolveEffect(cardId: Int, effect: IGameEffect): ILogicEffect = effect match
//    case FireEffect.Explosion      => FireEffect.fireEffectResolver.resolve(FireEffect.Explosion)
//    case FireEffect.Flare          => FireEffect.fireEffectResolver.resolve(FireEffect.Flare)
//    case FireEffect.BurningSnag    => FireEffect.fireEffectResolver.resolve(FireEffect.BurningSnag)
//    case FireEffect.Ember          => FireEffect.fireEffectResolver.resolve(FireEffect.Ember)
//    case FirebreakEffect.DozerLine => FirebreakEffect.fireBreakEffectResolver
//        .resolve(FirebreakEffect.DozerLine)
//    case FirebreakEffect.ScratchLine => FirebreakEffect.fireBreakEffectResolver
//        .resolve(FirebreakEffect.ScratchLine)
//    case FirebreakEffect.DeReforest  => FirebreakEffect.fireBreakEffectResolver
//        .resolve(FirebreakEffect.DeReforest)
//    case BucketEffect                => BucketEffect.bucketEffect
//    case WindEffect.North            => WindEffect.windEffectResolver.resolve(WindEffect.North)
//    case WindEffect.South            => WindEffect.windEffectResolver.resolve(WindEffect.South)
//    case WindEffect.East             => WindEffect.windEffectResolver.resolve(WindEffect.East)
//    case WindEffect.West             => WindEffect.windEffectResolver.resolve(WindEffect.West)
//    case _                           => throw new MatchError(s"Unmatched effect: $effect")
//
//  private def run(): Unit =
//    val opponentPositions = gb.getOpponent.towerPositions.map(_.position)
//    val enemyTower = gb.getOpponent.towerPositions.head.position
//    val grid = gb.board.grid
//
//    // Create the map with the logic effects of the two cards
//    val map: Map[Int, List[ILogicEffect]] = Map(
//      card1.get.id -> List(resolveEffect(card1.get.id, card1.get.effect)),
//      card2.get.id -> List(resolveEffect(card2.get.id, card2.get.effect))
//    )
//
//    // Generate the Prolog theory
//    val dynamicTheory = AllCardsResultTheory(map)
//    val theory = GridTheory(grid, map)
//
//    theory.append(Theory.parseLazilyWithStandardOperators(
//      s"tower_position((${enemyTower.row}, ${enemyTower.col}))."
//    ))
//    theory.append(SolverType.ManhattanSolver)
//    theory.append(SolverType.ConcatListSolver)
//    theory.append(dynamicTheory)
//    theory.append(SolverType.CardChoserSolver)
//    theory.append(SolverType.CardSolver)
//    theory.append(SolverType.BaseSolver)
//    println(theory)
//    val engine = PrologEngine(theory)
//    val goal = "main(R)"
//    val result = engine.solve(goal).headOption
//
//    result match
//      case Some(solution) =>
//        val cardResults = PrologUtils.parseAllCardsResult(solution)
//        println(s"Parsed card results: $cardResults")
//      case None           => println("No solution found")
//
//  @main
//  def main(): Unit = println(s"Took ${PerformanceUtils.measure(run()).duration.toSeconds} seconds")
