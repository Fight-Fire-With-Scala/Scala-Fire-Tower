package it.unibo.model.cards

import io.circe.{Decoder, HCursor}
import it.unibo.model.effects.cards.{FireEffect, FirebreakEffect, WaterEffect, WindEffect}
import it.unibo.model.effects.core.{ICardEffect, IStandardCardEffect}

final case class CardType(
    title: String,
    description: String,
    amount: Int,
    effect: Option[ICardEffect]
)

object CardType:
  def apply(title: String, description: String, amount: Int) =
    new CardType(title, description, amount, None)

  private val allCards: List[IStandardCardEffect] =
    (FireEffect.values ++ FirebreakEffect.values ++ WaterEffect.values ++ WindEffect.values).toList

  implicit val decodeBaseCard: Decoder[CardType] = (c: HCursor) =>
    for
      title <- c.downField("title").as[String]
      description <- c.downField("description").as[String]
      effectCode <- c.downField("effectCode").as[Int]
      amount <- c.downField("amount").as[Int]
    yield
      val effect = allCards.findLast(c => c.effectId.equals(effectCode))
      effect match
        case Some(ef) => CardType(title, description, amount, Some(ef))
        case None     => CardType(title, description, amount)
