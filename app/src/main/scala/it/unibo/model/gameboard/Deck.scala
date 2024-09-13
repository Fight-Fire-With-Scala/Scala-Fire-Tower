package it.unibo.model.gameboard

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Random

import cats.syntax.either._
import io.circe._
import io.circe.generic.auto._
import io.circe.yaml
import io.circe.yaml.parser
import it.unibo.model.card.Card
import it.unibo.model.card.CardSet
import it.unibo.model.card.CardType
import it.unibo.model.effect.core.ISpecialCardEffect
import it.unibo.model.logger

final case class Deck(
    standardCards: List[Card],
    specialCards: List[Card] = List.empty,
    playedCards: List[Card] = List.empty
):
  def shuffle(): Deck = copy(standardCards = Random.shuffle(standardCards))
  @tailrec
  def drawCard(): (Option[Card], Deck) = standardCards.headOption match
    case Some(card)                  => (Some(card), copy(standardCards = standardCards.tail))
    case None if playedCards.isEmpty =>
      logger.warn("[DECK] Could not find a standard card in deck")
      (None, this)
    case None                        =>
      logger.info("[DECK] Regenerating deck")
      val newDeck = regenerate()
      newDeck.drawCard()

  def drawSpecialCard(): (Option[Card], Deck) = specialCards.headOption match
    case Some(card) => (Some(card), copy(specialCards = specialCards.tail))
    case None       =>
      logger.warn("[DECK] Could not find a special card in deck")
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

    allCards.partition:
      case card: ISpecialCardEffect => true
      case _                        => false

  def showDeck(deck: Deck): Unit = deck.standardCards.foreach(card => logger.info(s"$card"))
