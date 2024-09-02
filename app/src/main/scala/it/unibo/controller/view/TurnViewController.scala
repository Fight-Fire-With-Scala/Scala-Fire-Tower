package it.unibo.controller.view

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.game.gameboard.sidebar.{
  DeckComponent,
  GameInfoComponent,
  WindRoseComponent
}

// TODO avoid reference to the controller in the view
// TODO avoid passing observables here
final case class TurnViewController(
    internalObservable: InternalViewSubject,
    viewObservable: ViewSubject
) extends DiscardController with ActivationController with PlayCardController:

  def refreshView(gameBoard: GameBoard): Unit =
    val currentGamePhase = gameBoard.gamePhase
    updateGamePhase(currentGamePhase)
    
    gameComponent.fold(()) { component =>
      component.updateGrid(gameBoard.board.grid)
      component.updatePlayer(gameBoard.currentPlayer)
      component.gridComponent.setAvailablePatterns(gameBoard.board.availablePatterns)
      component.sidebarComponent.components.foreach {
        case c: GameInfoComponent =>
          c.updateTurnPhase(currentGamePhase.toString)
          c.updateTurnNumber(0)
          c.updateTurnPlayer(gameBoard.currentPlayer.name)
        case c: WindRoseComponent => c.updateWindRoseDirection(gameBoard.board.windDirection)
        case c: DeckComponent     =>
      }
    }
