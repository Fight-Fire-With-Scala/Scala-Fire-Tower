package it.unibo.model.effect.pattern

import alice.tuprolog.Theory
import it.unibo.model.effect.core.LogicEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.reasoner.{ GridTheory, ReasonerEngine, ReasonerUtils, SolverType }
import it.unibo.model.reasoner.ReasonerUtils.given_Conversion_Rule_Term
import it.unibo.model.reasoner.ReasonerUtils.given_Conversion_SolverType_Theory
import it.unibo.model.reasoner.ReasonerUtils.given_Conversion_String_Term
import it.unibo.model.reasoner.SolverType.CardChoserSolver
import it.unibo.model.reasoner.SolverType.CardSolver
import it.unibo.model.reasoner.SolverType.ConcatListSolver
import it.unibo.model.reasoner.SolverType.ManhattanSolver
import it.unibo.model.reasoner.decisionmaking.{ AllCardsResultTheory, DecisionMaker }
import it.unibo.model.gameboard.Pattern

trait LogicSolverManager:
  protected def computePatterns(
      gb: GameBoard,
      cardId: Option[Int],
      logicEffect: LogicEffect
  ): Set[Pattern] =
    val theory = GridTheory(gb.board.grid, Map(cardId -> List(logicEffect)))
    theory.append(SolverType.CardSolver)
    theory.append(SolverType.BaseSolver)

    val engine = ReasonerEngine(theory)
    logicEffect.computations.zipWithIndex
      .map((c, idx) => engine.solveAsPatterns(c.goal(cardId, Some(idx))))
      .reduce((a, b) => a.union(b))

  protected def computePatterns(
      gb: GameBoard,
      cards: Map[Option[Int], List[LogicEffect]]
  ): (Option[Int], Pattern) =

    val grid          = gb.board.grid
    val dynamicTheory = AllCardsResultTheory(cards)
    val theory        = GridTheory(grid, cards)

    DecisionMaker.getObjectiveTower.foreach(tower =>
      theory.append(
        Theory.parseWithStandardOperators(s"tower_position((${tower.row}, ${tower.col})).")
      )
    )
    theory.append(SolverType.ManhattanSolver)
    theory.append(SolverType.ConcatListSolver)
    theory.append(dynamicTheory)
    theory.append(SolverType.CardChoserSolver)
    theory.append(SolverType.CardSolver)
    theory.append(SolverType.BaseSolver)
    val engine = ReasonerEngine(theory)
    val goal   = "main(R)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) => ReasonerUtils.parseAllCardsResult(solution)
      case None           => (None, Map.empty)
