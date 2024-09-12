package it.unibo.controller.view

import it.unibo.controller.{InternalViewSubject, RefreshType, ViewSubject}
import it.unibo.model.effects.MoveEffect
import it.unibo.model.effects.MoveEffect.CardChosen
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Move
import it.unibo.view.ViewModule.View
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.game.gameboard.sidebar.{
  DeckComponent,
  DiceComponent,
  GameInfoComponent,
  WindRoseComponent
}
import it.unibo.view.logger

final case class ViewController(
    view: View,
    internalObservable: InternalViewSubject,
    observable: ViewSubject
) extends DiscardController with ActivationController with PlayCardController:

  def startMenu(): Unit = view.startMenu(observable)

  def startGame(gb: GameBoard): Unit =
    given intObservable: InternalViewSubject = internalObservable
    given viewObservable: ViewSubject = observable
    view.startGame(gb, this)

  def refreshView(gb: GameBoard, refreshType: RefreshType): Unit = gameComponent match
    case Some(c) => updateGameComponent(c, gb)
    case None    => // do not update the view
  private def updateGameComponent(component: GameComponent, gameBoard: GameBoard): Unit =
    val currentGamePhase = gameBoard.gamePhase
    updateGamePhaseActivation(currentGamePhase)

    component.updateGrid(gameBoard, currentGamePhase)
    component.updatePlayer(gameBoard.getCurrentPlayer)(currentGamePhase)

    given c: GameComponent = component
    given gb: GameBoard = gameBoard

    setAvailablePatternsInGrid

    component.sidebarComponent.components.foreach:
      case c: GameInfoComponent =>
        c.updateTurnPhase(currentGamePhase.toString)
        c.updateTurnNumber(gameBoard.turnNumber)
        c.updateTurnPlayer(gameBoard.getCurrentPlayer.name)
      case c: WindRoseComponent => c.updateWindRoseDirection(gameBoard.board.windDirection)
      case c: DeckComponent     =>
      case c: DiceComponent     =>

  private def setAvailablePatternsInGrid(using c: GameComponent, gb: GameBoard): Unit =
    val lastPatternChosenMove = gb.getCurrentPlayer.lastPatternChosen
    lastPatternChosenMove match
      case Some(value) => handleMove(lastPatternChosenMove)
      case None        =>
        val lastCardChosenMove = gb.getCurrentPlayer.lastCardChosen
        handleMove(lastCardChosenMove)

  private def handleMove(lastMove: Option[Move])(using c: GameComponent, gb: GameBoard): Unit =
    lastMove match
      case Some(move) => move.effect match
          case MoveEffect.CardChosen(card, computedPatterns) =>
            if move.turnNumber == gb.turnNumber then
              c.gridComponent.setAvailablePatterns(computedPatterns, card.effect.effectId)
          case MoveEffect.PatternChosen(computedPatterns)    => c.gridComponent
              .setAvailablePatterns(computedPatterns, -1)
          case _                                             => // do not update the grid
      case None       => // do not update the grid
