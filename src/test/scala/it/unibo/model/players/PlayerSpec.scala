package it.unibo.model.players

import it.unibo.model.card.{Card, CardType}
import it.unibo.model.card.types.{CanBePlayedAsExtra, WaterCard, WindCard}
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.gameboard.player.{Bot, Move, Person, Player}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyFlatSpec with Matchers:

  val testCardType: CardType =
    CardType(title = "Test", description = "Test card", amount = 1, effectType = WindCard.North)

  val extraCardType: CardType =
    CardType(title = "Extra", description = "Extra card", amount = 1, effectType = WaterCard.Bucket)

  def commonPlayerTests(createPlayer: () => Player, playerType: String): Unit =
    s"maintain a record of moves for $playerType" should s"contain all logged moves in order" in {
      val player = createPlayer()
      val move1 = Move("Move1")
      val move2 = Move("Move2")
      val updatedPlayer = player.logMove(move1).logMove(move2)
      updatedPlayer.moves should contain theSameElementsInOrderAs List(move1, move2)
    }

    s"know his hand for $playerType" should s"contain all drawn cards" in {
      val player = createPlayer()
      val card1 = Card(1, testCardType)
      val card2 = Card(2, testCardType)
      val updatedPlayer = player.drawCardFromDeck(card1).drawCardFromDeck(card2)
      updatedPlayer.hand should contain allOf (card1, card2)
    }

    s"not have more than 5 cards in hand for $playerType" should
      s"limit the hand size to 5 cards" in {
        val player = createPlayer()
        val updatedPlayer = (1 to 6)
          .foldLeft(player)((p, _) => p.drawCardFromDeck(Card(3, testCardType)))
        updatedPlayer.hand.size should be(5)
      }

    s"play a card for $playerType" should s"remove the card from the hand" in {
      val player = createPlayer()
      val card = Card(1, testCardType)
      val updatedPlayer = player.drawCardFromDeck(card)
      val (playerWithoutCard, _) = updatedPlayer.playCard(card.id)
      playerWithoutCard.hand should not contain card
    }

    s"add a card to the hand and play it, returning the same card for $playerType" should
      s"correctly process the card for $playerType" in {
        val player = createPlayer()
        val card = Card(4, testCardType)
        val updatedPlayer = player.drawCardFromDeck(card)
        val (playerAfterPlayingCard, playedCardOption) = updatedPlayer.playCard(card.id)

        playedCardOption should contain(card)
      }

    s"draw an extra card for $playerType" should s"place the card in the extra card slot" in {
      val player = createPlayer()
      val extraCard = Card(5, extraCardType)
      val updatedPlayer = player.drawCardFromDeck(extraCard)
      updatedPlayer.extraCard should contain(extraCard)
    }

    s"play an extra card for $playerType" should s"remove the card from the extra card slot" in {
      val player = createPlayer()
      val extraCard = Card(5, extraCardType)
      val updatedPlayer = player.drawCardFromDeck(extraCard)
      val (playerAfterPlayingExtraCard, playedCardOption) = updatedPlayer.playCard(extraCard.id)
      playerAfterPlayingExtraCard.extraCard should be(None)
      playedCardOption should contain(extraCard)
    }

    s"not discard an extra card for $playerType" should s"keep the extra card in the slot" in {
      val player = createPlayer()
      val extraCard = Card(5, extraCardType)
      val updatedPlayer = player.drawCardFromDeck(extraCard)
      val playerAfterDiscard = updatedPlayer.discardCards(List(extraCard.id))
      playerAfterDiscard.extraCard should contain(extraCard)
    }

  "A Person" should "be able to choose a name" in {
    val person = Player.apply("Alice")
    person.name should be("Alice")
  }

  it should behave like
    commonPlayerTests(() => Person("TestPerson", List.empty, List.empty), "Person")

  "A Bot" should "have a predefined name 'BOT'" in {
    val bot = Player.bot
    bot.name should be("BOT")
  }

  it should behave like commonPlayerTests(() => Bot(List.empty, List.empty), "Bot")
