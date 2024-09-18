package it.unibo.model.prolog.decisionmaking

import scala.jdk.CollectionConverters.*
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Aggressive
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.logger
import it.unibo.model.prolog.PrologEngine
import it.unibo.model.prolog.PrologUtils.{ given_Conversion_SolverType_Theory, given_Conversion_String_Term, parseClosestTowerPosition, given }
import it.unibo.model.prolog.SolverType
import it.unibo.model.prolog.SolverType.DistanceSolver
import it.unibo.model.prolog.SolverType.ManhattanSolver

object DecisionMaker:
  private var attackOrDefense: AttackDefense = AttackDefense.Attack
  private var objectiveTower: Set[Position]  = Set(Position(0, 0))

  def getAttackOrDefense: AttackDefense                = attackOrDefense
  def getObjectiveTower: Set[Position]                 = objectiveTower
  def setObjectiveTower(position: Set[Position]): Unit = objectiveTower = position

  def computeAttackOrDefense(gameBoard: GameBoard, botBehaviour: BotBehaviour): Unit =
    val myTowerPositions = gameBoard.getCurrentPlayer.towerPositions.map(_.position)
    logger.info(myTowerPositions.toString())
    val opponentPositions = gameBoard.getOpponent.towerPositions.map(_.position)
    logger.info(opponentPositions.toString())
    val theory =
      AttackDefenseTheory(gameBoard.board.grid, myTowerPositions, opponentPositions, botBehaviour)

    val engine = PrologEngine(theory)
    val goal   = "closest_tower_to_fire(ClosestTower)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) =>
        objectiveTower = parseClosestTowerPosition(solution)
        attackOrDefense =
          if myTowerPositions.contains(objectiveTower.head) then AttackDefense.Defense
          else AttackDefense.Attack
      case None =>
        botBehaviour match
          case Aggressive => attackOrDefense = AttackDefense.Attack
          case _          => attackOrDefense = AttackDefense.Defense
