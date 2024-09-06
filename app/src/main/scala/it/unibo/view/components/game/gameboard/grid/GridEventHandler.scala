package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{
  InternalViewSubject,
  ResolvePatternChoice,
  UpdateGamePhaseModel,
  UpdateGamePhaseView,
  ViewSubject
}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.{Direction, GamePhase}
import it.unibo.model.gameboard.GamePhase.{
  PlaySpecialCardPhase,
  PlayStandardCardPhase,
  RedrawCardsPhase,
  WaitingPhase,
  WindPhase
}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized
import it.unibo.view.logger

class GridEventHandler(
    observableSubject: ViewSubject,
    internalObservable: InternalViewSubject,
    squareMap: mutable.Map[Position, GridSquare]
):
  private val hoveredCells: mutable.Map[Position, Color] = mutable.Map()
  private val fixedCell: mutable.Map[Position, Color] = mutable.Map()
  private var currentGamePhase: GamePhase = uninitialized
  private var availablePatterns: Set[Map[Position, Token]] = Set.empty
  private var availablePatternsClickFixed: Set[Map[Position, Token]] = Set.empty

  def updateGamePhase(gamePhase: GamePhase): Unit = currentGamePhase = gamePhase

  def updateAvailablePatterns(ap: Set[Map[Position, Token]]): Unit = availablePatterns = ap

  private def placePattern(pattern: Map[Position, Token], newPhase: GamePhase): Unit =
    observableSubject.onNext(ResolvePatternChoice(pattern))
    internalObservable.onNext(UpdateGamePhaseView(newPhase))
    observableSubject.onNext(UpdateGamePhaseModel(newPhase))
    hoveredCells.clear()

  def handleCellClick(row: Int, col: Int, gamePhase: GamePhase): Unit =
    val position = Position(row, col)
    gamePhase match
      case WindPhase        => if (hoveredCells.contains(position))
          val pattern = availablePatterns.find(_.contains(position)).get
          placePattern(pattern, WaitingPhase)
      case RedrawCardsPhase => ???
      case PlayStandardCardPhase    => handleCardPlay(position)
      case WaitingPhase     => ???
      case PlaySpecialCardPhase => ???

  private def handleCardPlay(position: Position): Unit =
    if hoveredCells.contains(position) then
      if isSinglePatternAvailable then placeSinglePattern(position)
      else if fixedCell.nonEmpty then placeFixedPattern(position)
      else activateFixedCellMode(position)
    else if fixedCell.nonEmpty && fixedCell.contains(position) then
      deactivateFixedCellMode(position)

  private def isSinglePatternAvailable: Boolean = !availablePatterns.exists(_.size > 1)

  private def placeSinglePattern(position: Position): Unit =
    val pattern = availablePatterns.find(_.contains(position)).get
    placePattern(pattern, PlaySpecialCardPhase)

  private def placeFixedPattern(position: Position): Unit =
    fixedCell.clear()
    val pattern = availablePatternsClickFixed.find(_.contains(position)).get
    placePattern(pattern, PlaySpecialCardPhase)

  private def activateFixedCellMode(position: Position): Unit =
    val pattern = availablePatterns.find(_.contains(position)).get
    availablePatternsClickFixed = availablePatterns.filter(_.contains(position))
    runOnUIThread {
      fixedCell += position -> hoveredCells(position)
      squareMap(position).updateColor(pattern(position).color.deriveColor(1, 1, 1, 0.7))
      hoveredCells.clear()
    }

  private def deactivateFixedCellMode(position: Position): Unit =
    logger.info("Fixed cell mode deactivated")
    runOnUIThread {
      squareMap(position).updateColor(fixedCell(position))
      fixedCell.clear()
    }
    resetHoverColors()
    availablePatternsClickFixed = availablePatterns

  // if u go on a cell that is an available patterns starts hovering
  private def hoverForAvailablePatterns(row: Int, col: Int): Unit =
    resetHoverColors()
    val position = Position(row, col)
    availablePatterns.find(_.contains(position)) match
      case Some(pattern) => if (!fixedCell.contains(position))
        runOnUIThread {
            hoveredCells += position -> squareMap(position).getColor
            squareMap(position).updateColor(pattern(position).color)
          }
      case None          =>

  def handleCellHover(
      row: Int,
      col: Int,
      hoverDirection: HoverDirection,
      gamePhase: GamePhase
  ): Unit = gamePhase match
    case WindPhase     => hoverForAvailablePatterns(row, col)
    case PlayStandardCardPhase =>
      if fixedCell.nonEmpty then
        resetHoverColors()
        val neighbourPosition = getNeighbor(Position(row, col), hoverDirection)
        val candidatePatterns = availablePatternsClickFixed.filter(_.contains(neighbourPosition))
        if (candidatePatterns.size == 1)
          availablePatternsClickFixed = candidatePatterns
          val pattern = candidatePatterns.head
          pattern.foreach { case (position, token) =>
            if !fixedCell.contains(position) then
              runOnUIThread {
                  hoveredCells += position -> squareMap(position).getColor
                  squareMap(position).updateColor(token.color)
                }
          }
      else hoverForAvailablePatterns(row, col)

    case RedrawCardsPhase => ???
    case WaitingPhase     => ???
    case PlaySpecialCardPhase => ???

  private def resetHoverColors(): Unit =
    hoveredCells.foreach((position, color) => runOnUIThread(squareMap(position).updateColor(color)))
    hoveredCells.clear()

  private def getNeighbor(startPosition: Position, hoverDirection: HoverDirection): Position =
    hoverDirection.direction match
      case Some(direction) => startPosition + direction.getDelta
      case None            => startPosition
