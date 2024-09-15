package it.unibo.model.effect.phase

import scala.annotation.tailrec
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.effect.card.WindEffect.given_Conversion_Direction_WindEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.player.Person
import it.unibo.model.gameboard.player.Player
import it.unibo.model.logger

trait PhaseManager:
  @tailrec
  final def updateGamePhase(gb: GameBoard, choice: GamePhase): GameBoard = choice match
    case WindPhase =>
      gb.getCurrentPlayer match
        case p: Person => handleWindPhase(gb)
        case _         => gb
    case WaitingPhase          => gb.copy(gamePhase = WaitingPhase)
    case PlayStandardCardPhase => gb.copy(gamePhase = PlayStandardCardPhase)
    case RedrawCardsPhase      => gb.copy(gamePhase = RedrawCardsPhase)
    case DecisionPhase =>
      gb.getCurrentPlayer match
        case p: Person => updateGamePhase(gb, getNextPhaseAfterDecisionPhase(gb))
        case _         => gb.copy(gamePhase = DecisionPhase)
    case PlaySpecialCardPhase => gb.copy(gamePhase = PlaySpecialCardPhase)
    case EndTurnPhase         => updateGamePhase(handleTurnEnd(gb), WindPhase)

  private def handleTurnEnd(gb: GameBoard) = gb
    .copy(gamePhase = WindPhase, turnNumber = gb.turnNumber + 1)
    .changePlayer()

  private def getNextPhaseAfterDecisionPhase(gb: GameBoard) = gb.getCurrentPlayer.extraCard match
    case Some(_) => PlaySpecialCardPhase
    case None    => EndTurnPhase

  private def handleWindPhase(gb: GameBoard) =
    val direction          = gb.board.windDirection
    val logicEffect        = WindEffect.windEffectSolver.solve(direction)
    val patternComputation = PatternEffect.PatternComputation(logicEffect)
    val gbEffectSolver     = PatternEffect.patternEffectSolver.solve(patternComputation)
    val newGb              = gbEffectSolver.solve(GameBoardEffect(gb)).gameBoard
    newGb
