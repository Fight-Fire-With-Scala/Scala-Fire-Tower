package it.unibo.model.gameboard

import scala.io.Source
import scala.util.Random
import it.unibo.model.logger
import cats.syntax.either.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.yaml
import io.circe.yaml.parser
import it.unibo.model.cards.{Card, CardType}

// Used to parse the yaml file of the cards' types
final case class CardSet(cardSets: List[CardType])

final case class Deck(cards: List[Card]):
  def shuffle(): Deck = copy(cards = Random.shuffle(cards))
  def drawCard(): (Card, Deck) = (cards.head, copy(cards = cards.tail))

object Deck:
  def apply(cardsResourcePath: String): Deck =
    val cardTypes = parseCardTypes(cardsResourcePath)
    logger.info(s"${cardTypes.get.cardSets}")
    cardTypes match
      case Some(cards) =>
        val c = createCards(cards)
        logger.info(s"$c")
        new Deck(c)
      case None        => Deck(List.empty)

  private def parseCardTypes(cardsResourcePath: String): Option[CardSet] =
    val deckYaml = Source.fromResource(cardsResourcePath).mkString
    parser.parse(deckYaml).leftMap(err => err: Error).flatMap(_.as[CardSet]).toOption

  private def createCards(cardTypes: CardSet): List[Card] = cardTypes.cardSets.flatMap { c =>
    List.fill(c.amount)(c)
  }.zipWithIndex.map { case (c, index) => Card(index + 1, c) }

  def showDeck(deck: Deck): Unit = deck.cards.foreach(card => logger.info(s"$card"))
