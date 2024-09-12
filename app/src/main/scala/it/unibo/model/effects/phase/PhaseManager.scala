package it.unibo.model.effects.phase

import scala.annotation.tailrec

import it.unibo.model.effects.GameBoardEffect
import it.unibo.model.effects.PatternEffect
import it.unibo.model.effects.cards.WindChoiceEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase._

trait PhaseManager:
  @tailrec
  final def updateGamePhase(gb: GameBoard, choice: GamePhase): GameBoard = choice match
    case WindPhase             => handleWindPhase(gb)
    case WaitingPhase          => gb.copy(gamePhase = WaitingPhase)
    case PlayStandardCardPhase => gb.copy(gamePhase = PlayStandardCardPhase)
    case RedrawCardsPhase      => gb.copy(gamePhase = RedrawCardsPhase)
    case DecisionPhase         => updateGamePhase(gb, getNextPhaseAfterDecisionPhase(gb))
    case PlaySpecialCardPhase  => gb.copy(gamePhase = PlaySpecialCardPhase)
    case EndTurnPhase          => handleWindPhase(handleTurnEnd(gb))

  private def handleTurnEnd(gb: GameBoard) = gb
    .copy(gamePhase = WindPhase, turnNumber = gb.turnNumber + 1).changePlayer()

  private def getNextPhaseAfterDecisionPhase(gb: GameBoard) = gb.getCurrentPlayer.extraCard match
    case Some(_) => PlaySpecialCardPhase
    case None    => EndTurnPhase

  def handleWindPhase(gb: GameBoard): GameBoard =
    val direction = gb.board.windDirection
    val logicEffect = WindChoiceEffect.getPlaceFireEffect(direction)
    val patternComputation = PatternEffect.PatternComputation(logicEffect)
    val gbEffectResolver = PatternEffect.patternEffectResolver.resolve(patternComputation)
    val newGb = gbEffectResolver.resolve(GameBoardEffect(gb)).gameBoard
    newGb
