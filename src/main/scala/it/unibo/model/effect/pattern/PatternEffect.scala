package it.unibo.model.effect.pattern

import it.unibo.model.effect.{ GameBoardEffect, MoveEffect }
import it.unibo.model.effect.MoveEffect.{ logBotChoice, logCardChosen, logPatternApplied, logPatternChosen, runIfLastMoveFound }
import it.unibo.model.effect.card.CardManager
import it.unibo.model.effect.core.*
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.effect.phase.PhaseEffect.updatePlayer
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.player.PlayerManager
import it.unibo.model.logger

enum PatternEffect extends IGameEffect:
  case PatternComputation(logicEffect: ILogicEffect)
  case CardComputation(cardId: Int, logicEffect: ILogicEffect)
  case BotComputation(cards: Map[Option[Int], List[ILogicEffect]])
  case PatternApplication(pattern: Map[Position, Token])
  case ResetPatternComputation

object PatternEffect extends CardManager with PlayerManager with LogicSolverManager:
  private def solvePatternComputation(logicEffect: ILogicEffect) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb                = gbe.gameBoard
      val availablePatterns = computePatterns(gb, None, logicEffect)
      logPatternChosen(gb, availablePatterns)

  private def solveCardsComputation(cards: Map[Option[Int], List[ILogicEffect]]) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb                      = gbe.gameBoard
      val (cardId, chosenPattern) = computePatterns(gb, cards)
      cardId match
        case Some(id) => logBotChoice(gb, id, chosenPattern)
        case None     => gb

  private def solveCardComputation(cardId: Int, logicEffect: ILogicEffect) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb                = gbe.gameBoard
      val currentPlayer     = gb.getCurrentPlayer
      val availablePatterns = computePatterns(gb, Some(cardId), logicEffect)
      val cardOpt           = currentPlayer.hand.find(_.id == cardId)
      cardOpt match
        case Some(card) =>
          logCardChosen(gb, card, availablePatterns)
        case None =>
          currentPlayer.extraCard match
            case Some(card) =>
              logCardChosen(gb, card, availablePatterns)
            case None =>
              logger.warn(s"Could not find a card with id $cardId in hand")
              gb

  private def solvePatternApplication(pattern: Map[Position, Token]) =
    GameBoardEffectSolver: (gbe: GameBoardEffect) =>
      val gb      = gbe.gameBoard
      val b       = gb.board
      val newGrid = b.grid.setTokens(pattern.toSeq*)
      val newGb   = runIfLastMoveFound(gb, updateDeckAndHand).gameBoard
      logPatternApplied(newGb.copy(board = newGb.board.copy(grid = newGrid)), pattern)

  private def solvePatternReset() = GameBoardEffectSolver: (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    runIfLastMoveFound(gb, updatePlayer)

  val patternEffectSolver: GameEffectSolver[PatternEffect, GameBoardEffectSolver] =
    GameEffectSolver:
      case CardComputation(id, logicEffect) => solveCardComputation(id, logicEffect)
      case BotComputation(cards)            => solveCardsComputation(cards)
      case PatternComputation(logicEffect)  => solvePatternComputation(logicEffect)
      case PatternApplication(pattern)      => solvePatternApplication(pattern)
      case ResetPatternComputation          => solvePatternReset()
