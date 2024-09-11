package it.unibo.view.components.game.gameboard.grid.handler

import it.unibo.controller.*
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.effects.PatternEffect.PatternApplication
import it.unibo.model.effects.cards.{FireEffect, FirebreakEffect, WaterEffect, WindEffect}
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.{
  DecisionPhase,
  PlayStandardCardPhase,
  WaitingPhase,
  WindPhase
}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.components.game.gameboard.grid.{GridSquare, GridState}
import it.unibo.view.logger

import scala.collection.mutable

class GridClickHandler(
    observableSubject: ViewSubject,
    internalObservable: InternalViewSubject,
    squareMap: mutable.Map[Position, GridSquare],
    gridState: GridState
):
  def handleCellClick(row: Int, col: Int, gamePhase: GamePhase): Unit =
    val position = Position(row, col)
    gamePhase match
      case WindPhase             => if (gridState.hoveredCells.contains(position))
          placePattern(gridState.availablePatterns.find(_.contains(position)).get, WaitingPhase)
      case PlayStandardCardPhase => handleCardPlay(position)
      case _                     =>

  private def handleCardPlay(position: Position): Unit =
    if gridState.hoveredCells.contains(position) then
      if isSinglePatternAvailable then placeSinglePattern(position)
      else if isExplosionPattern then placeExplosionPattern(position)
      else if gridState.fixedCell.nonEmpty then placeFixedPattern(position)
      else activateFixedCellMode(position)
    else if gridState.fixedCell.nonEmpty && gridState.fixedCell.contains(position) then
      deactivateFixedCellMode(position)

  private def isExplosionPattern: Boolean = gridState.effectCode == FireEffect.Explosion.effectId ||
    gridState.effectCode == WaterEffect.SmokeJumper.effectId

  private def isSinglePatternAvailable: Boolean = gridState.effectCode ==
    WindEffect.North.effectId || gridState.effectCode == WindEffect.South.effectId ||
    gridState.effectCode == WindEffect.East.effectId ||
    gridState.effectCode == WindEffect.West.effectId ||
    gridState.effectCode == FirebreakEffect.DeReforest.effectId

  private def placeExplosionPattern(position: Position): Unit = gridState.effectCode match
    case FireEffect.Explosion.effectId    => placePattern(
        gridState.availablePatterns
          .find(_.exists((pos, tkn) => pos == position && tkn == Firebreak)).get,
        DecisionPhase
      )
    case WaterEffect.SmokeJumper.effectId => placePattern(
        gridState.availablePatterns.find(_.exists((pos, tkn) => pos == position && tkn == Fire))
          .get,
        DecisionPhase
      )
    case _                                => logger.error("Error in explosion pattern")

  private def placeSinglePattern(position: Position): Unit =
    val pattern = gridState.availablePatterns.find(_.contains(position)).get
    placePattern(pattern, DecisionPhase)

  private def placeFixedPattern(position: Position): Unit =
    gridState.fixedCell.clear()
    val pattern = gridState.availablePatternsClickFixed.find(_.contains(position)).get
    placePattern(pattern, DecisionPhase)

  private def activateFixedCellMode(position: Position): Unit =
    val pattern = gridState.availablePatterns.find(_.contains(position)).get
    gridState.availablePatternsClickFixed = gridState.availablePatterns.filter(_.contains(position))
    runOnUIThread {
      gridState.fixedCell += position -> gridState.hoveredCells(position)
      squareMap(position).updateColor(pattern(position).color.deriveColor(1, 1, 1, 0.7))
      gridState.hoveredCells.clear()
    }

  private def deactivateFixedCellMode(position: Position): Unit =
    runOnUIThread {
      squareMap(position).updateColor(gridState.fixedCell(position))
      gridState.fixedCell.clear()
    }
    gridState.resetHoverColors()
    gridState.availablePatternsClickFixed = gridState.availablePatterns

  private def placePattern(pattern: Map[Position, Token], newPhase: GamePhase): Unit =
    observableSubject.onNext(ResolvePatternChoice(PatternApplication(pattern)))
    internalObservable.onNext(UpdateGamePhaseView(newPhase))
    observableSubject.onNext(UpdateGamePhaseModel(PhaseEffect(newPhase)))
    gridState.hoveredCells.clear()
