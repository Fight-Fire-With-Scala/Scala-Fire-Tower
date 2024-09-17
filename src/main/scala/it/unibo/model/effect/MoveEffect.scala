package it.unibo.model.effect

import it.unibo.model.card.Card
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.effect.core.given_Conversion_GameBoard_GameBoardEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.player.{ Bot, Move, Person }

enum MoveEffect extends IGameEffect:
  case CardsRedrawn(cards: List[Int])
  case CardChosen(card: Card, computedPatterns: Set[Map[Position, Token]])
  case BotChoice(cardId: Int, patternChosen: Map[Position, Token])
  case PatternChosen(computedPatterns: Set[Map[Position, Token]])
  case PatternApplied(chosenPattern: Map[Position, Token])

object MoveEffect:
  private def solveMove(effect: MoveEffect, gb: GameBoard): GameBoardEffect =
    val move          = Move(gb.turnNumber, effect)
    val currentPlayer = gb.getCurrentPlayer.logMove(move)
    val newGb         = gb.updateCurrentPlayer(currentPlayer)
    newGb

  def logPatternChosen(
      gb: GameBoard,
      availablePatterns: Set[Map[Position, Token]]
  ): GameBoardEffect =
    val move = PatternChosen(availablePatterns)
    MoveEffect.solveMove(move, gb)

  def logBotChoice(
      gb: GameBoard,
      cardId: Int,
      patternChosen: Map[Position, Token]
  ): GameBoardEffect =
    val move = MoveEffect.BotChoice(cardId, patternChosen)
    MoveEffect.solveMove(move, gb)

  def logCardChosen(
      gb: GameBoard,
      card: Card,
      availablePatterns: Set[Map[Position, Token]]
  ): GameBoardEffect =
    val move = MoveEffect.CardChosen(card, availablePatterns)
    MoveEffect.solveMove(move, gb)

  def logPatternApplied(gb: GameBoard, pattern: Map[Position, Token]): GameBoardEffect =
    val move = PatternApplied(pattern)
    MoveEffect.solveMove(move, gb)

  def runIfLastMoveFound(
      gb: GameBoard,
      run: (GameBoard, Move) => GameBoardEffect
  ): GameBoardEffect =
    gb.getCurrentPlayer match
      case p: Person => p.lastCardChosen.map(move => run(gb, move)).getOrElse(gb)
      case b: Bot    => b.lastBotChoice.map(move => run(gb, move)).getOrElse(gb)
      case _         => gb
