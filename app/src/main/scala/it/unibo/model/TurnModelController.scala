package it.unibo.model

import alice.tuprolog.{Struct, Var}
import it.unibo.controller.{ShowAvailablePatterns, logger}
import it.unibo.model.ModelModule.Model
import it.unibo.model.cards.choices.StepChoice
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.{PlayCard, RedrawCards, WaitingPhase, WindPhase}
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.prolog.Rule

/*
In the modelMessageHandler we needed the logic to handle the status of the turn.
because the turn could be made in two phases and with different possibilities.
Is a sort of handler of the game phase.
 */

case class TurnModelController():
  def handleWindPhase(model: Model): Unit =
    val board = model.getGameBoard.board
    val direction = board.windDirection

    val availablePatterns = PatternComputationResolver(
      VerySmallEffect(Map("a" -> Fire)),
      Rule(Struct.of("fire", Var.of("R"))),
      List(direction)
    ).getAvailableMoves(board).patterns

    model.getObservable.onNext(ShowAvailablePatterns(availablePatterns))

  def handleActionPhase(cardId: Int, model: Model, choice: GamePhase): Unit =
    handleActionPhaseChoice(cardId, model, choice)
    //model.getGameBoard.changeTurn()

  private def handleActionPhaseChoice(cardId: Int, model: Model, choice: GamePhase): Unit =
    choice match
      case RedrawCards => ???
      case PlayCard    =>
        val gameBoard = model.getGameBoard
        val card = gameBoard.currentPlayer.hand.find(_.id == cardId)
        card match
          case Some(c) => gameBoard.resolveCardPlayed(c, StepChoice.PatternComputation)
          case None => logger.warn("No card found")
      case WindPhase => ???
      case WaitingPhase => ???