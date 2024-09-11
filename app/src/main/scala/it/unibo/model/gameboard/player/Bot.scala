package it.unibo.model.gameboard.player

import it.unibo.model.cards.Card
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.{GameBoard, GamePhase}
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

  def think(gameBoard: GameBoard, gamePhase: GamePhase): Unit = gamePhase match
    // In WindPhase the bot has just to decide from the available patterns the one that gets closer to one tower of the opponent

    // Should be Waiting Phase
    case WindPhase =>
      computeAttackOrDefense(gameBoard)
      logger.info(s"Bot is thinking... Attack or Defense: ${DecisionMaker.getAttackOrDefense}")
      logger.info(s"Objective tower: ${DecisionMaker.getObjectiveTower}")
    case _         =>

object Bot:
  def apply(moves: List[Move], hand: List[Card], botBehaviour: BotBehaviour) =
    new Bot(List.empty, List.empty, botBehaviour, Set.empty)
