package it.unibo.model.cards

import scala.io.Source
import scala.util.Random

import cats.syntax.either._
import io.circe._
import io.circe.generic.auto._
import io.circe.yaml
import io.circe.yaml.parser

case class CardSet(cardSets: List[BaseCard])

case class Deck(cards: List[BaseCard], seed: Int):
  def shuffle(): Deck = copy(cards = Random.shuffle(cards))
  def drawCard(): (BaseCard, Deck) = (cards.head, copy(cards = cards.tail))

object Deck:
  def apply(cardsResourcePath: String, seed: Int = 42): Deck =
    Random.setSeed(seed)
    new Deck(parseCards(cardsResourcePath).cardSets, seed)

  private def parseCards(cardsResourcePath: String): CardSet =
    val deckYaml = Source.fromResource(cardsResourcePath).mkString
    parser.parse(deckYaml).leftMap(err => err: Error).flatMap(_.as[CardSet]).valueOr(throw _)

  def showDeck(deck: Deck): Unit = deck.cards
    .foreach(card => logger.debug(s"${card.title}: ${card.typeName}"))