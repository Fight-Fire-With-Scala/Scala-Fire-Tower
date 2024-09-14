package it.unibo.model.effect.card

import it.unibo.model.effect._
import it.unibo.model.effect.core.GameBoardEffectResolver
import it.unibo.model.effect.core.GameEffectResolver
import it.unibo.model.effect.core.IGameChoiceEffect
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.Dice
import it.unibo.model.gameboard.Direction

enum WindUpdateEffect extends IGameChoiceEffect:
  case UpdateWind(direction: Direction)
  case RandomUpdateWind

object WindUpdateEffect:
  private val dice: Dice[Direction] = Dice(Direction.values.toSeq, 42L)

  private def resolveWindChoice(direction: Direction) =
    GameBoardEffectResolver { (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val newBoard = gb.board.copy(windDirection = direction)
      gbe.gameBoard.copy(board = newBoard)
    }

  val windChoiceResolver: GameEffectResolver[WindUpdateEffect, GameBoardEffectResolver] =
    GameEffectResolver:
      case UpdateWind(direction) => resolveWindChoice(direction)
      case RandomUpdateWind      => resolveWindChoice(dice.roll())
