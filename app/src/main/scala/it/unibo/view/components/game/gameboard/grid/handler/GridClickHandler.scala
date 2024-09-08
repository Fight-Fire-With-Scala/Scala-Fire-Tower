package it.unibo.view.components.game.gameboard.grid.handler

import it.unibo.controller.*
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.{DecisionPhase, PlaySpecialCardPhase, PlayStandardCardPhase, WaitingPhase, WindPhase}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.components.game.gameboard.grid.{EffectType, GridSquare, GridState}
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

  private def isExplosionPattern: Boolean = gridState.effectCode == EffectType.Esplosione ||
    gridState.effectCode == EffectType.VigileDelFuocoParacadutista

  private def isSinglePatternAvailable: Boolean = gridState.effectCode == EffectType.Nord ||
    gridState.effectCode == EffectType.Sud || gridState.effectCode == EffectType.Est ||
    gridState.effectCode == EffectType.Ovest || gridState.effectCode == EffectType.RiDeforesta

  private def placeExplosionPattern(position: Position): Unit = gridState.effectCode match
    case EffectType.Esplosione                  => placePattern(
        gridState.availablePatterns
          .find(_.exists((pos, tkn) => pos == position && tkn == Firebreak)).get,
        DecisionPhase
      )
    case EffectType.VigileDelFuocoParacadutista => placePattern(
        gridState.availablePatterns.find(_.exists((pos, tkn) => pos == position && tkn == Fire))
          .get,
        DecisionPhase
      )
    case _                                      => logger.error("Error in explosion pattern")

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
    observableSubject.onNext(ResolvePatternChoice(pattern))
    internalObservable.onNext(UpdateGamePhaseView(newPhase))
    observableSubject.onNext(UpdateGamePhaseModel(newPhase))
    gridState.hoveredCells.clear()
