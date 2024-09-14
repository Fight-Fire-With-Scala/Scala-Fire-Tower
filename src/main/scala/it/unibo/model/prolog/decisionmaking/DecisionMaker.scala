package it.unibo.model.prolog.decisionmaking

import scala.jdk.CollectionConverters._

import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Aggressive
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.logger
import it.unibo.model.prolog.PrologEngine
import it.unibo.model.prolog.PrologUtils.given
import it.unibo.model.prolog.PrologUtils.given_Conversion_SolverType_Theory
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term
import it.unibo.model.prolog.SolverType
import it.unibo.model.prolog.SolverType.DistanceSolver
import it.unibo.model.prolog.SolverType.ManhattanSolver

object DecisionMaker:
  private var attackOrDefense: AttackDefense = AttackDefense.Attack
  private var objectiveTower: Position = Position(0, 0)
  
  def getAttackOrDefense: AttackDefense = attackOrDefense
  def getObjectiveTower: Position = objectiveTower

  def computeAttackOrDefense(gameBoard: GameBoard, botBehaviour: BotBehaviour): Unit =
    val myTowerPositions = gameBoard.getCurrentPlayer.towerPositions.map(_.position)
    logger.info(myTowerPositions.toString())
    val opponentPositions = gameBoard.getOpponent.towerPositions.map(_.position)
    logger.info(opponentPositions.toString())
    val theory = AttackDefenseTheory(gameBoard.board.grid, myTowerPositions, opponentPositions)
    val biasVariable = Struct.of("biasFactor", botBehaviour.biasFactor)
    val biasTheory = Theory.fromPrologList(Struct.list(Iterator.single(biasVariable).asJava))
    theory.append(biasTheory)
    theory.append(SolverType.DistanceSolver)
    theory.append(SolverType.ManhattanSolver)

    val engine = PrologEngine(theory)
    val goal = "closest_tower_to_fire(ClosestTower)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) =>
        val closestTower = solution.getTerm("ClosestTower").toString
        objectiveTower = Position(
          closestTower.substring(1, closestTower.indexOf(',')).toInt,
          closestTower.substring(closestTower.indexOf(',') + 1, closestTower.length - 1).toInt
        )
        attackOrDefense =
          if myTowerPositions.contains(objectiveTower) then AttackDefense.Defense
          else AttackDefense.Attack
      case None           => botBehaviour match
          case Aggressive => attackOrDefense = AttackDefense.Attack
          case _          => attackOrDefense = AttackDefense.Defense
