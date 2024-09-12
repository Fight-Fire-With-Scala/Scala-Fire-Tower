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
import it.unibo.model.prolog.PrologProgram.distanceProgram
import it.unibo.model.prolog.PrologProgram.manhattanDistance
import it.unibo.model.prolog.PrologUtils.given
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term

object DecisionMaker:
  private var objectiveTower: Position = Position(0, 0)
  private var attackOrDefense: AttackDefense = AttackDefense.Attack

  def getObjectiveTower: Position = objectiveTower
  def getAttackOrDefense: AttackDefense = attackOrDefense

  def computeAttackOrDefense(gameBoard: GameBoard, botBehaviour: BotBehaviour): Unit =
    val myTowerPositions = gameBoard.getCurrentPlayer.towerPositions.map(_.position)
    logger.info(myTowerPositions.toString())
    val opponentPositions = gameBoard.getOpponent.towerPositions.map(_.position)
    logger.info(opponentPositions.toString())
    val theory = AttackDefenseTheory(gameBoard.board.grid, myTowerPositions, opponentPositions)
    val variable = Struct.of("biasFactor", botBehaviour.biasFactor)
    val theoryVariable = Theory.fromPrologList(Struct.list(Iterator.single(variable).asJava))
    theory.append(theoryVariable)
    theory.append(distanceProgram)
    theory.append(manhattanDistance)
    println(theory)
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
