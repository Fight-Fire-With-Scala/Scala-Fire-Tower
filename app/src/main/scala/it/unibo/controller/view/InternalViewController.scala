package it.unibo.controller.view

import it.unibo.controller.{InternalViewSubject, RefreshType, ViewSubject}
import it.unibo.model.effects.MoveEffect
import it.unibo.model.effects.MoveEffect.CardChosen
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Move
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.game.gameboard.sidebar.{
  DeckComponent,
  DiceComponent,
  GameInfoComponent,
  WindRoseComponent
}

// TODO avoid reference to the controller in the view
final case class InternalViewController(
    internalObservable: InternalViewSubject,
    viewObservable: ViewSubject
) extends DiscardController with ActivationController with PlayCardController:

  def refreshView(gb: GameBoard, refreshType: RefreshType): Unit =
    val currentGamePhase = gb.gamePhase
    updateGamePhase(currentGamePhase)
    gameComponent.fold(()) { component =>
      component.updateGrid(gb, currentGamePhase)
      component.updatePlayer(gb.getCurrentPlayer)(currentGamePhase)

      val lastPatternChosenMove = gb.getCurrentPlayer.lastPatternChosen
      lastPatternChosenMove match
        case Some(value) => handleMove(component, gb, lastPatternChosenMove)
        case None        =>
          val lastCardChosenMove = gb.getCurrentPlayer.lastCardChosen
          handleMove(component, gb, lastCardChosenMove)

      component.sidebarComponent.components.foreach {
        case c: GameInfoComponent =>
          c.updateTurnPhase(currentGamePhase.toString)
          c.updateTurnNumber(gb.turnNumber)
          c.updateTurnPlayer(gb.getCurrentPlayer.name)
        case c: WindRoseComponent => c.updateWindRoseDirection(gb.board.windDirection)
        case c: DeckComponent     =>
        case c: DiceComponent     =>
      }
    }

  private def handleMove(component: GameComponent, gb: GameBoard, lastMove: Option[Move]): Unit =
    lastMove match
      case Some(move) => move.effect match
          case MoveEffect.CardChosen(card, computedPatterns) =>
            if move.turnNumber == gb.turnNumber then
              component.gridComponent.setAvailablePatterns(computedPatterns, card.effect.effectId)
          case MoveEffect.PatternChosen(computedPatterns)    => component.gridComponent
              .setAvailablePatterns(computedPatterns, -1)
          case _                                             => // do not update the grid
      case None       => // do not update the grid
