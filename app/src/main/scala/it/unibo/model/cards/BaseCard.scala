package it.unibo.model.cards

import io.circe.{Decoder, HCursor}
import it.unibo.model.cards.resolvers.Resolver

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
    } yield Card(title, description, resolve = parseResolution(effectCode), id = 0)

  private def parseResolution(effectCode: Int): Resolver = allCards
    .filter(c => c.effectCode.equals(effectCode)).map(c => c.effect).head