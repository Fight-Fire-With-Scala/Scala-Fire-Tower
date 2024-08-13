package it.unibo.model.settings

import it.unibo.model.players.{Person, Player}

enum CardSet:
  case Base, Expanded, Full

enum BotBehaviour:
  case Aggressive, Balanced, Defensive

enum GameMode:
  case HumanVsHuman, HumanVsBot

case class Settings(
    gameMode: GameMode = GameMode.HumanVsHuman,
    cardSet: CardSet = CardSet.Base,
    botBehaviour: Option[BotBehaviour] = None,
    playerOneName: String = "Player 1",
    playerTwoName: Option[String] = None
) derives CanEqual:
  private val playerOne: Player = Person(playerOneName, List.empty, List.empty)
  private val playerTwo: Player = gameMode match
    case GameMode.HumanVsHuman => Player(playerTwoName.getOrElse("Player 2"))
    case GameMode.HumanVsBot   => Player.bot

  def getPlayerOne: Player = playerOne
  def getPlayerTwo: Player = playerTwo
