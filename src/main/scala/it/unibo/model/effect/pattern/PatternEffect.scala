package it.unibo.model.effect.pattern

import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.MoveEffect.logCardChosen
import it.unibo.model.effect.MoveEffect.logBotChoice
import it.unibo.model.effect.MoveEffect.logPatternApplied
import it.unibo.model.effect.MoveEffect.logPatternChosen
import it.unibo.model.effect.core._
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.logger

enum PatternEffect extends IGameEffect:
  case PatternComputation(logicEffect: ILogicEffect)
  case CardComputation(cardId: Int, logicEffect: ILogicEffect)
  case BotComputation(cards: Map[Int, List[ILogicEffect]])
  case PatternApplication(pattern: Map[Position, Token])
  case ResetPatternComputation

object PatternEffect extends PatternManager with LogicSolverManager:
  private def solvePatternComputation(logicEffect: ILogicEffect) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb                = gbe.gameBoard
      val availablePatterns = computePatterns(gb, -1, logicEffect)
      logPatternChosen(gb, availablePatterns)

  private def solveCardsComputation(cards: Map[Int, List[ILogicEffect]]) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb            = gbe.gameBoard
      val chosenPattern = computePatterns(gb, cards)
      logBotChoice(gb, chosenPattern)

  private def solveCardComputation(cardId: Int, logicEffect: ILogicEffect) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb                = gbe.gameBoard
      val availablePatterns = computePatterns(gb, cardId, logicEffect)
      val cardOpt           = gb.getCurrentPlayer.hand.find(_.id == cardId)
      cardOpt
        .map(card => logCardChosen(gb, card, availablePatterns))
        .getOrElse:
          logger.warn(s"Could not find a card with id $cardId in hand")
          gb

  private def solvePatternApplication(pattern: Map[Position, Token]) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb      = gbe.gameBoard
      val b       = gb.board
      val newGrid = b.grid.setTokens(pattern.toSeq*)
      val newGb   = runIfLastCardChosenFound(gb, updateDeckAndHand).gameBoard
      logPatternApplied(newGb.copy(board = b.copy(grid = newGrid)), pattern)

  private def solvePatternReset() = GameBoardEffectSolver: (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    runIfLastCardChosenFound(gb, updatePlayer)

  val patternEffectSolver: GameEffectSolver[PatternEffect, GameBoardEffectSolver] =
    GameEffectSolver:
      case CardComputation(id, logicEffect) => solveCardComputation(id, logicEffect)
      case BotComputation(cards)            => solveCardsComputation(cards)
      case PatternComputation(logicEffect)  => solvePatternComputation(logicEffect)
      case PatternApplication(pattern)      => solvePatternApplication(pattern)
      case ResetPatternComputation          => solvePatternReset()
