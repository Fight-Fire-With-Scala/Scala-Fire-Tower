package it.unibo.model.gameboard.player

import it.unibo.model.card.Card
import it.unibo.model.effect.core.{ DefensiveEffect, OffensiveEffect }
import it.unibo.model.reasoner.decisionmaking.AttackDefense
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.ConcreteToken

trait IMakeDecision {
  protected def filterCardsBasedOnDecision(hand: List[Card], decision: AttackDefense): List[Card] =
    decision match
      case AttackDefense.Attack =>
        hand.collect:
          case card if card.effect.computations.exists {
                case _: OffensiveEffect => true
                case _                  => false
              } =>
            card
      case AttackDefense.Defense =>
        hand.collect:
          case card if card.effect.computations.exists {
                case _: DefensiveEffect => true
                case _                  => false
              } =>
            card

  protected def isFireTokenInTowerArea(gb: GameBoard): Boolean =
    val towerPositions = gb.board.grid.getTowerCells(gb.getCurrentPlayer.towerPositions)
    towerPositions.exists(pos =>
      gb.board.grid.getToken(pos).exists {
        case token: ConcreteToken => token == ConcreteToken.Fire
        case _                    => false
      }
    )

  protected def isFireBreakTokenInBoard(gb: GameBoard): Boolean =
    val res = gb.board.grid.tokens.exists { case (_, token) =>
      token == ConcreteToken.Firebreak
    }
    res

}
