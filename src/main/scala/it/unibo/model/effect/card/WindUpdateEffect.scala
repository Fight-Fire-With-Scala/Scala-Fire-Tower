package it.unibo.model.effect.card

import it.unibo.model.effect.*
import it.unibo.model.effect.MoveEffect.runIfLastMoveFound
import it.unibo.model.effect.core.GameBoardEffectSolver
import it.unibo.model.effect.core.GameEffectSolver
import it.unibo.model.effect.core.GameChoiceEffect
import it.unibo.model.effect.hand.HandManager
import it.unibo.model.effect.pattern.PatternEffect.updateDeckAndHand
import it.unibo.model.gameboard.Direction

enum WindUpdateEffect extends GameChoiceEffect:
  case UpdateWind(direction: Direction)
  case RandomUpdateWind(direction: Direction)

object WindUpdateEffect extends CardManager with HandManager:
  private def solveWindChoice(direction: Direction) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb       = gbe.gameBoard
      val newBoard = gb.board.copy(windDirection = direction)
      runIfLastMoveFound(gb.copy(board = newBoard), updateDeckAndHand)

  val windChoiceSolver: GameEffectSolver[WindUpdateEffect, GameBoardEffectSolver] =
    GameEffectSolver:
      case UpdateWind(direction)       => solveWindChoice(direction)
      case RandomUpdateWind(direction) => solveWindChoice(direction)
