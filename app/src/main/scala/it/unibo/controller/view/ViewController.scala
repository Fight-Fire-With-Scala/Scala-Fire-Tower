package it.unibo.controller.view

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.game.gameboard.sidebar.{
  DeckComponent,
  DiceComponent,
  GameInfoComponent,
  WindRoseComponent
}

// TODO avoid reference to the controller in the view
// TODO avoid passing observables here
final case class ViewController(
    internalObservable: InternalViewSubject,
    viewObservable: ViewSubject
) extends DiscardController with ActivationController with PlayCardController:

  def refreshView(gameBoard: GameBoard): Unit =
    val currentGamePhase = gameBoard.gamePhase
    updateGamePhase(currentGamePhase)
    gameComponent.fold(()) { component =>
      component.updateGrid(gameBoard.board.grid, currentGamePhase)
      component.updatePlayer(gameBoard.currentPlayer)(currentGamePhase)
      
      // TODO: when the enum of cardEffects will be done we will pass that instead of the Int
      component.gridComponent.setAvailablePatterns(
        gameBoard.board.availablePatterns,
        gameBoard.board.currentCardId match
          case Some(id) => gameBoard.currentPlayer.hand.find(_.id == id).get.cardType.effectType.id
          case _        => -1
      )
      component.sidebarComponent.components.foreach {
        case c: GameInfoComponent =>
          c.updateTurnPhase(currentGamePhase.toString)
          c.updateTurnNumber(gameBoard.turnNumber)
          c.updateTurnPlayer(gameBoard.currentPlayer.name)
        case c: WindRoseComponent => c.updateWindRoseDirection(gameBoard.board.windDirection)
        case c: DeckComponent     =>
        case c: DiceComponent     =>
      }
    }
