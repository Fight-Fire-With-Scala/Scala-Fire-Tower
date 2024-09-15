package it.unibo.model.gameboard

import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.GameBoardConfig.GameMode
import it.unibo.model.gameboard.player.Person
import it.unibo.model.gameboard.player.Player

case class GameBoardConfig(
    gameMode: GameMode = GameMode.HumanVsHuman,
    cardSet: GameBoardConfig.CardSet = GameBoardConfig.CardSet.Base,
    botBehaviour: Option[BotBehaviour] = None,
    playerOneName: String = "Player 1",
    playerTwoName: Option[String] = None
) derives CanEqual:
  private val playerOne: Player = Person(playerOneName, List.empty, List.empty)
  private val playerTwo: Player = gameMode match
    case GameMode.HumanVsHuman => Player(playerTwoName.getOrElse("Player 2"))
    case GameMode.HumanVsBot =>
      botBehaviour match
        case Some(b) => Player.bot(b)
        case None    => Player.bot(Balanced)

  def getPlayerOne: Player = playerOne
  def getPlayerTwo: Player = playerTwo

object GameBoardConfig:
  enum CardSet:
    case Base, Expanded, Full

  enum BotBehaviour(val biasFactor: Int):
    case Aggressive extends BotBehaviour(7)
    case Balanced extends BotBehaviour(0)
    case Defensive extends BotBehaviour(7)

  enum GameMode:
    case HumanVsHuman, HumanVsBot
