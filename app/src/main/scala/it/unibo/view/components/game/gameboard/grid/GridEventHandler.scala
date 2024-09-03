package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{InternalViewSubject, ResolvePatternChoice, UpdateGamePhaseModel, UpdateGamePhaseView, ViewSubject}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.{Direction, GamePhase}
import it.unibo.model.gameboard.GamePhase.{ExtraActionPhase, PlayCardPhase, RedrawCardsPhase, WaitingPhase, WindPhase}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import it.unibo.view.logger
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized

class GridEventHandler(
    observableSubject: ViewSubject,
    internalObservable: InternalViewSubject,
    squareMap: mutable.Map[Position, GridSquare]
):
  private val hoveredCells: mutable.Map[Position, Color] = mutable.Map()
  private val clickedCells: mutable.Map[Position, Color] = mutable.Map()
  private var currentGamePhase: GamePhase = uninitialized
  private var availablePatterns: List[Map[Position, Token]] = List.empty

  def updateGamePhase(gamePhase: GamePhase): Unit =
    currentGamePhase = gamePhase

  def updateAvailablePatterns(ap: List[Map[Position, Token]]): Unit = availablePatterns = ap

  def handleCellClick(row: Int, col: Int, gamePhase: GamePhase): Unit =
    val position = Position(row, col)
    gamePhase match
      case WindPhase =>
        if(hoveredCells.contains(position))
          hoveredCells.clear()
          val pattern = availablePatterns.find(_.contains(position)).get
          observableSubject.onNext(ResolvePatternChoice(pattern))
          internalObservable.onNext(UpdateGamePhaseView(WaitingPhase))
          observableSubject.onNext(UpdateGamePhaseModel(WaitingPhase))
      case RedrawCardsPhase => ???
      case PlayCardPhase =>
        if(hoveredCells.contains(position))
          val pattern = availablePatterns.find(_.contains(position)).get
          val hoverColor = getHoverColor(pattern(position))
          runOnUIThread {
            clickedCells += position -> squareMap(position).getColor
            squareMap(position).updateColor(hoverColor.deriveColor(1, 1, 1, 0.5))
          }
      case WaitingPhase => ???
      case ExtraActionPhase => ???

  private def hoverForAvailablePatterns(row: Int, col: Int): Unit =
    resetHoverColors(Position(row, col))
    val position = Position(row, col)
    availablePatterns.find(_.contains(position)) match
      case Some(pattern) =>
        val hoverColor = getHoverColor(pattern(position))
        runOnUIThread {
          hoveredCells += position -> squareMap(position).getColor
          squareMap(position).updateColor(hoverColor)
        }
      case None =>

  private def hoverForClickedCells(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    val position = Position(row, col)
    resetHoverColors(Position(row, col))
    val uniquePatterns = availablePatterns
      .filter { pattern =>
        pattern.contains(position) && pattern.keys.forall { pos =>
          val delta = pos - position
          logger.info("Delta: " + delta + " pos: " + pos + " position: " + position)
          hoverDirection.direction match
            case Some(Direction.North) => delta.row < 0 || (delta.row == 0 && delta.col == 0)
            case Some(Direction.South) => delta.row > 0 || (delta.row == 0 && delta.col == 0)
            case Some(Direction.West)  => delta.col < 0 || (delta.row == 0 && delta.col == 0)
            case Some(Direction.East)  => delta.col > 0 || (delta.row == 0 && delta.col == 0)
            case None                  => false
        }
      }
      .map(_.keySet)
      .distinct.flatMap(keys => availablePatterns.filter(_.keySet == keys))

    logger.info(s"Unique patterns: $uniquePatterns")
    //Unique patterns should be always unique
    uniquePatterns.foreach { pattern =>
      pattern.foreach { case (pos, token) =>
        if pos != position then
          val hoverColor = getHoverColor(token)
          runOnUIThread {
            hoveredCells += pos -> squareMap(pos).getColor
            squareMap(pos).updateColor(hoverColor)
          }
      }
    }

  def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection, gamePhase: GamePhase): Unit =
    gamePhase match
      case GamePhase.WindPhase =>
        hoverForAvailablePatterns(row, col)
      case GamePhase.RedrawCardsPhase => ???
      case GamePhase.PlayCardPhase =>
        if clickedCells.isEmpty then
          hoverForAvailablePatterns(row, col)
        else
          hoverForClickedCells(row, col, hoverDirection)
      case GamePhase.WaitingPhase => ???
      case GamePhase.ExtraActionPhase => ???

  //restart from here
  private def resetHoverColors(currentCell: Position): Unit =
    hoveredCells.foreach((position, color) =>
      //if currentCell != position then
        runOnUIThread(squareMap(position).updateColor(color)))
    hoveredCells.clear()

  private def getHoverColor(token: Token): Color = token match
    case Fire      => Color.DarkOrange
    case Firebreak => Color.Blue
    case _         => Color.Gray
