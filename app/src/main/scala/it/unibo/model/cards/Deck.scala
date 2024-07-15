package it.unibo.model.cards

import scala.io.Source
import scala.util.Random
import it.unibo.model.logger

import cats.syntax.either._
import io.circe._
import io.circe.generic.auto._
import io.circe.yaml
import io.circe.yaml.parser

case class CardSet(cardSets: List[BaseCard])

case class Deck(cards: List[BaseCard]):
  def shuffle(): Deck = copy(cards = Random.shuffle(cards))
  def drawCard(): (BaseCard, Deck) = (cards.head, copy(cards = cards.tail))

object Deck:
  def apply(cardsResourcePath: String): Deck =
    val cards = parseCards(cardsResourcePath)
    cards match
      case Some(cards) => new Deck(cards.cardSets)
      case None        => Deck(List.empty)

  private def parseCards(cardsResourcePath: String): Option[CardSet] =
    val deckYaml = Source.fromResource(cardsResourcePath).mkString
    parser.parse(deckYaml).leftMap(err => err: Error).flatMap(_.as[CardSet]).toOption

  def showDeck(deck: Deck): Unit = deck.cards.foreach(card => logger.debug(s"${card.title}"))
