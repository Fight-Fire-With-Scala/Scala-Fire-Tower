package it.unibo.model.prolog.decisionmaking

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.PrologEngine
import it.unibo.model.prolog.PrologProgram.{distanceProgram, manhattanDistance}
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term

object DecisionMaker:
  private var objectiveTower: Position = Position(0, 0)
  private var attackOrDefense: AttackDefense = AttackDefense.Attack

  def getObjectiveTower: Position = objectiveTower
  def getAttackOrDefense: AttackDefense = attackOrDefense

  def computeAttackOrDefense(gameBoard: GameBoard): Unit =
    val myTowerPositions = gameBoard.getCurrentPlayer.towerPositions.map(_.position)
    println(myTowerPositions)
    val opponentPositions = gameBoard.getOpponent.towerPositions.map(_.position)
    println(opponentPositions)
    val theory = AttackDefenseTheory(gameBoard.board.grid, myTowerPositions, opponentPositions)
    theory.append(distanceProgram)
    theory.append(manhattanDistance)
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
      case None           => println("No solution found")
