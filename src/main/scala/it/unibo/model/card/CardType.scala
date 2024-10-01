package it.unibo.model.card

import io.circe.Decoder
import io.circe.HCursor
import it.unibo.model.effect.card.BucketEffect
import it.unibo.model.effect.card.FireEffect
import it.unibo.model.effect.card.FirebreakEffect
import it.unibo.model.effect.card.WaterEffect
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.effect.core.CardEffect
import it.unibo.model.effect.core.StandardCardEffect

final case class CardType(title: String, description: String, amount: Int, effect: CardEffect)

object CardType:
  private val allCards: List[CardEffect] =
    (FireEffect.values ++ FirebreakEffect.values ++ WaterEffect.values ++ WindEffect.values).toList ++ List(
      BucketEffect
    )

  implicit val decodeBaseCard: Decoder[CardType] = (c: HCursor) =>
    for
      title       <- c.downField("title").as[String]
      description <- c.downField("description").as[String]
      effectCode  <- c.downField("effectCode").as[Int]
      amount      <- c.downField("amount").as[Int]
    yield
      val effect = allCards.findLast(c => c.effectId.equals(effectCode)).head
      CardType(title, description, amount, effect)
