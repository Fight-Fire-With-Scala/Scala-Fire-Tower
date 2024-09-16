package it.unibo.model.effect.pattern

import it.unibo.model.card.Card
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.MoveEffect.{ BotChoice, CardChosen }
import it.unibo.model.effect.core.*
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.player.{ Bot, Move, Person }

trait PatternManager:
  protected def updateDeckAndHand(gb: GameBoard, move: Move): GameBoardEffect =
    move.effect match
      case BotChoice(cardId, patternChosen) =>
        val cardOpt = gb.getCurrentPlayer.hand.find(_.id == cardId)
        cardOpt
          .map { card =>
            val gbUpdatedHand = updateHand(gb, card)
            updateDeck(gbUpdatedHand, card)
          }
          .getOrElse(gb)
      case MoveEffect.CardChosen(card, _) =>
        card.effect match
          case _: CanBePlayedAsExtra => updateHand(gb, card)
          case _ =>
            val gbUpdatedHand = updateHand(gb, card)
            updateDeck(gbUpdatedHand, card)
      case _ => gb

  private def updateDeck(gb: GameBoard, card: Card) =
    val deck        = gb.deck
    val playedCards = card :: deck.playedCards
    val newDeck     = deck.copy(playedCards = playedCards)
    gb.copy(deck = newDeck)

  private def updateHand(gb: GameBoard, card: Card) =
    val (player, _) = gb.getCurrentPlayer.playCard(card.id)
    gb.updateCurrentPlayer(player)

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
    val lastMove = gb.getCurrentPlayer match
      case p: Person => p.lastCardChosen
      case b: Bot    => b.lastBotChoice
      case _         => None
    lastMove.map(move => run(gb, move)).getOrElse(gb)
