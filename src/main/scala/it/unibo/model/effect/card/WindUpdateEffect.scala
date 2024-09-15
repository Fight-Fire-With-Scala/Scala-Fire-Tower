package it.unibo.model.effect.card

import it.unibo.model.effect._
import it.unibo.model.effect.core.GameBoardEffectSolver
import it.unibo.model.effect.core.GameEffectSolver
import it.unibo.model.effect.core.IGameChoiceEffect
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.Direction

enum WindUpdateEffect extends IGameChoiceEffect:
  case UpdateWind(direction: Direction)
  case RandomUpdateWind(direction: Direction)

object WindUpdateEffect:
  private def solveWindChoice(direction: Direction) =
    GameBoardEffectSolver { (gbe: GameBoardEffect) =>
      val gb       = gbe.gameBoard
      val newBoard = gb.board.copy(windDirection = direction)
      gbe.gameBoard.copy(board = newBoard)
    }

  val windChoiceSolver: GameEffectSolver[WindUpdateEffect, GameBoardEffectSolver] =
    GameEffectSolver:
      case UpdateWind(direction)       => solveWindChoice(direction)
      case RandomUpdateWind(direction) => solveWindChoice(direction)
