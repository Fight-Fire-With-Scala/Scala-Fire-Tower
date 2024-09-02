package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{InternalViewSubject, ResolvePatternChoice, UpdateGamePhaseModel, UpdateGamePhaseView, ViewSubject}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.{Direction, GamePhase}
import it.unibo.model.gameboard.GamePhase.{ExtraActionPhase, WaitingPhase}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import it.unibo.view.logger
import scalafx.scene.paint.Color

import scala.collection.mutable

class GridEventHandler(
    observableSubject: ViewSubject,
    internalObservable: InternalViewSubject,
    squareMap: mutable.Map[Position, GridSquare]
):
  private val hoveredCellsOriginalColors: mutable.Map[Position, Color] = mutable.Map()
  private var availablePatterns: List[Map[Position, Token]] = List.empty

  def updateAvailablePatterns(ap: List[Map[Position, Token]]): Unit = availablePatterns = ap

  private def handleCellClick(gamePhaseToChange: GamePhase): Unit =
    val matchedPatterns: Map[Position, Token] = hoveredCellsOriginalColors.keys
      .flatMap { position =>
        availablePatterns.collect {
          case pattern if pattern.contains(position) => position -> pattern(position)
        }
      }.toMap
    if matchedPatterns.nonEmpty then
      hoveredCellsOriginalColors.clear()
      observableSubject.onNext(ResolvePatternChoice(matchedPatterns))
      internalObservable.onNext(UpdateGamePhaseView(gamePhaseToChange))
      observableSubject.onNext(UpdateGamePhaseModel(gamePhaseToChange))

  def handleCellClickForWindPhase(): Unit = handleCellClick(WaitingPhase)
  def handleCellClickForCardPhase(): Unit = handleCellClick(ExtraActionPhase)

  def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    resetHoverColors()
    hoverDirection.direction match
      case Some(dir) =>
        val positionToCheck = checkNeighbor(Position(row, col), dir)
        val candidatePositions = availablePatterns.filter(_.contains(positionToCheck))
        candidatePositions.foreach { pattern =>
          val square = squareMap(positionToCheck)
          val hoverColor = getHoverColor(pattern(positionToCheck))
          runOnUIThread {
            hoveredCellsOriginalColors += positionToCheck -> square.getColor
            square.updateColor(hoverColor)
          }
        }
        logger.info(s"$candidatePositions")
      case None      =>

  private def resetHoverColors(): Unit =
    hoveredCellsOriginalColors.foreach { case (position, color) =>
      val square = squareMap(position)
      runOnUIThread(square.updateColor(color))
    }
    hoveredCellsOriginalColors.clear()

  private def checkNeighbor(startPosition: Position, direction: Direction): Position =
    startPosition + direction.getDelta

  private def getHoverColor(token: Token): Color = token match
    case Fire      => Color.DarkOrange
    case Firebreak => Color.Blue
    case _         => Color.Gray
