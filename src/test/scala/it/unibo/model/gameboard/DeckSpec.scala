package it.unibo.model.gameboard

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class DeckSpec extends AnyWordSpecLike with Matchers:

  "Deck" should:
    "be created from a YAML file" in:
      val deck = Deck("cards.yaml")
      deck.standardCards should not be empty
      deck.specialCards should not be empty

    "shuffle the deck" in:
      val deck = Deck("cards.yaml")
      val originalOrder = deck.standardCards.map(_.id)
      val shuffledDeck = deck.shuffle()
      val shuffledOrder = shuffledDeck.standardCards.map(_.id)
      shuffledOrder should not equal originalOrder

    "draw a card from the deck" in:
      val deck = Deck("cards.yaml")
      val (card, newDeck) = deck.drawCard()
      deck.standardCards should have size (newDeck.standardCards.size + 1)
      newDeck.standardCards should not contain card

    "draw a special card from the deck" in:
      val deck = Deck("cards.yaml")
      val (specialCard, newDeck) = deck.drawSpecialCard()
      deck.specialCards should have size (newDeck.specialCards.size + 1)
      newDeck.specialCards should not contain specialCard

    "regenerate the deck from playedCards when empty" in:
      val deck = Deck("cards.yaml")
      val playedCard = deck.standardCards.head
      val emptyDeck = deck.copy(standardCards = List.empty, playedCards = List(playedCard))
      val (card, regeneratedDeck) = emptyDeck.drawCard()
      regeneratedDeck.standardCards should not be empty
      regeneratedDeck.standardCards should contain(playedCard)
      regeneratedDeck.playedCards shouldBe empty
