package it.unibo.model.gameboard

import scala.io.Source
import scala.util.Random
import cats.syntax.either.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.yaml
import io.circe.yaml.parser
import it.unibo.model.cards.{Card, CardSet, CardType}
import it.unibo.model.effects.core.ISpecialCardEffect
import it.unibo.model.logger

import scala.annotation.tailrec

final case class Deck(
    standardCards: List[Card],
    specialCards: List[Card] = List.empty,
    playedCards: List[Card] = List.empty
):
  def shuffle(): Deck = copy(standardCards = Random.shuffle(standardCards))
  @tailrec
  def drawCard(): (Option[Card], Deck) =
    logger.warn("Drawing a card")
    standardCards.headOption match
      case Some(card)                  => (Some(card), copy(standardCards = standardCards.tail))
      case None if playedCards.isEmpty =>
        logger.warn("No standard cards found in deck")
        (None, this)
      case None                        =>
        logger.warn("Looping here")
        regenerate()
        drawCard()
  def drawSpecialCard(): (Option[Card], Deck) = specialCards.headOption match
    case Some(card) => (Some(card), copy(specialCards = specialCards.tail))
    case None       =>
      logger.warn("No special cards found in deck")
      (None, this)

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
    parser.parse(deckYaml).flatMap(_.as[CardSet]).toOption

  private def createCards(cardTypes: CardSet): (List[Card], List[Card]) =
    val allCards = cardTypes.cardSets.flatMap(c => List.fill(c.amount)(c)).zipWithIndex
      .map { case (c, index) => Card(index + 1, c.title, c.description, c.effect) }

    allCards.partition(_.effect.isInstanceOf[ISpecialCardEffect])

  def showDeck(deck: Deck): Unit = deck.standardCards.foreach(card => logger.info(s"$card"))
