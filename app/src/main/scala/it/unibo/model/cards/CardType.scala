package it.unibo.model.cards

import io.circe.{Decoder, HCursor}
import it.unibo.model.effects.cards.{
  BucketEffect,
  FireEffect,
  FirebreakEffect,
  WaterEffect,
  WindEffect
}
import it.unibo.model.effects.core.{ICardEffect, IStandardCardEffect}

final case class CardType(title: String, description: String, amount: Int, effect: ICardEffect)

object CardType:
  private val allCards: List[ICardEffect] =
    (FireEffect.values ++ FirebreakEffect.values ++ WaterEffect.values ++ WindEffect.values)
      .toList ++ List(BucketEffect)

  implicit val decodeBaseCard: Decoder[CardType] = (c: HCursor) =>
    for
      title <- c.downField("title").as[String]
      description <- c.downField("description").as[String]
      effectCode <- c.downField("effectCode").as[Int]
      amount <- c.downField("amount").as[Int]
    yield
      val effect = allCards.findLast(c => c.effectId.equals(effectCode)).head
      CardType(title, description, amount, effect)
