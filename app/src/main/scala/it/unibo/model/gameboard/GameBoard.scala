package it.unibo.model.gameboard

import it.unibo.model.cards.Card
import it.unibo.model.cards.choices.{GameChoice, StepChoice, WindChoice}
import it.unibo.model.cards.choices.StepChoice.PatternComputation
import it.unibo.model.cards.effects.GameEffect
import it.unibo.model.cards.resolvers.{ChoiceResultResolver, EffectResolver, InstantResolver, InstantWindResolver, MetaResolver, MultiStepResolver, PatternApplicationResolver, PatternComputationResolver, StepResolver, WindResolver}
import it.unibo.model.cards.types.CanBePlayedAsExtra
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.board.Board

import it.unibo.model.gameboard.grid.{Grid, Position, TowerPosition}
import it.unibo.model.gameboard.player.{Bot, Person, Player, PlayerInstance, PlayerManager}

enum GamePhase:
  case WindPhase, RedrawCardsPhase, PlayStandardCardPhase, WaitingPhase, PlaySpecialCardPhase,
    EndTurnPhase, DecisionPhase

case class GameBoard(
    board: Board,
    deck: Deck,
    player1: Player,
    player2: Player,
    playerManager: PlayerManager = PlayerManager(),
    turnNumber: Int = 0,
    gamePhase: GamePhase = WindPhase
):

  def getCurrentPlayer(): Player = playerManager.getCurrentState match
    case PlayerInstance.Player1 => player1
    case PlayerInstance.Player2 => player2

  def getOpponent(): Player = playerManager.getCurrentState match
    case PlayerInstance.Player1 => player2
    case PlayerInstance.Player2 => player1

  def updateCurrentPlayer(player: Player): GameBoard = playerManager.getCurrentState match
    case PlayerInstance.Player1 => copy(player1 = player)
    case PlayerInstance.Player2 => copy(player2 = player)

  def changeTurn(): GameBoard =
    playerManager.toggle()
    copy(gamePhase = WindPhase, turnNumber = turnNumber + 1)

  def resolveCardPlayed(card: Card, choice: GameChoice): GameBoard =
    val resolver = card.cardType.effectType.effect
    val effect = getGameEffect(resolver, choice)
    val newDeck = card.cardType.effectType match
      case _: CanBePlayedAsExtra => deck
      case _                     => deck.copy(playedCards = card :: deck.playedCards)
    copy(board = board.applyEffect(effect), deck = newDeck)

  private def getGameEffect(
      resolver: MetaResolver[? <: GameChoice, ? <: EffectResolver],
      choice: GameChoice
  ): Option[GameEffect] = resolver match
    case r: WindResolver      => choice match
        case c: WindChoice => handleWind(r.resolve(c))
        case _             => None
    case r: MultiStepResolver => choice match
        case c: StepChoice => handleStep(r.resolve(c))
        case _             => None

  private def handleWind(resolver: ChoiceResultResolver) = resolver match
    case mr: MultiStepResolver   => handleStep(mr.resolve(StepChoice.PatternComputation))
    case ir: InstantWindResolver => Some(ir.resolve())

  private def handleFirebreak(resolver: ChoiceResultResolver) = resolver match
    case mr: MultiStepResolver => handleStep(mr.resolve(StepChoice.PatternComputation))
    case _: InstantResolver[_] => None

  private def handleStep(resolver: StepResolver): Option[GameEffect] = resolver match
    case sr: PatternComputationResolver => Some(sr.getAvailableMoves(board))
    case sr: PatternApplicationResolver => Some(sr.applyMove(board))

object GameBoard:
  def apply(): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    val player1 = Person("", List.empty, List.empty)
    val player2 = Person("", List.empty, List.empty)
    GameBoard(b, Deck("cards.yaml"), player1, player2)

  def apply(player1: Player, player2: Player): GameBoard =
    val b = Board.withRandomWindAndStandardGrid
    val updatedPlayer1 = player1 match
      case p: Person => p.copy(towerPositions = Set(TowerPosition.TOP_RIGHT, TowerPosition.BOTTOM_LEFT))
      case b: Bot => b.copy(towerPositions = Set(TowerPosition.TOP_RIGHT, TowerPosition.BOTTOM_LEFT))

    val updatedPlayer2 = player2 match
      case p: Person => p.copy(towerPositions = Set(TowerPosition.TOP_LEFT, TowerPosition.BOTTOM_RIGHT))
      case b: Bot => b.copy(towerPositions = Set(TowerPosition.TOP_LEFT, TowerPosition.BOTTOM_RIGHT))

    GameBoard(b, Deck("cards.yaml"), updatedPlayer1, updatedPlayer2)

  private def updateTowerPosition(player: Player, positions: Set[TowerPosition]): Player = player match
    case p: Person => p.copy(towerPositions = positions)
    case b: Bot => b.copy(towerPositions = positions)