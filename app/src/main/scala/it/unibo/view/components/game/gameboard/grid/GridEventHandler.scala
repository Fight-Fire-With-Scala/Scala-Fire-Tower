package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{InternalViewSubject, ResolvePatternChoice, UpdateGamePhaseModel, UpdateGamePhaseView, ViewSubject}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.cards.Card
import it.unibo.model.cards.Card.allCards
import it.unibo.model.cards.types.{FireCard, WindCard}
import it.unibo.model.gameboard.{Direction, GamePhase}
import it.unibo.model.gameboard.GamePhase.{ExtraActionPhase, PlayCardPhase, RedrawCardsPhase, WaitingPhase, WindPhase}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak}
import it.unibo.view.components.game.gameboard.grid.FigurePattern.{Row3, SingleCell, Square, Void}
import it.unibo.view.logger
import scalafx.scene.paint.Color

import java.util.Optional
import scala.collection.mutable
import scala.compiletime.uninitialized

enum FigurePattern:
  case SingleCell
  case Row3
  case Square
  case Void

class GridEventHandler(
                        observableSubject: ViewSubject,
                        internalObservable: InternalViewSubject,
                        squareMap: mutable.Map[Position, GridSquare]
                      ):
  private val hoveredCells: mutable.Map[Position, Color] = mutable.Map()
  private val clickedCell: mutable.Map[Position, Color] = mutable.Map()
  private var currentGamePhase: GamePhase = uninitialized
  private var availablePatterns: List[Map[Position, Token]] = List.empty
  private var actualCard: Option[Card] = None
  private var actualPatternResolved: FigurePattern = Void

  def updateGamePhase(gamePhase: GamePhase): Unit = currentGamePhase = gamePhase
  def setActualCard(card: Option[Card]): Unit =
    actualCard = card
    card match
      case None       => actualPatternResolved = FigurePattern.Void
      case Some(card) => card.cardType.effectType.id match
        case 4 | 5 | 6 | 7 => actualPatternResolved = SingleCell
        case 1             => actualPatternResolved = Row3

  def updateAvailablePatterns(ap: List[Map[Position, Token]]): Unit = availablePatterns = ap

  def handleCellClick(row: Int, col: Int, gamePhase: GamePhase): Unit =
    val position = Position(row, col)
    gamePhase match
      case WindPhase        => if (hoveredCells.contains(position))
        hoveredCells.clear()
        val pattern = availablePatterns.find(_.contains(position)).get
        observableSubject.onNext(ResolvePatternChoice(pattern))
        internalObservable.onNext(UpdateGamePhaseView(WaitingPhase))
        observableSubject.onNext(UpdateGamePhaseModel(WaitingPhase))
      case RedrawCardsPhase => ???
      case PlayCardPhase    =>
        actualPatternResolved match
          case SingleCell =>
            if(hoveredCells.contains(position))
              val pattern = availablePatterns.find(_.contains(position)).get
                observableSubject.onNext(ResolvePatternChoice(pattern))
                internalObservable.onNext(UpdateGamePhaseView(ExtraActionPhase))
                observableSubject.onNext(UpdateGamePhaseModel(ExtraActionPhase))
                hoveredCells.clear()
          case Row3       =>
            logger.info(s"Row3 pattern resolving")
            handleClickForRowPattern(position)
          case Square => ???
          case Void => ???

      case WaitingPhase     => ???
      case ExtraActionPhase => ???

  private def handleClickForRowPattern(position: Position): Unit =
    if clickedCell.contains(position) then
      if hoveredCells.nonEmpty then
        val pattern = (hoveredCells.keys ++ clickedCell.keys).map { pos =>
          pos -> availablePatterns.flatMap(_.get(pos)).head
        }.toMap
        logger.info(s"Pattern to resolve: $pattern")
        observableSubject.onNext(ResolvePatternChoice(pattern))
        internalObservable.onNext(UpdateGamePhaseView(ExtraActionPhase))
        observableSubject.onNext(UpdateGamePhaseModel(ExtraActionPhase))
        hoveredCells.clear()
        clickedCell.clear()
      else
        runOnUIThread {
          squareMap(position).updateColor(clickedCell(position))
          clickedCell.clear()
        }
    else if hoveredCells.contains(position) && clickedCell.isEmpty then
      val pattern = availablePatterns.find(_.contains(position)).get
      val hoverColor = getHoverColor(pattern(position))
      runOnUIThread {
        // Clicked cell has to keep the old color of the cell before hovering
        clickedCell += position -> hoveredCells(position)
        squareMap(position).updateColor(hoverColor.deriveColor(1, 1, 1, 0.5))
        hoveredCells.clear()
      }
      logger.info(s"ActualPattern = $actualPatternResolved")

  private def hoverForAvailablePatterns(row: Int, col: Int): Unit =
    resetHoverColors()
    val position = Position(row, col)
    availablePatterns.find(_.contains(position)) match
      case Some(pattern) =>
        val hoverColor = getHoverColor(pattern(position))
        runOnUIThread {
          hoveredCells += position -> squareMap(position).getColor
          squareMap(position).updateColor(hoverColor)
        }
      case None          =>

  private def hoverForClickedCells(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    val position = Position(row, col)
    if (clickedCell.contains(position))
      resetHoverColors()
      val uniquePatterns = availablePatterns.filter { pattern =>
        pattern.contains(position) && pattern.keys.forall { pos =>
          val delta = pos - position

          hoverDirection.direction match
            case Some(Direction.North) => delta.row < 0 || (delta.row == 0 && delta.col == 0)
            case Some(Direction.South) => delta.row > 0 || (delta.row == 0 && delta.col == 0)
            case Some(Direction.West)  => delta.col < 0 || (delta.row == 0 && delta.col == 0)
            case Some(Direction.East)  => delta.col > 0 || (delta.row == 0 && delta.col == 0)
            case None                  => false
        }
      }.map(_.toSet).distinct.map(_.toMap)
      // TODO: why distinct is needed if mapped into a set ?

      // Unique patterns should be always unique
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

  def handleCellHover(
                       row: Int,
                       col: Int,
                       hoverDirection: HoverDirection,
                       gamePhase: GamePhase
                     ): Unit = gamePhase match
    case GamePhase.WindPhase        => hoverForAvailablePatterns(row, col)
    case GamePhase.RedrawCardsPhase => ???
    case GamePhase.PlayCardPhase    =>
      actualPatternResolved match
        case SingleCell => hoverForAvailablePatterns(row, col)
        case Row3       =>
          if clickedCell.isEmpty then hoverForAvailablePatterns(row, col)
          else hoverForClickedCells(row, col, hoverDirection)
        case Square => ???
        case Void => ???

    case GamePhase.WaitingPhase     => ???
    case GamePhase.ExtraActionPhase => ???

  private def resetHoverColors(): Unit =
    hoveredCells.foreach((position, color) => runOnUIThread(squareMap(position).updateColor(color)))
    hoveredCells.clear()

  private def getHoverColor(token: Token): Color = token match
    case Fire      => Color.DarkOrange
    case Firebreak => Color.Blue
    case _         => Color.Gray