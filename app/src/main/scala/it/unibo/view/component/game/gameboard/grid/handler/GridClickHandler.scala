package it.unibo.view.component.game.gameboard.grid.handler

import scala.collection.mutable

import it.unibo.controller._
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.effect.pattern.PatternEffect.PatternApplication
import it.unibo.model.effect.card.FireEffect
import it.unibo.model.effect.card.FirebreakEffect
import it.unibo.model.effect.card.WaterEffect
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.DecisionPhase
import it.unibo.model.gameboard.GamePhase.PlayStandardCardPhase
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Firebreak
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.view.component.game.gameboard.grid.GridSquare
import it.unibo.view.component.game.gameboard.grid.GridState
import it.unibo.view.logger

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
    runOnUIThread:
      gridState.fixedCell += position -> gridState.hoveredCells(position)
      squareMap(position).updateColor(pattern(position).color.deriveColor(1, 1, 1, 0.7))
      gridState.hoveredCells.clear()

  private def deactivateFixedCellMode(position: Position): Unit =
    runOnUIThread:
      squareMap(position).updateColor(gridState.fixedCell(position))
      gridState.fixedCell.clear()
    gridState.resetHoverColors()
    gridState.availablePatternsClickFixed = gridState.availablePatterns

  private def placePattern(pattern: Map[Position, Token], newPhase: GamePhase): Unit =
    val effect = PatternApplication(pattern)
    observableSubject.onNext(ResolvePatternChoice(PatternApplication(pattern)))
    observableSubject.onNext(UpdateGamePhase(PhaseEffect(newPhase)))
    gridState.hoveredCells.clear()