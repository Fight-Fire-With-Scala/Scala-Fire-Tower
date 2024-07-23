package it.unibo.model.players

import it.unibo.model.cards.{Card, CardType}
import it.unibo.model.cards.effects.{WindCard, WindEffect}
import it.unibo.model.cards.types.WindCardType
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends AnyFlatSpec with Matchers:

  val testCardType: CardType =
    CardType(title = "Test", description = "Test card", amount = 1, effect = WindCardType.South.effect)

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
