package it.unibo.model.cards

import io.circe.{Decoder, HCursor}
import it.unibo.model.cards.resolvers.Resolver
import it.unibo.model.logger

case class CardType(title: String, description: String, amount: Int, resolve: Resolver)

object CardType:
  implicit val decodeBaseCard: Decoder[CardType] = (c: HCursor) =>
    for
      title <- c.downField("title").as[String]
      description <- c.downField("description").as[String]
      effectCode <- c.downField("effectCode").as[Int]
      amount <- c.downField("amount").as[Int]
    yield CardType(title, description, amount, resolve = parseResolution(effectCode))

  private def parseResolution(effectCode: Int): Resolver =
    logger.info(s"effectCode: $effectCode")  
    allCards.filter(c => c.effectCode.equals(effectCode)).map(c => c.effect).head
