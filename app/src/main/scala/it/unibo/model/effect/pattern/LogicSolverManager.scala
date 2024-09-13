package it.unibo.model.effect.pattern

import alice.tuprolog.Theory
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.GridTheory
import it.unibo.model.prolog.PrologEngine
import it.unibo.model.prolog.PrologUtils.given_Conversion_Rule_Term
import it.unibo.model.prolog.PrologUtils.given_Conversion_SolverType_Theory
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term
import it.unibo.model.prolog.SolverType
import it.unibo.model.prolog.SolverType.CardChoserSolver
import it.unibo.model.prolog.SolverType.CardSolver
import it.unibo.model.prolog.SolverType.ConcatListSolver
import it.unibo.model.prolog.SolverType.ManhattanSolver
import it.unibo.model.prolog.decisionmaking.AllCardsResultTheory

trait LogicSolverManager:
  protected def computePatterns(
      gb: GameBoard,
      cardId: Int,
      logicEffect: ILogicEffect
  ): Set[Map[Position, Token]] =
    val theory = GridTheory(gb.board.grid, Map(cardId -> List(logicEffect)))
    theory.append(SolverType.CardSolver)
    theory.append(SolverType.BaseSolver)
    val engine = PrologEngine(theory)
    logicEffect.goals.map(g => engine.solveAsPatterns(g(cardId))).reduce((a, b) => a.union(b))

  protected def computePatterns(
      gb: GameBoard,
      cards: Map[Int, List[ILogicEffect]]
  ): Map[Int, Set[Map[Position, Token]]] =
    val opponentPositions = gb.getOpponent.towerPositions.map(_.position)
    val enemyTower = gb.getOpponent.towerPositions.head.position
    val grid = gb.board.grid

    val dynamicTheory = AllCardsResultTheory(cards)
    val theory = GridTheory(grid, cards)

    val t = Theory
      .parseWithStandardOperators(s"tower_position((${enemyTower.row}, ${enemyTower.col})).")
    theory.append(SolverType.ManhattanSolver)
    theory.append(SolverType.ConcatListSolver)
    theory.append(dynamicTheory)
    theory.append(SolverType.CardChoserSolver)
    theory.append(SolverType.CardSolver)
    theory.append(SolverType.BaseSolver)

    val engine = PrologEngine(theory)
    val goal = "main(R)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) =>
        val allCardResults = solution.getTerm("R")
        Map.empty
      case None           => Map.empty
