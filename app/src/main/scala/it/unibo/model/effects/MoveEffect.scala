package it.unibo.model.effects

import it.unibo.model.cards.Card
import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.player.Move

enum MoveEffect extends IGameEffect:
  case CardsRedrawn(cards: List[Int])
  case CardChosen(card: Card, computedPatterns: Set[Map[Position, Token]])
  case PatternChosen(computedPatterns: Set[Map[Position, Token]])
  case PatternApplied(chosenPattern: Map[Position, Token])

object MoveEffect:
  def resolveMove(effect: MoveEffect, gb: GameBoard): GameBoardEffect =
    val move = Move(gb.turnNumber, effect)
    val currentPlayer = gb.getCurrentPlayer.logMove(move)
    GameBoardEffect(gb.updateCurrentPlayer(currentPlayer))
