package it.unibo.view.component.game.gameboard.grid.handler

import scala.collection.mutable
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.effect.card.FireEffect
import it.unibo.model.effect.card.WaterEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.{ PlaySpecialCardPhase, PlayStandardCardPhase, WindPhase }
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Firebreak
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.view.component.game.gameboard.grid.GridSquare
import it.unibo.view.component.game.gameboard.grid.GridState
import it.unibo.view.component.game.gameboard.grid.HoverDirection
import it.unibo.view.logger

/** Handles the hover events on the game grid.
  *
  * @param _squareMap
  *   A mutable map of `Position` to `GridSquare`.
  * @param gridState
  *   The current state of the grid.
  */
class GridHoverHandler(using _squareMap: mutable.Map[Position, GridSquare], gridState: GridState):
  /** Handles the hover event for a cell based on the current game phase.
    *
    * @param row
    *   The row index of the cell.
    * @param col
    *   The column index of the cell.
    * @param hoverDirection
    *   The direction of the hover.
    * @param gamePhase
    *   The current phase of the game.
    */
  def handleCellHover(
      row: Int,
      col: Int,
      hoverDirection: HoverDirection,
      gamePhase: GamePhase
  ): Unit =
    val position: Position = Position(row, col)

    gamePhase match
      case WindPhase => hoverForAvailablePatterns(position)
      case PlayStandardCardPhase | PlaySpecialCardPhase =>
        if gridState.fixedCell.nonEmpty then hoverForFixedCell(position, hoverDirection)
        else hoverForAvailablePatterns(position)
      case _ =>

  /** Handles hover events when there is a fixed cell.
    *
    * @param position
    *   The position of the cell hovering.
    * @param hoverDirection
    *   The direction of the hover.
    */
  private def hoverForFixedCell(position: Position, hoverDirection: HoverDirection): Unit =
    gridState.resetHoverColors()
    val neighbourPosition = getNeighbor(position, hoverDirection)
    val candidatePatterns = gridState.availablePatternsClickFixed
      .filter(_.contains(neighbourPosition))
    logger.debug(s"Candidate Patterns $candidatePatterns")
    candidatePatterns.toList match
      case pattern :: Nil =>
        gridState.availablePatternsClickHovered = candidatePatterns
        pattern.foreach { case (position, token) =>
          if !gridState.fixedCell.contains(position) then
            runOnUIThread:
              gridState.hoveredCells += position -> _squareMap(position).getColor
              _squareMap(position).updateColor(token.color)
        }
      case _ =>

  private def hoverForAvailablePatterns(position: Position): Unit =
    gridState.resetHoverColors()
    gridState.effectCode match
      case FireEffect.Explosion.effectId    => handleExplosionPattern(position, Firebreak)
      case WaterEffect.SmokeJumper.effectId => handleExplosionPattern(position, Fire)
      case _ =>
        gridState.availablePatterns.find(_.contains(position)) match
          case Some(pattern) =>
            if (!gridState.fixedCell.contains(position))
              updateHoveredCells(position, pattern(position))
          case None =>

  private def handleExplosionPattern(position: Position, token: Token): Unit =
    gridState.availablePatterns.collectFirst:
      case pattern if pattern.contains(position) && pattern(position) == token =>
        (position, pattern(position))
    match
      case Some(pattern) => updateHoveredCells(pattern._1, pattern._2)
      case None          =>

  /** Updates the colors of the hovered cells.
    *
    * @param position
    *   The position of the cell.
    * @param token
    *   The token to be used for updating the color.
    */
  private def updateHoveredCells(position: Position, token: Token): Unit = runOnUIThread:
    gridState.hoveredCells += position -> _squareMap(position).getColor
    _squareMap(position).updateColor(token.color)

  /** Gets the neighboring cell based on the hover direction.
    *
    * @param startPosition
    *   The starting position of the cell.
    * @param hoverDirection
    *   The direction of the hover.
    * @return
    *   The position of the neighboring cell.
    */
  private def getNeighbor(startPosition: Position, hoverDirection: HoverDirection): Position =
    hoverDirection.direction match
      case Some(direction) => startPosition + direction.getDelta
      case None            => startPosition
