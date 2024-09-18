package it.unibo.model.gameboard.player

import it.unibo.controller.BotSubject
import it.unibo.model.ModelModule.Model
import it.unibo.model.card.Card
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.card.{ FirebreakEffect, WindEffect }
import it.unibo.model.effect.core.{ DefensiveEffect, ICardEffect, ILogicEffect, OffensiveEffect, PatternLogicEffect }
import it.unibo.model.effect.core.ICardEffect.given_Conversion_ICardEffect_ILogicEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.{ BotComputation, PatternApplication }
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.player.ThinkingPlayer.handleMove
import it.unibo.model.logger
import it.unibo.model.prolog.decisionmaking.AttackDefense
import it.unibo.model.prolog.decisionmaking.DecisionMaker
import it.unibo.model.prolog.decisionmaking.DecisionMaker.computeAttackOrDefense
import it.unibo.model.gameboard.Pattern

trait ISendMessages:
  protected def onUpdateGamePhaseRequest(model: Model, phaseEffect: PhaseEffect): Unit

trait ThinkingPlayer extends Player with ISendMessages with IMakeDecision:
  val botBehaviour: BotBehaviour
  val botObservable: Option[BotSubject]
  def think(model: Model): Unit

  private def handleMoveAndApplyEffect(model: Model, patternEffect: PatternEffect): Unit =
    val gb       = model.getGameBoard
    val gbSolved = gb.solveEffect(patternEffect)
    model.setGameBoard(gbSolved)
    val lastMove = gbSolved.getCurrentPlayer.lastBotChoice
    logger.info(s"[BOT] Last move: $lastMove")
    val (cardId, chosenPattern) = handleMove(lastMove)
    val appEffect               = PatternApplication(chosenPattern)
    val newGb                   = model.getGameBoard.solveEffect(appEffect)
    model.setGameBoard(newGb)

  protected def thinkForWindPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForWindPhase")
    val gb          = model.getGameBoard
    val direction   = gb.board.windDirection
    val logicEffect = WindEffect.windEffectSolver.solve(direction)
    DecisionMaker.setObjectiveTower(gb.getOpponent.towerPositions.map(_.position))
    val effect = BotComputation(Map(None -> List(logicEffect)))
    handleMoveAndApplyEffect(model, effect)
    onUpdateGamePhaseRequest(model, PhaseEffect(WaitingPhase))

  protected def thinkForWaitingPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForWaitingPhase")
    val gb = model.getGameBoard
    computeAttackOrDefense(gb, botBehaviour)
    val filteredCards = filterCardsBasedOnDecision(hand, DecisionMaker.getAttackOrDefense)
    val containsDeReforest =
      filteredCards.exists(card => card.effect.effectId == FirebreakEffect.DeReforest.effectId)
    if filteredCards.isEmpty || (containsDeReforest && !isFireBreakTokenInBoard(gb)) then
      onUpdateGamePhaseRequest(model, PhaseEffect(RedrawCardsPhase))
    else onUpdateGamePhaseRequest(model, PhaseEffect(PlayStandardCardPhase))

  protected def thinkForRedrawCardPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForRedrawCardPhase")
    logger.info(s"[BOT] hand before: $hand")
    val numberOfCardsToDraw = hand.length
    val discardCardEffect   = HandEffect.DiscardCard(hand.map(_.id))
    val drawCardEffect      = HandEffect.DrawCard(numberOfCardsToDraw)
    val gb                  = model.getGameBoard
    val newGb               = gb.solveEffect(discardCardEffect).solveEffect(drawCardEffect)
    model.setGameBoard(newGb)
    logger.info(s"[BOT] hand after: ${newGb.getCurrentPlayer.hand}")
    onUpdateGamePhaseRequest(model, PhaseEffect(DecisionPhase))

  protected def thinkForPlayStandardCardPhase(using model: Model): Unit =
    val decision = DecisionMaker.getAttackOrDefense

    val effects = filterCardsBasedOnDecision(hand, decision)
      .map { card =>
        val filteredComputations = decision match {
          case AttackDefense.Attack =>
            card.effect.computations.collect { case e: OffensiveEffect =>
              PatternLogicEffect(List(e)).asInstanceOf[ILogicEffect]
            }
          case AttackDefense.Defense =>
            card.effect.computations.collect { case e: DefensiveEffect =>
              PatternLogicEffect(List(e)).asInstanceOf[ILogicEffect]
            }
        }
        Option(card.id) -> filteredComputations
      }
      .filter(_._2.nonEmpty)
      .toMap
    val botComputation = BotComputation(effects)
    handleMoveAndApplyEffect(model, botComputation)
    onUpdateGamePhaseRequest(model, PhaseEffect(DecisionPhase))

  protected def thinkForDecisionPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForDecisionPhase")
    if isFireTokenInTowerArea(model.getGameBoard) then
      onUpdateGamePhaseRequest(model, PhaseEffect(PlaySpecialCardPhase))
    else onUpdateGamePhaseRequest(model, PhaseEffect(EndTurnPhase))

  protected def thinkForPlaySpecialCardPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForPlaySpecialCardPhase")
    extraCard match
      case Some(card) =>
        val gb      = model.getGameBoard
        val effects = Map(Option(card.id) -> List(ICardEffect.convert(card.effect)))
        DecisionMaker.setObjectiveTower(gb.getCurrentPlayer.towerPositions.map(_.position))
        val botComputation = BotComputation(effects)
        handleMoveAndApplyEffect(model, botComputation)

      case None =>
    onUpdateGamePhaseRequest(model, PhaseEffect(EndTurnPhase))

object ThinkingPlayer:
  private def handleMove(lastMove: Option[Move]): (Option[Int], Pattern) =
    lastMove match
      case Some(move) =>
        move.effect match
          case MoveEffect.BotChoice(cardId, patternChosen) => Some(cardId) -> patternChosen
          case MoveEffect.PatternApplied(appliedPattern)   => None         -> appliedPattern
          case _                                           => None         -> Map.empty
      case None => None -> Map.empty
