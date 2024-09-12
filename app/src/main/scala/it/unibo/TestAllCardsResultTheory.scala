package it.unibo

import alice.tuprolog.Theory
import it.unibo.PerformanceUtils.measure
import it.unibo.model.effects.cards.BucketEffect
import it.unibo.model.effects.cards.FireEffect
import it.unibo.model.effects.cards.FirebreakEffect
import it.unibo.model.effects.cards.WindEffect
import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.effects.core.ILogicEffect
import it.unibo.model.gameboard.Deck
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.player.Person
import it.unibo.model.gameboard.player.Player
import it.unibo.model.prolog.GridTheory
import it.unibo.model.prolog.PrologEngine
import it.unibo.model.prolog.PrologProgram.cardsProgram
import it.unibo.model.prolog.PrologProgram.choseCardProgram
import it.unibo.model.prolog.PrologProgram.concatListsProgram
import it.unibo.model.prolog.PrologProgram.manhattanDistance
import it.unibo.model.prolog.PrologProgram.solverProgram
import it.unibo.model.prolog.PrologUtils.given
import it.unibo.model.prolog.decisionmaking.AllCardsResultTheory

object TestAllCardsResultTheory:
  private val deck = Deck("cards.yaml")
  private val enemy: Person = Person("tone", List.empty, List.empty)
  val player: Player = Player.bot(Balanced)
  val (card1, newDeck) = deck.drawCard()
  private val newPlayer = player.drawCardFromDeck(card1.get)
  val (card2, newDeck2) = newDeck.drawCard()
  private val newPlayer2 = newPlayer.drawCardFromDeck(card2.get)
  private val gb = GameBoard(enemy, newPlayer2)
  private val newGb = gb.changePlayer()

  private def resolveEffect(cardId: Int, effect: IGameEffect): ILogicEffect = effect match
    case FireEffect.Explosion      => FireEffect.fireEffectResolver.resolve(FireEffect.Explosion)
    case FireEffect.Flare          => FireEffect.fireEffectResolver.resolve(FireEffect.Flare)
    case FireEffect.BurningSnag    => FireEffect.fireEffectResolver.resolve(FireEffect.BurningSnag)
    case FireEffect.Ember          => FireEffect.fireEffectResolver.resolve(FireEffect.Ember)
    case FirebreakEffect.DozerLine => FirebreakEffect.fireBreakEffectResolver
        .resolve(FirebreakEffect.DozerLine)
    case FirebreakEffect.ScratchLine => FirebreakEffect.fireBreakEffectResolver
        .resolve(FirebreakEffect.ScratchLine)
    case FirebreakEffect.DeReforest  => FirebreakEffect.fireBreakEffectResolver
        .resolve(FirebreakEffect.DeReforest)
    case BucketEffect                => BucketEffect.bucketEffect
    case WindEffect.North            => WindEffect.windEffectResolver.resolve(WindEffect.North)
    case WindEffect.South            => WindEffect.windEffectResolver.resolve(WindEffect.South)
    case WindEffect.East             => WindEffect.windEffectResolver.resolve(WindEffect.East)
    case WindEffect.West             => WindEffect.windEffectResolver.resolve(WindEffect.West)
    case _                           => throw new MatchError(s"Unmatched effect: $effect")
  private def run(): Unit =
    val opponentPositions = gb.getOpponent.towerPositions.map(_.position)
    val enemyTower = gb.getOpponent.towerPositions.head.position
    val grid = gb.board.grid

    // Create the map with the logic effects of the two cards
    val map: Map[Int, List[ILogicEffect]] = Map(
      card1.get.id -> List(resolveEffect(card1.get.id, card1.get.effect)),
      card2.get.id -> List(resolveEffect(card2.get.id, card2.get.effect))
    )

    // Generate the Prolog theory
    val dynamicTheory = AllCardsResultTheory(map)
    val theory = GridTheory(grid, map)

    theory.append(Theory.parseLazilyWithStandardOperators(
      s"tower_position((${enemyTower.row}, ${enemyTower.col}))."
    ))
    theory.append(manhattanDistance)
    theory.append(concatListsProgram)
    theory.append(dynamicTheory)
    theory.append(choseCardProgram)
    theory.append(cardsProgram)
    theory.append(solverProgram)
    println(theory)
    val engine = PrologEngine(theory)
    val goal = "main(R)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) =>
        val allCardResults = solution.getTerm("R").toString
        println(s"All card results: $allCardResults")
      case None           => println("No solution found")

  @main
  def main(): Unit = println(s"Took ${measure(run()).duration.toSeconds} seconds")
