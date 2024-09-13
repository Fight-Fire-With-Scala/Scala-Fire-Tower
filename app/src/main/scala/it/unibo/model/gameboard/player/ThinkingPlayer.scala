package it.unibo.model.gameboard.player

import it.unibo.controller.BotSubject
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType.CardDiscard
import it.unibo.controller.view.RefreshType.CardSelected
import it.unibo.controller.view.RefreshType.PhaseUpdate
import it.unibo.model.card.Card
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.core.IDefensiveCard
import it.unibo.model.effect.core.IOffensiveCard
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase._
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.gameboard.player.ThinkingPlayer.filterCardsBasedOnDecision
import it.unibo.model.logger
import it.unibo.model.prolog.decisionmaking.AttackDefense
import it.unibo.model.prolog.decisionmaking.DecisionMaker
import it.unibo.model.prolog.decisionmaking.DecisionMaker.computeAttackOrDefense

trait ThinkingPlayer extends Player:
  val botBehaviour: BotBehaviour
  val botObservable: Option[BotSubject]
  def think(controller: ModelController): Unit

  protected def thinkForWindPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForWindPhase")
    // In WindPhase the bot has just to decide from the available patterns
    // the one that gets closer to one tower of the opponent
//    val gb = controller.model.getGameBoard
//    val direction = gb.board.windDirection
//    val logicEffect = WindEffect.windEffectResolver.resolve(direction)
//    val effect = CardsComputation(Map(-1 -> List(logicEffect)))
//
//    val newGb = PatternEffect.patternEffectResolver.resolve(effect).resolve(gb).gameBoard
//
//    val lastCardsChosen = newGb.getCurrentPlayer.lastCardsChosen
//    val availablePattern = handleMove(gb, lastCardsChosen).values.last
//    logger.info(availablePattern.toString())

    // val patternApplicationEffect = PatternApplication(availablePatterns)

    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(WaitingPhase)))

  protected def thinkForWaitingPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForWaitingPhase")
    val gb = controller.model.getGameBoard
    computeAttackOrDefense(gb, botBehaviour)

    logger.info(s"[BOT] Attack or Defense: ${DecisionMaker.getAttackOrDefense}")
//    logger.info(s"[BOT] Objective tower: ${DecisionMaker.getObjectiveTower}")

    // bot does redraw cards only if attacking and not having attack cards
    val filteredCards = filterCardsBasedOnDecision(hand, DecisionMaker.getAttackOrDefense)
    if (filteredCards.isEmpty) controller.applyEffect(PhaseEffect(RedrawCardsPhase), CardDiscard)
    else controller.applyEffect(PhaseEffect(PlayStandardCardPhase), CardSelected)

    logger.info(filteredCards.toString())

  protected def thinkForRedrawCardPhase(using controller: ModelController): Unit = ???

  protected def thinkForPlayStandardCardPhase(using controller: ModelController): Unit = ???

  protected def thinkForDecisionPhase(using controller: ModelController): Unit =
    controller.applyEffect(PhaseEffect(PlaySpecialCardPhase), PhaseUpdate)
    controller.applyEffect(PhaseEffect(EndTurnPhase), PhaseUpdate)

  protected def thinkForPlaySpecialCardPhase(using controller: ModelController): Unit = ???

  protected def thinkForEndTurnPhase(using controller: ModelController): Unit = ???

object ThinkingPlayer:
  private def handleMove(
      gb: GameBoard,
      lastMove: Option[Move]
  ): Map[Int, Set[Map[Position, Token]]] = lastMove match
    case Some(move) => move.effect match
        case MoveEffect.CardsChosen(cards)              => cards
        case MoveEffect.PatternChosen(computedPatterns) => Map(-1 -> computedPatterns)
        case _                                          => Map.empty
    case None       => Map.empty

  private def filterCardsBasedOnDecision(hand: List[Card], decision: AttackDefense): List[Card] =
    decision match
      case AttackDefense.Attack  => hand.collect:
          case card if card.effect match
                case _: IOffensiveCard => true;
                case _                 => false
              => card
      case AttackDefense.Defense => hand.collect:
          case card if card.effect match
                case _: IDefensiveCard => true;
                case _                 => false
              => card
