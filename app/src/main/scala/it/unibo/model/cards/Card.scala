package it.unibo.model.cards

import cats.syntax.either.*
import com.typesafe.scalalogging.Logger
import io.circe.Decoder
import io.circe.HCursor
import it.unibo.model.cards.resolvers.Resolver
import it.unibo.model.cards.types.allCards

val logger = Logger("cards")

trait BaseCard:
  def title: String
  def description: String
  def resolve: Resolver

object BaseCard:
  implicit val decodeBaseCard: Decoder[BaseCard] = (c: HCursor) =>
    for {
      title <- c.downField("title").as[String]
      description <- c.downField("description").as[String]
      effectCode <- c.downField("effectCode").as[Int]
    } yield Card(title, description, resolve = parseResolution(effectCode))

  private def parseResolution(effectCode: Int): Resolver = allCards
    .filter(c => c.effectCode.equals(effectCode)).map(c => c.effect).head

case class Card(title: String, description: String, resolve: Resolver) extends BaseCard
