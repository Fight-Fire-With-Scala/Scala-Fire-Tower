package it.unibo.model.cards

import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeckSpec extends AnyWordSpecLike with Matchers:

  "Deck" should:
    "be created from a YAML file" in:
      val deck = Deck("cards.yaml")
      deck.cards should not be empty

    "shuffle the deck" in:
      val deck = Deck("cards.yaml")
      val originalOrder = deck.cards.map(_.id)
      val shuffledDeck = deck.shuffle()
      val shuffledOrder = shuffledDeck.cards.map(_.id)
      shuffledOrder should not equal originalOrder

    "draw a card from the deck" in:
      val deck = Deck("cards.yaml")
      val (card, newDeck) = deck.drawCard()
      deck.cards should have size (newDeck.cards.size + 1)
      newDeck.cards should not contain card 