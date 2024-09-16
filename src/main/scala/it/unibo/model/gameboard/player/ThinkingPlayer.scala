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
import it.unibo.model.effect.core.{ given_Conversion_GameBoard_GameBoardEffect, DefensiveEffect, ICardEffect, ILogicEffect, OffensiveEffect, SingleStepEffect }
import it.unibo.model.effect.core.ICardEffect.given_Conversion_ICardEffect_ILogicEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.{ patternEffectSolver, BotComputation, PatternApplication }
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
    val tmpGb = controller.model.getGameBoard
    val newGb = tmpGb.solveEffect(effect)
    logger.info(s"[BOT] Thinking Player Gameboard ${newGb.getCurrentPlayer.moves}")
    controller.model.setGameBoard(newGb)
    val lastBotChoice = newGb.getCurrentPlayer.lastBotChoice
    logger.info(s"[BOT] Last Bot Choice $lastBotChoice")

    val (_, chosenPattern) = handleMove(lastBotChoice)
    logger.info(s"[PATTERN] $chosenPattern")
    val appEffect = PatternApplication(chosenPattern)
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
//      controller.applyEffect(PhaseEffect(RedrawCardsPhase), CardDiscard)
      botObservable.get.onNext(UpdateGamePhase(PhaseEffect(RedrawCardsPhase)))
    else
//      controller.applyEffect(PhaseEffect(PlayStandardCardPhase), CardSelected)
      botObservable.get.onNext(UpdateGamePhase(PhaseEffect(PlayStandardCardPhase)))

  protected def thinkForRedrawCardPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForRedrawCardPhase")
    logger.info(s"[BOT] My hand is: $hand")
    val numberOfCardsToDraw = hand.length
    val discardCardEffect   = HandEffect.DiscardCard(hand.map(_.id))
    val drawCardEffect      = HandEffect.DrawCard(numberOfCardsToDraw)
    val gb                  = controller.model.getGameBoard
    val newGb               = gb.solveEffect(discardCardEffect).solveEffect(drawCardEffect)
    controller.model.setGameBoard(newGb)
    logger.info(s"[BOT] My hand is: ${newGb.getCurrentPlayer.hand}")

    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(DecisionPhase)))

  protected def thinkForPlayStandardCardPhase(using controller: ModelController): Unit =
    logger.info(s"[BOT] My hand is: $hand")
    logger.info("[BOT] thinkForPlayStandardCardPhase")
    val decision = DecisionMaker.getAttackOrDefense

//    val offensiveEffects = effects.flatMap(_.computations).filter {
//      case _: OffensiveEffect => true
//      case _ => false
//    }.map {
//      case oe: OffensiveEffect => oe
//    }
//
//    val defensiveEffects = effects.flatMap(_.computations).filter {
//      case _: DefensiveEffect => true
//      case _ => false
//    }.map {
//      case de: DefensiveEffect => de
//    }

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

    val filteredCards = filterCardsBasedOnDecision(hand, DecisionMaker.getAttackOrDefense)
    val effectsPrev =
      filteredCards.map(card => card.id -> List(ICardEffect.convert(card.effect))).toMap

    logger.info(s"[BOT] Effects: $effectsPrev")
    logger.info("[BOT] Effects: " + effects)
    val botComputation = BotComputation(effects)
    val gb             = controller.model.getGameBoard
    val newGb          = gb.solveEffect(botComputation)
    controller.model.setGameBoard(newGb)
    val lastBotChoice = newGb.getCurrentPlayer.lastBotChoice
    logger.info(s"[BOT] My move is: $lastBotChoice")
    val (_, chosenPattern) = handleMove(lastBotChoice)
    logger.info(s"[PATTERN] $chosenPattern")

    val appEffect = PatternApplication(chosenPattern)
    controller.applyEffect(appEffect, RefreshType.PatternChosen)
    logger.info(s"[BOT] My hand is: ${controller.model.getGameBoard.getCurrentPlayer.hand}")

    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(DecisionPhase)))

  protected def thinkForDecisionPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForDecisionPhase")
    if isFireTokenInTowerArea(controller.model.getGameBoard) then
      botObservable.get.onNext(UpdateGamePhase(PhaseEffect(PlaySpecialCardPhase)))
    else botObservable.get.onNext(UpdateGamePhase(PhaseEffect(EndTurnPhase)))

  protected def thinkForPlaySpecialCardPhase(using controller: ModelController): Unit =
    logger.info("[BOT] thinkForPlaySpecialCardPhase")
    extraCard match
      case Some(card) =>
        val gb      = controller.model.getGameBoard
        val effects = Map(card.id -> List(ICardEffect.convert(card.effect)))
        DecisionMaker.setObjectiveTower(gb.getCurrentPlayer.towerPositions.map(_.position))
        val botComputation = BotComputation(effects)
        val newGb          = gb.solveEffect(botComputation)
        controller.model.setGameBoard(newGb)
        val lastBotChoice = newGb.getCurrentPlayer.lastBotChoice
        logger.info(s"[BOT] My move is: $lastBotChoice")
        val (_, chosenPattern) = handleMove(lastBotChoice)
        logger.info(s"[PATTERN] $chosenPattern")

        val appEffect = PatternApplication(chosenPattern)
        controller.applyEffect(appEffect, RefreshType.PatternChosen)
        logger.info(
          s"[BOT] My extra hand is: ${controller.model.getGameBoard.getCurrentPlayer.extraCard.isEmpty}"
        )
    botObservable.get.onNext(UpdateGamePhase(PhaseEffect(EndTurnPhase)))

object ThinkingPlayer:
  private def handleMove(lastMove: Option[Move]): (Int, Map[Position, Token]) = lastMove match
    case Some(move) =>
      move.effect match
        case MoveEffect.BotChoice(cardId, patternChosen) => cardId -> patternChosen
        case MoveEffect.PatternApplied(appliedPattern)   => -1     -> appliedPattern
        case _                                           => -1     -> Map.empty
    case None => -1 -> Map.empty

  private def filterCardsBasedOnDecision(hand: List[Card], decision: AttackDefense): List[Card] =
    decision match
      case AttackDefense.Attack =>
        hand.collect:
          case card if card.effect.computations.exists {
                case _: OffensiveEffect => true
                case _                  => false
              } =>
            card
      case AttackDefense.Defense =>
        hand.collect:
          case card if card.effect.computations.exists {
                case _: DefensiveEffect => true
                case _                  => false
              } =>
            card

  private def isFireTokenInTowerArea(gb: GameBoard): Boolean =
    val towerPositions = gb.board.grid.getTowerCells(gb.getCurrentPlayer.towerPositions)
    towerPositions.exists(pos =>
      gb.board.grid.getToken(pos).exists {
        case token: ConcreteToken => token == ConcreteToken.Fire
        case _                    => false
      }
    )
