package it.unibo.model.gameboard

import it.unibo.model.cards.Card
import it.unibo.model.effects.{GameBoardEffect, PatternEffect}
import it.unibo.model.effects.core.{GameBoardEffectResolver, GameLogicEffectResolver, IGameEffect}
import it.unibo.model.effects.hand.HandEffect
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.grid.TowerPosition
import it.unibo.model.gameboard.player.{Bot, Person, Player, PlayerInstance, PlayerManager}
import it.unibo.model.logger

case class GameBoard(
    board: Board,
    deck: Deck,
    player1: Player,
    player2: Player,
    private val playerManager: PlayerManager = PlayerManager(),
    turnNumber: Int = 0,
    gamePhase: GamePhase = WindPhase
):

  def getCurrentPlayer: Player = playerManager.getCurrentState match
    case PlayerInstance.Player1 => player1
    case PlayerInstance.Player2 => player2

  def getOpponent: Player = playerManager.getCurrentState match
    case PlayerInstance.Player1 => player2
    case PlayerInstance.Player2 => player1

  def updateCurrentPlayer(player: Player): GameBoard = playerManager.getCurrentState match
    case PlayerInstance.Player1 => copy(player1 = player)
    case PlayerInstance.Player2 => copy(player2 = player)

  def changePlayer(): GameBoard =
    playerManager.toggle()
    this

  def resolveEffect(effect: IGameEffect): GameBoard = getEffectResolver(effect)
    .resolve(GameBoardEffect(this)).gameBoard

  private def getEffectResolver(effect: IGameEffect): GameBoardEffectResolver =
    logger.info(s"Effect to resolve $effect")
    effect match
      case ef: PhaseEffect             => PhaseEffect.phaseEffectResolver.resolve(ef)
      case ef: HandEffect              => HandEffect.handEffectResolver.resolve(ef) match
          case e: GameLogicEffectResolver =>
            val patternEffect = e.resolve(GameBoardEffect(this))
            PatternEffect.patternEffectResolver.resolve(patternEffect)
          case e: GameBoardEffectResolver => e
      case ef: PatternEffect           => PatternEffect.patternEffectResolver.resolve(ef)
      case ef: GameBoardEffectResolver => ef

object GameBoard:
  def apply(player1: Player, player2: Player): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    val updatedPlayer1 = player1 match
      case p: Person => p
          .copy(towerPositions = Set(TowerPosition.TOP_RIGHT, TowerPosition.BOTTOM_LEFT))
      case b: Bot    => b
          .copy(towerPositions = Set(TowerPosition.TOP_RIGHT, TowerPosition.BOTTOM_LEFT))

    val updatedPlayer2 = player2 match
      case p: Person => p
          .copy(towerPositions = Set(TowerPosition.TOP_LEFT, TowerPosition.BOTTOM_RIGHT))
      case b: Bot    => b
          .copy(towerPositions = Set(TowerPosition.TOP_LEFT, TowerPosition.BOTTOM_RIGHT))

    val deck = Deck("cards.yaml")
    GameBoard(b, deck, updatedPlayer1, updatedPlayer2)
