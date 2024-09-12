package it.unibo.model.effect.pattern

import alice.tuprolog.Theory
import it.unibo.model.card.Card
import it.unibo.model.effect.GameBoardEffect
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.MoveEffect.CardChosen
import it.unibo.model.effect.MoveEffect.PatternApplied
import it.unibo.model.effect.MoveEffect.PatternChosen
import it.unibo.model.effect.core.*
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.player.Bot
import it.unibo.model.gameboard.player.Move
import it.unibo.model.gameboard.player.Person
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term
import it.unibo.model.logger
import it.unibo.model.prolog.{GridTheory, PrologEngine, SolverType}
import it.unibo.model.prolog.SolverType.{
  CardChoserSolver,
  CardSolver,
  ConcatListSolver,
  ManhattanSolver
}
import it.unibo.model.prolog.decisionmaking.AllCardsResultTheory
import it.unibo.model.prolog.given_Conversion_SolverType_Theory

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
    theory.append(SolverType.CardSolver)
    theory.append(SolverType.BaseSolver)
    val engine = PrologEngine(theory)
    logicEffect.goals.map(g => engine.solveAsPatterns(g(cardId))).reduce((a, b) => a.union(b))

  private def computePatterns(
      gb: GameBoard,
      cards: Map[Int, List[ILogicEffect]]
  ): Map[Int, Set[Map[Position, Token]]] =
    val opponentPositions = gb.getOpponent.towerPositions.map(_.position)
    val enemyTower = gb.getOpponent.towerPositions.head.position
    val grid = gb.board.grid

    val dynamicTheory = AllCardsResultTheory(cards)
    val theory = GridTheory(grid, cards)

    val t = Theory
      .parseWithStandardOperators(s"tower_position((${enemyTower.row}, ${enemyTower.col})).")
    theory.append(SolverType.ManhattanSolver)
    theory.append(SolverType.ConcatListSolver)
    theory.append(dynamicTheory)
    theory.append(SolverType.CardChoserSolver)
    theory.append(SolverType.CardSolver)
    theory.append(SolverType.BaseSolver)

    val engine = PrologEngine(theory)
    val goal = "main(R)"
    val result = engine.solve(goal).headOption

    result match
      case Some(solution) =>
        val allCardResults = solution.getTerm("R")
        Map.empty
      case None           => Map.empty

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
      val availablePatterns = computePatterns(gb, cards)
      gb.getCurrentPlayer match
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
      logger.warn("No card found")
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
