package it.unibo.model.effects

import it.unibo.model.cards.Card
import it.unibo.model.effects.MoveEffect.{CardChosen, PatternApplied, PatternChosen}
import it.unibo.model.effects.core.{
  CanBePlayedAsExtra,
  GameBoardEffectResolver,
  GameEffectResolver,
  IGameEffect,
  ILogicEffect
}
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.gameboard.player.{Bot, Move, Person}
import it.unibo.model.logger
import it.unibo.model.prolog.PrologProgram.{cardsProgram, solverProgram}
import it.unibo.model.prolog.{GridTheory, PrologEngine}

enum PatternEffect extends IGameEffect:
  case PatternComputation(logicEffect: ILogicEffect)
  case CardComputation(cardId: Int, logicEffect: ILogicEffect)
  case CardsComputation(cards: Map[Int, List[ILogicEffect]])
  case PatternApplication(pattern: Map[Position, Token])
  case ResetPatternComputation

object PatternEffect:
  import it.unibo.model.prolog.PrologUtils.given_Conversion_Rule_Term

  private def computePatterns(gb: GameBoard, cardId: Int, logicEffect: ILogicEffect) =
    val theory = GridTheory(gb.board.grid, Map(cardId -> List(logicEffect)))
    theory.append(cardsProgram)
    theory.append(solverProgram)
    val engine = PrologEngine(theory)
    logicEffect.goals.map(g => engine.solveAsPatterns(g(cardId))).reduce((a, b) => a.union(b))

  private def computePatterns(gb: GameBoard, cards: Map[Int, List[ILogicEffect]]) = ???
//    val theory = GridTheory(gb.board.grid, cards)
//    theory.append(cardsProgram)
//    theory.append(solverProgram)
//    val engine = PrologEngine(theory)
    

  private def resolvePatternComputation(logicEffect: ILogicEffect) =
    GameBoardEffectResolver { (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val availablePatterns = computePatterns(gb, -1, logicEffect)
      val move = PatternChosen(availablePatterns)
      MoveEffect.resolveMove(move, gb)
    }

  private def resolveCardsComputation(cards: Map[Int, List[ILogicEffect]]) =
    GameBoardEffectResolver { (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val availablePatterns: Map[Int, Set[Map[Position, Token]]] =
        computePatterns(gb, cards) // TODO call prolog solver
      val player = gb.getCurrentPlayer
      player match
        case b: Bot => logCardsComputation(gb, availablePatterns)
        case _      => GameBoardEffect(gb)
    }

  private def logCardsComputation(gb: GameBoard, cards: Map[Int, Set[Map[Position, Token]]]) =
    val move = MoveEffect.CardsChosen(cards)
    MoveEffect.resolveMove(move, gb)

  private def resolveCardComputation(cardId: Int, logicEffect: ILogicEffect) =
    GameBoardEffectResolver { (gbe: GameBoardEffect) =>
      val gb = gbe.gameBoard
      val availablePatterns = computePatterns(gb, cardId, logicEffect)
      val card = gb.getCurrentPlayer.hand.find(_.id == cardId)
      logCardComputation(gb, card, availablePatterns)
    }

  private def logCardComputation(
      gb: GameBoard,
      card: Option[Card],
      availablePatterns: Set[Map[Position, Token]]
  ) = card match
    case Some(c) =>
      val move = MoveEffect.CardChosen(c, availablePatterns)
      MoveEffect.resolveMove(move, gb)
    case None    =>
      logger.warn(s"No card found")
      GameBoardEffect(gb)

  private def resolvePatternApplication(pattern: Map[Position, Token]) = GameBoardEffectResolver:
    (gbe: GameBoardEffect) =>
      // Update the grid
      val gb = gbe.gameBoard
      val b = gb.board
      val newGrid = b.grid.setTokens(pattern.toSeq*)
      // Update the deck
      val lastMove = gb.getCurrentPlayer.lastCardChosen
      lastMove match
        case Some(m) =>
          // Log the move
          val move = PatternApplied(pattern)
          // Save the result
          val newGb = updateDeckAndHand(gb, m.effect).copy(board = b.copy(grid = newGrid))
          MoveEffect.resolveMove(move, newGb)
        case None    =>
          logger.warn("Could not find the last card chosen, deck not updated and move not logged")
          val newGb = gb.copy(board = b.copy(grid = newGrid))
          GameBoardEffect(newGb)

  private def updateDeckAndHand(gb: GameBoard, move: MoveEffect): GameBoard =
    val deck = gb.deck
    val currentPlayer = gb.getCurrentPlayer
    move match
      case MoveEffect.CardChosen(card, _) => card.effect match
          case _: CanBePlayedAsExtra => gb
          case _                     =>
            val playedCards = card :: deck.playedCards
            val (player, _) = currentPlayer.playCard(card.id)
            val newDeck = deck.copy(playedCards = playedCards)
            gb.updateCurrentPlayer(player).copy(deck = newDeck)
      case _                              => gb

  private def getUpdatedCurrentPlayer(gb: GameBoard, move: Move) =
    val updatedPlayerMoves = gb.getCurrentPlayer.moves.filter(m => m != move)
    gb.getCurrentPlayer match
      case b: Bot    =>
        val updatedPlayer = b.updatePlayer(moves = updatedPlayerMoves)
        GameBoardEffect(gb.updateCurrentPlayer(updatedPlayer))
      case p: Person =>
        val updatedPlayer = p.updatePlayer(moves = updatedPlayerMoves)
        GameBoardEffect(gb.updateCurrentPlayer(updatedPlayer))
      case _         => GameBoardEffect(gb)

  private def resolvePatternReset() = GameBoardEffectResolver { (gbe: GameBoardEffect) =>
    val gb = gbe.gameBoard
    val lastMove = gb.getCurrentPlayer.lastPatternChosen
    lastMove match
      case Some(move) => getUpdatedCurrentPlayer(gb, move)
      case None       =>
        logger.warn("Could not find the last card played, move not logged")
        GameBoardEffect(gb)
  }

  val patternEffectResolver: GameEffectResolver[IGameEffect, GameBoardEffectResolver] =
    GameEffectResolver:
      case e: GameBoardEffect               => GameBoardEffectResolver { (gbe: GameBoardEffect) =>
          GameBoardEffect(gbe.gameBoard)
        }
      case CardComputation(id, logicEffect) => resolveCardComputation(id, logicEffect)
      case CardsComputation(cards)          => resolveCardsComputation(cards)
      case PatternComputation(logicEffect)  => resolvePatternComputation(logicEffect)
      case PatternApplication(pattern)      => resolvePatternApplication(pattern)
      case ResetPatternComputation          => resolvePatternReset()
