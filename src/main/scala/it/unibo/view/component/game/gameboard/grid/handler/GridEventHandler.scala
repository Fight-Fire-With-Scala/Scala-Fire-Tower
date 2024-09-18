package it.unibo.view.component.game.gameboard.grid.handler

import scala.collection.mutable

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.view.component.game.gameboard.grid.GridSquare
import it.unibo.view.component.game.gameboard.grid.GridState
import it.unibo.view.component.game.gameboard.grid.HoverDirection

/**
 * The GridEventHandler class coordinates the events of click and hover on the grid.
 * It uses GridClickHandler and GridHoverHandler to manage the click and hover events respectively.
 *
 * @param observableSubject the subject to observe for view updates
 * @param squareMap         the map of grid positions to GridSquare objects
 */
class GridEventHandler(
    observableSubject: ViewSubject,
    squareMap: mutable.Map[Position, GridSquare]
):
  private given gridState : GridState = new GridState(squareMap)
  private given _squareMap: mutable.Map[Position, GridSquare] = squareMap
  private val clickHandler =
    new GridClickHandler(observableSubject)

  private val hoverHandler = new GridHoverHandler()
  
  def updateGamePhase(gamePhase: GamePhase): Unit = gridState.currentGamePhase = gamePhase
  
  def updateAvailablePatterns(ap: Set[Map[Position, Token]]): Unit =
    gridState.availablePatterns = ap

  def setEffectCode(cardEffect: Int): Unit = gridState.effectCode = cardEffect

  def handleCellClick(row: Int, col: Int, gamePhase: GamePhase): Unit = clickHandler
    .handleCellClick(row, col, gamePhase)

  /**
   * Handles the cell hover event by delegating to the GridHoverHandler.
   *
   * @param row            the row of the hovered cell
   * @param col            the column of the hovered cell
   * @param hoverDirection the direction of the hover
   * @param gamePhase      the current game phase
   */
  def handleCellHover(
      row: Int,
      col: Int,
      hoverDirection: HoverDirection,
      gamePhase: GamePhase
  ): Unit = hoverHandler.handleCellHover(row, col, hoverDirection, gamePhase)
