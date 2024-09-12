package it.unibo.model.gameboard.player

import it.unibo.model.card.Card
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.phase.PhaseEffect.handleWindPhase
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.grid.TowerPosition
import it.unibo.model.logger
import it.unibo.model.prolog.decisionmaking.DecisionMaker
import it.unibo.model.prolog.decisionmaking.DecisionMaker.computeAttackOrDefense

final case class Bot(
    moves: List[Move],
    hand: List[Card],
    botBehaviour: BotBehaviour,
    towerPositions: Set[TowerPosition],
    extraCard: Option[Card] = None
) extends Player:
  override val name: String = "BOT"
  override def updatePlayer(moves: List[Move], hand: List[Card], extraCard: Option[Card]): Player =
    copy(moves = moves, hand = hand, extraCard = extraCard)

  private def handleMove(
      gb: GameBoard,
      lastMove: Option[Move]
  ): Map[Int, Set[Map[Position, Token]]] = lastMove match
    case Some(move) => move.effect match
        case MoveEffect.CardsChosen(cards) => ??? // cards: Map[Card, Set[Map[Position, Token]]]
        case MoveEffect.PatternChosen(computedPatterns) => Map(-1 -> computedPatterns)
        case _                                          => Map.empty
    case None       => Map.empty

  def think(gb: GameBoard, gamePhase: GamePhase): Unit = gamePhase match
    // In WindPhase the bot has just to decide from the available patterns the one that gets closer to one tower of the opponent
    case WindPhase =>
      val newGb = handleWindPhase(gb)
      val lastPatternChosenMove = newGb.getCurrentPlayer.lastPatternChosen
      val availablePattern = handleMove(newGb, lastPatternChosenMove).values.last
      logger.info(availablePattern.toString())
    // Should be Waiting Phase
    case WaitingPhase =>
      computeAttackOrDefense(gb, botBehaviour)
//      val cardsToCompute: Map[Int, List[ILogicEffect]] = ???
//      val effect = CardsComputation(cardsToCompute)
//      val newGb = gameBoard.resolveEffect(effect)
//    case CardsChosen(cards: )

      logger.info(s"Bot is thinking... Attack or Defense: ${DecisionMaker.getAttackOrDefense}")
      logger.info(s"Objective tower: ${DecisionMaker.getObjectiveTower}")
    case _         =>

object Bot:
  def apply(moves: List[Move], hand: List[Card], botBehaviour: BotBehaviour) =
    new Bot(List.empty, List.empty, botBehaviour, Set.empty)
