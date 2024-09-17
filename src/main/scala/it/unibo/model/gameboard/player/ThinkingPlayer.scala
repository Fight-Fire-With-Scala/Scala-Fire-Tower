package it.unibo.model.gameboard.player

import it.unibo.controller.BotSubject
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.model.ModelController
import it.unibo.model.ModelModule.Model
import it.unibo.model.card.Card
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.card.{ FirebreakEffect, WindEffect }
import it.unibo.model.effect.core.{ DefensiveEffect, ICardEffect, IGameEffect, ILogicEffect, OffensiveEffect, SingleStepEffect }
import it.unibo.model.effect.core.ICardEffect.given_Conversion_ICardEffect_ILogicEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.{ BotComputation, PatternApplication }
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.{ ConcreteToken, Position, Token }
import it.unibo.model.gameboard.player.ThinkingPlayer.handleMove
import it.unibo.model.logger
import it.unibo.model.prolog.decisionmaking.AttackDefense
import it.unibo.model.prolog.decisionmaking.DecisionMaker
import it.unibo.model.prolog.decisionmaking.DecisionMaker.computeAttackOrDefense

trait ISendMessages:
  protected def onUpdateGamePhaseRequest(phaseEffect: PhaseEffect): Unit

trait ThinkingPlayer extends Player with ISendMessages with IMakeDecision:
  val botBehaviour: BotBehaviour
  val botObservable: Option[BotSubject]
  def think(model: Model): Unit

  def applyEffect(model: Model, ef: IGameEffect): Unit =
    val newGb = model.getGameBoard.solveEffect(ef)
    model.setGameBoard(newGb)

  protected def thinkForWindPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForWindPhase")

    // In WindPhase the bot has just to decide from the available patterns
    // the one that gets closer to one tower of the opponent
    val gb          = model.getGameBoard
    val direction   = gb.board.windDirection
    val logicEffect = WindEffect.windEffectSolver.solve(direction)
    val effect      = BotComputation(Map(-1 -> List(logicEffect)))
    DecisionMaker.setObjectiveTower(gb.getOpponent.towerPositions.map(_.position))
    val tmpGb = model.getGameBoard
    val newGb = tmpGb.solveEffect(effect)
    logger.info(s"[BOT] Thinking Player Gameboard ${newGb.getCurrentPlayer.moves}")
    model.setGameBoard(newGb)
    val lastBotChoice = newGb.getCurrentPlayer.lastBotChoice
    logger.info(s"[BOT] Last Bot Choice $lastBotChoice")

    val (_, chosenPattern) = handleMove(lastBotChoice)
    logger.info(s"[PATTERN] $chosenPattern")
    val appEffect = PatternApplication(chosenPattern)
    applyEffect(model, appEffect)

    // val gbAfterApplication = PatternEffect.patternEffectSolver.solve(appEffect).solve(gb).gameBoard
    // controller.model.setGameBoard(gbAfterApplication)

    onUpdateGamePhaseRequest(PhaseEffect(WaitingPhase))

  protected def thinkForWaitingPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForWaitingPhase")
    val gb = model.getGameBoard
    computeAttackOrDefense(gb, botBehaviour)
    logger.info(s"[BOT] Attack or Defense: ${DecisionMaker.getAttackOrDefense}")
    logger.info(s"[BOT] Objective tower: ${DecisionMaker.getObjectiveTower.head}")

    val filteredCards = filterCardsBasedOnDecision(hand, DecisionMaker.getAttackOrDefense)

    val containsDeReforest =
      filteredCards.exists(card => card.effect.effectId == FirebreakEffect.DeReforest.effectId)

    if filteredCards.isEmpty || (containsDeReforest && !isFireBreakTokenInBoard(gb)) then
      onUpdateGamePhaseRequest(PhaseEffect(RedrawCardsPhase))
    else onUpdateGamePhaseRequest(PhaseEffect(PlayStandardCardPhase))

  protected def thinkForRedrawCardPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForRedrawCardPhase")
    logger.info(s"[BOT] My hand is: $hand")
    val numberOfCardsToDraw = hand.length
    val discardCardEffect   = HandEffect.DiscardCard(hand.map(_.id))
    val drawCardEffect      = HandEffect.DrawCard(numberOfCardsToDraw)
    val gb                  = model.getGameBoard
    val newGb               = gb.solveEffect(discardCardEffect).solveEffect(drawCardEffect)
    model.setGameBoard(newGb)
    logger.info(s"[BOT] My hand is: ${newGb.getCurrentPlayer.hand}")

    onUpdateGamePhaseRequest(PhaseEffect(DecisionPhase))

  protected def thinkForPlayStandardCardPhase(using model: Model): Unit =
    logger.info(s"[BOT] My hand is: $hand")
    logger.info("[BOT] thinkForPlayStandardCardPhase")
    val decision = DecisionMaker.getAttackOrDefense

    val effects = filterCardsBasedOnDecision(hand, decision)
      .map { card =>
        val filteredComputations = decision match {
          case AttackDefense.Attack =>
            card.effect.computations.collect { case e: OffensiveEffect =>
              SingleStepEffect(List(e)).asInstanceOf[ILogicEffect]
            }
          case AttackDefense.Defense =>
            card.effect.computations.collect { case e: DefensiveEffect =>
              SingleStepEffect(List(e)).asInstanceOf[ILogicEffect]
            }
        }
        card.id -> filteredComputations
      }
      .filter(_._2.nonEmpty)
      .toMap

    logger.info("[BOT] Effects: " + effects)
    val botComputation = BotComputation(effects)
    val gb             = model.getGameBoard
    val newGb          = gb.solveEffect(botComputation)
    model.setGameBoard(newGb)
    val lastBotChoice = newGb.getCurrentPlayer.lastBotChoice
    logger.info(s"[BOT] My move is: $lastBotChoice")
    val (_, chosenPattern) = handleMove(lastBotChoice)
    logger.info(s"[PATTERN] $chosenPattern")

    val appEffect = PatternApplication(chosenPattern)
    applyEffect(model, appEffect)
    logger.info(s"[BOT] My hand is: ${model.getGameBoard.getCurrentPlayer.hand}")

    onUpdateGamePhaseRequest(PhaseEffect(DecisionPhase))

  protected def thinkForDecisionPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForDecisionPhase")
    if isFireTokenInTowerArea(model.getGameBoard) then
      onUpdateGamePhaseRequest(PhaseEffect(PlaySpecialCardPhase))
    else onUpdateGamePhaseRequest(PhaseEffect(EndTurnPhase))

  protected def thinkForPlaySpecialCardPhase(using model: Model): Unit =
    logger.info("[BOT] thinkForPlaySpecialCardPhase")
    extraCard match
      case Some(card) =>
        val gb      = model.getGameBoard
        val effects = Map(card.id -> List(ICardEffect.convert(card.effect)))
        DecisionMaker.setObjectiveTower(gb.getCurrentPlayer.towerPositions.map(_.position))
        val botComputation = BotComputation(effects)
        val newGb          = gb.solveEffect(botComputation)
        model.setGameBoard(newGb)
        val lastBotChoice = newGb.getCurrentPlayer.lastBotChoice
        logger.info(s"[BOT] My move is: $lastBotChoice")
        val (_, chosenPattern) = handleMove(lastBotChoice)
        logger.info(s"[PATTERN] $chosenPattern")

        val appEffect = PatternApplication(chosenPattern)
        applyEffect(model, appEffect)

      case None =>
    onUpdateGamePhaseRequest(PhaseEffect(EndTurnPhase))

object ThinkingPlayer:
  private def handleMove(lastMove: Option[Move]): (Int, Map[Position, Token]) = lastMove match
    case Some(move) =>
      move.effect match
        case MoveEffect.BotChoice(cardId, patternChosen) => cardId -> patternChosen
        case MoveEffect.PatternApplied(appliedPattern)   => -1     -> appliedPattern
        case _                                           => -1     -> Map.empty
    case None => -1 -> Map.empty
