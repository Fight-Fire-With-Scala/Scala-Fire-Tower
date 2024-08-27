package it.unibo.controller

import alice.tuprolog.{Struct, Var}
import it.unibo.model.ModelModule.Model
import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.cards.resolvers.PatternComputationResolver
import it.unibo.model.gameboard.ActionPhaseChoice.{PlayCard, RedrawCards}
import it.unibo.model.gameboard.GamePhase.{ActionPhase, WindPhase}
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.{ActionPhaseChoice, GamePhase}
import it.unibo.model.prolog.Rule

//import it.unibo.model.cards.effects.GameboardEffect

/*
In the modelMessageHandler we needed the logic to handle the status of the turn.
because the turn could be made in two phases and with different possibilities.
Is a sort of handler of the game phase.
 */

//enum GamePhase:
//  case WindPhase, ActionPhase
//
//enum ActionPhaseChoice:
//  case RedrawCards, PlayCard
//
//ovviamente manca il model al GameController. Ma non saprei se darglielo o meno.
// direi di provare a metterlo, secondo me lo usiamo tutte le volte che serve gestire il turno
//probabilmente sì, però non so se è il caso di gestire le modifiche al model tramite il modelModule
//ma comunque dovremmo passarlo
//per ora passiamolo
// ok sì per accorciare il codice poi spostiamo il grosso nel model module
// meglio così che dici?
//sì
case class GameController():
  def handleWindPhase(model: Model): Unit =
    val board = model.getGameBoard.board
    val direction = board.windDirection

    val availablePatterns = PatternComputationResolver(
      VerySmallEffect(Map("a" -> Fire)),
      Rule(Struct.of("fire", Var.of("R"))),
      List(direction)
    ).getAvailableMoves(board).patterns

    model.getObservable.onNext(ShowAvailablePatterns(availablePatterns))

  def handleActionPhase(model: Model, choice: ActionPhaseChoice): Unit =
    handleActionPhaseChoice(model, choice)

  private def handleActionPhaseChoice(model: Model, choice: ActionPhaseChoice): Unit = choice match
    case RedrawCards => ???
    case PlayCard    => ???
