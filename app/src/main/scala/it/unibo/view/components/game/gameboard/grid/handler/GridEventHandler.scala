package it.unibo.view.components.game.gameboard.grid.handler

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.components.game.gameboard.grid.{GridSquare, GridState, HoverDirection}

import scala.collection.mutable

class GridEventHandler(
    observableSubject: ViewSubject,
    internalObservable: InternalViewSubject,
    squareMap: mutable.Map[Position, GridSquare]
):
  private val gridState = new GridState(squareMap)

  private val clickHandler =
    new GridClickHandler(observableSubject, internalObservable, squareMap, gridState)

  private val hoverHandler = new GridHoverHandler(squareMap, gridState)

  def updateGamePhase(gamePhase: GamePhase): Unit = gridState.currentGamePhase = gamePhase

  def updateAvailablePatterns(ap: Set[Map[Position, Token]]): Unit =
    gridState.availablePatterns = ap

  def setEffectCode(cardEffect: Int): Unit = gridState.effectCode = cardEffect

  def handleCellClick(row: Int, col: Int, gamePhase: GamePhase): Unit = clickHandler
    .handleCellClick(row, col, gamePhase)

  def handleCellHover(
      row: Int,
      col: Int,
      hoverDirection: HoverDirection,
      gamePhase: GamePhase
  ): Unit = hoverHandler.handleCellHover(row, col, hoverDirection, gamePhase)
