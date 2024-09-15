package it.unibo.model.gameboard.player

import it.unibo.controller.BotSubject
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType
import it.unibo.controller.view.RefreshType.CardDiscard
import it.unibo.controller.view.RefreshType.CardSelected
import it.unibo.model.card.Card
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.effect.core.{ given_Conversion_GameBoard_GameBoardEffect, given_Conversion_ICardEffect_ILogicEffect, given_Conversion_List_List, ICardEffect, IDefensiveCard, ILogicEffect, IOffensiveCard }
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.{ BotComputation, PatternApplication }
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.{ ConcreteToken, Position, Token }
import it.unibo.model.gameboard.player.ThinkingPlayer.{ filterCardsBasedOnDecision, handleMove, isFireTokenInTowerArea }
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
    val gb          = controller.model.getGameBoard
    val direction   = gb.board.windDirection
    val logicEffect = WindEffect.windEffectSolver.solve(direction)
    val effect      = BotComputation(Map(-1 -> List(logicEffect)))
    DecisionMaker.setObjectiveTower(gb.getOpponent.towerPositions.map(_.position))
    val gbAfterChoice = PatternEffect.patternEffectSolver.solve(effect).solve(gb).gameBoard
    val lastBotChoice = gbAfterChoice.getCurrentPlayer.lastBotChoice
    val chosenPattern = handleMove(gb, lastBotChoice)
    logger.info(s"[PATTERN] $chosenPattern")

    val appEffect = PatternApplication(chosenPattern.values.head)
    controller.applyEffect(appEffect, RefreshType.PatternChosen)

    // val gbAfterApplication = PatternEffect.patternEffectSolver.solve(appEffect).solve(gb).gameBoard
    // controller.model.setGameBoard(gbAfterApplication)

    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(WaitingPhase)))

  protected def thinkForWaitingPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForWaitingPhase")
    val gb = controller.model.getGameBoard
    computeAttackOrDefense(gb, botBehaviour)
    logger.info(s"[BOT] Attack or Defense: ${DecisionMaker.getAttackOrDefense}")
    logger.info(s"[BOT] Objective tower: ${DecisionMaker.getObjectiveTower.head}")

    // bot does redraw cards only if attacking and not having attack cards
    val filteredCards = filterCardsBasedOnDecision(hand, DecisionMaker.getAttackOrDefense)

    if filteredCards.isEmpty then
      controller.applyEffect(PhaseEffect(RedrawCardsPhase), CardDiscard)
      botObservable.get.onNext(UpdateGamePhase(PhaseEffect(RedrawCardsPhase)))
    else
      controller.applyEffect(PhaseEffect(PlayStandardCardPhase), CardSelected)
      botObservable.get.onNext(UpdateGamePhase(PhaseEffect(PlayStandardCardPhase)))

  protected def thinkForRedrawCardPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForRedrawCardPhase")
    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(DecisionPhase)))

  protected def thinkForPlayStandardCardPhase(using controller: ModelController): Unit =
    logger.info(s"[BOT] My hand is: $hand")
    logger.info("[BOT] thinkForPlayStandardCardPhase")
    val filteredCards = filterCardsBasedOnDecision(hand, DecisionMaker.getAttackOrDefense)
    val gb            = controller.model.getGameBoard
    val effects: Map[Int, List[ICardEffect]] =
      filteredCards.map(card => card.id -> List(card.effect)).toMap
    val botComputation = BotComputation(effects)
    val gbAfterChoice  = PatternEffect.patternEffectSolver.solve(botComputation).solve(gb).gameBoard
    val lastBotChoice  = gbAfterChoice.getCurrentPlayer.lastBotChoice
    val chosenPattern  = handleMove(gb, lastBotChoice)
    logger.info(s"[PATTERN] $chosenPattern")
    logger.info(s"[BOT] My hand is: $hand")

    val appEffect = PatternApplication(chosenPattern.values.head)
    controller.applyEffect(appEffect, RefreshType.PatternChosen)
    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(DecisionPhase)))

  protected def thinkForDecisionPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForDecisionPhase")
    if isFireTokenInTowerArea(controller.model.getGameBoard) then
      botObservable.get.onNext(UpdateGamePhase(PhaseEffect(PlaySpecialCardPhase)))
    else botObservable.get.onNext(UpdateGamePhase(PhaseEffect(EndTurnPhase)))

  protected def thinkForPlaySpecialCardPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForPlaySpecialCardPhase")
    // if u have secchio u should play it here
    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(EndTurnPhase)))

object ThinkingPlayer:
  private def handleMove(
      gb: GameBoard,
      lastMove: Option[Move]
  ): Map[Int, Map[Position, Token]] = lastMove match
    case Some(move) =>
      move.effect match
        case MoveEffect.BotChoice(cards)               => cards
        case MoveEffect.PatternApplied(appliedPattern) => Map(-1 -> appliedPattern)
        case _                                         => Map.empty
    case None => Map.empty

  private def filterCardsBasedOnDecision(hand: List[Card], decision: AttackDefense): List[Card] =
    decision match
      case AttackDefense.Attack =>
        hand.collect:
          case card if card.effect match
                case _: IOffensiveCard => true;
                case _                 => false
              =>
            card
      case AttackDefense.Defense =>
        hand.collect:
          case card if card.effect match
                case _: IDefensiveCard => true;
                case _                 => false
              =>
            card

  private def isFireTokenInTowerArea(gb: GameBoard): Boolean =
    val towerPositions = gb.board.grid.getTowerCells(gb.getCurrentPlayer.towerPositions)
    towerPositions.exists(pos =>
      gb.board.grid.getToken(pos).exists {
        case token: ConcreteToken => token == ConcreteToken.Fire
        case _                    => false
      }
    )
