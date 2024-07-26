package it.unibo.model.settings

import it.unibo.model.players.{Bot, Person}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SettingsSpec extends AnyFlatSpec with Matchers:

  "Settings" should "correctly instantiate two Persons for HumanVsHuman mode" in {
    val settings = Settings(
      gameMode = GameMode.HumanVsHuman,
      playerOneName = "Alice",
      playerTwoName = Some("Bob")
    )
    settings.getPlayerOne shouldBe a[Person]
    settings.getPlayerTwo shouldBe a[Person]
    settings.getPlayerOne.name shouldEqual "Alice"
    settings.getPlayerTwo.name shouldEqual "Bob"
  }

  it should "correctly instantiate a Person and a Bot for HumanVsBot mode" in {
    val settings = Settings(gameMode = GameMode.HumanVsBot, playerOneName = "Alice")
    settings.getPlayerOne shouldBe a[Person]
    settings.getPlayerTwo shouldBe a[Bot]
    settings.getPlayerOne.name shouldEqual "Alice"
    settings.getPlayerTwo.name shouldEqual "BOT" // Assuming BOT is the default name for bots
  }

  it should "correctly handle bot behaviour in HumanVsBot mode" in {
    val settings =
      Settings(gameMode = GameMode.HumanVsBot, botBehaviour = Some(BotBehaviour.Aggressive))
    settings.botBehaviour should contain(BotBehaviour.Aggressive)
  }

  it should "have correct default values" in {
    val defaultSettings = Settings()
    defaultSettings.gameMode shouldEqual GameMode.HumanVsHuman
    defaultSettings.cardSet shouldEqual CardSet.Base
    defaultSettings.botBehaviour shouldBe None
    defaultSettings.playerOneName shouldEqual "Player 1"
    defaultSettings.playerTwoName shouldBe None
  }
