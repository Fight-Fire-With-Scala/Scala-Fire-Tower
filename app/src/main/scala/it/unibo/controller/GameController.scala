package it.unibo.controller

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
//case class GameController():
//  def handleGamePhase(phase: GamePhase, choice: Option[ActionPhaseChoice]): Unit = phase match
//    case WindPhase => ???
//    case ActionPhase => choice match
//      case Some(c) => handleActionPhaseChoice(c)
//      case None => None
//
//  private def handleActionPhaseChoice(choice: ActionPhaseChoice): Unit = choice match
//    case RedrawCards => ???
//    case PlayCard => ???
