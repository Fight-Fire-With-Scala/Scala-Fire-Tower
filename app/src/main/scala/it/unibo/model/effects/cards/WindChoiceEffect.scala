package it.unibo.model.effects.cards

import it.unibo.model.effects._
import it.unibo.model.effects.core.GameEffectResolver
import it.unibo.model.effects.core.IGameChoiceEffect
import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.effects.core.ILogicEffect
import it.unibo.model.gameboard.Dice
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

enum WindChoiceEffect extends IGameChoiceEffect:
  case UpdateWind(direction: Direction)
  case RandomUpdateWind
  case PlaceFire(direction: Direction) extends WindChoiceEffect with IGameEffect

object WindChoiceEffect:
  private val dice: Dice[Direction] = Dice(Direction.values.toSeq, 42L)

  private def resolveWindChoice(direction: Direction) =
    GameEffectResolver { (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val newBoard = gb.board.copy(windDirection = direction)
      GameBoardEffect(gbe.gameBoard.copy(board = newBoard))
    }

  def getPlaceFireEffect(direction: Direction): ILogicEffect = ILogicEffect(
    pattern = VerySmallEffect(Map("a" -> Fire)).compilePattern,
    goals = List(Rule("fire")),
    directions = List(direction)
  )

  val windChoiceResolver: GameEffectResolver[WindChoiceEffect, IGameEffect] = GameEffectResolver:
    case UpdateWind(direction) => resolveWindChoice(direction)
    case RandomUpdateWind      => resolveWindChoice(dice.roll())
    case PlaceFire(direction)  => getPlaceFireEffect(direction)
