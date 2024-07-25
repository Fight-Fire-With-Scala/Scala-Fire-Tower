package it.unibo.model.cards

import io.circe.{Decoder, HCursor}
import it.unibo.model.cards.Card.allCards
import it.unibo.model.cards.types.HasEffectType

case class CardType(title: String, description: String, amount: Int, effect: HasEffectType)

object CardType:
  implicit val decodeBaseCard: Decoder[CardType] = (c: HCursor) =>
    for
      title <- c.downField("title").as[String]
      description <- c.downField("description").as[String]
      effectCode <- c.downField("effectCode").as[Int]
      amount <- c.downField("amount").as[Int]
    yield
      val effect = allCards.filter(c => c.effectCode.equals(effectCode)).head
      CardType(title, description, amount, effect)
