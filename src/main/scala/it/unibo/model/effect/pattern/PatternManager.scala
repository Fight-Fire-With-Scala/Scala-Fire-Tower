package it.unibo.model.effect.pattern

import it.unibo.model.card.Card
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.MoveEffect.CardChosen
import it.unibo.model.effect.core._
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.Bot
import it.unibo.model.gameboard.player.Move
import it.unibo.model.gameboard.player.Person

trait PatternManager:
  protected def updateDeckAndHand(gb: GameBoard, move: Move): GameBoardEffect =
    val deck          = gb.deck
    val currentPlayer = gb.getCurrentPlayer
    move.effect match
      case MoveEffect.CardChosen(card, _) =>
        card.effect match
          case _: CanBePlayedAsExtra => gb
          case _ =>
            val playedCards = card :: deck.playedCards
            val (player, _) = currentPlayer.playCard(card.id)
            val newDeck     = deck.copy(playedCards = playedCards)
            gb.updateCurrentPlayer(player).copy(deck = newDeck)
      case _ => gb

  protected def updatePlayer(gb: GameBoard, move: Move): GameBoardEffect =
    val updatedPlayerMoves = gb.getCurrentPlayer.moves.filter(m => m != move)
    gb.getCurrentPlayer match
      case b: Bot =>
        val updatedPlayer = b.updatePlayer(moves = updatedPlayerMoves)
        gb.updateCurrentPlayer(updatedPlayer)
      case p: Person =>
        val updatedPlayer = p.updatePlayer(moves = updatedPlayerMoves)
        gb.updateCurrentPlayer(updatedPlayer)
      case _ => gb

  protected def runIfLastCardChosenFound(
      gb: GameBoard,
      run: (GameBoard, Move) => GameBoardEffect
  ): GameBoardEffect =
    val lastMove = gb.getCurrentPlayer.lastCardChosen
    lastMove.map(move => run(gb, move)).getOrElse(gb)
