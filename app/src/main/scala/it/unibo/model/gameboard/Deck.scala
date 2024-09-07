package it.unibo.model.gameboard

import scala.io.Source
import scala.util.Random
import it.unibo.model.logger
import cats.syntax.either.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.yaml
import io.circe.yaml.parser
import it.unibo.model.cards.types.CanBePlayedAsExtra
import it.unibo.model.cards.{Card, CardType}

// Used to parse the yaml file of the cards' types
final case class CardSet(cardSets: List[CardType])

final case class Deck(
    standardCards: List[Card],
    specialCards: List[Card] = List.empty,
    playedCards: List[Card] = List.empty
):
  def shuffle(): Deck = copy(standardCards = Random.shuffle(standardCards))
  def drawCard(): (Card, Deck) =
    if standardCards.isEmpty then regenerate()
    (standardCards.head, copy(standardCards = standardCards.tail))
  def drawSpecialCard(): (Card, Deck) = (specialCards.head, copy(specialCards = specialCards.tail))
  private def regenerate(): Deck = copy(standardCards = playedCards, playedCards = List.empty)
    .shuffle()

object Deck:
  def apply(cardsResourcePath: String): Deck =
    val cardTypes = parseCardTypes(cardsResourcePath)
    cardTypes match
      case Some(cards) =>
        val (sc, c) = createCards(cards)
        new Deck(c, sc).shuffle()
      case None        => Deck(List.empty)

  private def parseCardTypes(cardsResourcePath: String): Option[CardSet] =
    val deckYaml = Source.fromResource(cardsResourcePath).mkString
    parser.parse(deckYaml).leftMap(err => err: Error).flatMap(_.as[CardSet]).toOption

  private def createCards(cardTypes: CardSet): (List[Card], List[Card]) =
    val allCards = cardTypes.cardSets.flatMap(c => List.fill(c.amount)(c)).zipWithIndex
      .map { case (c, index) => Card(index + 1, c) }

    allCards.partition(_.cardType.effectType.isInstanceOf[CanBePlayedAsExtra])

  def showDeck(deck: Deck): Unit = deck.standardCards.foreach(card => logger.info(s"$card"))
