package it.unibo.model.cards

import cats.syntax.either._
import com.typesafe.scalalogging.Logger
import io.circe.Decoder
import io.circe.HCursor
import it.unibo.model.cards.types.token.FireCards
import it.unibo.model.cards.types.token.FirebreakCards
import it.unibo.model.cards.types.token.WaterCards

val logger = Logger("cards")

trait Effect

trait BaseCard:
  def title: String
  def typeName: String
  def description: String
  def resolve: () => Unit

object BaseCard:
  implicit val decodeBaseCard: Decoder[BaseCard] = (c: HCursor) =>
    for {
      title <- c.downField("title").as[String]
      typeName <- c.downField("typeName").as[String]
      description <- c.downField("description").as[String]
      effectCode <- c.downField("effectCode").as[Int]
    } yield Card(title, typeName, description, resolve = parseResolution(effectCode))

  private def parseResolution(effectCode: Int): () => Unit =
    val cards = WaterCards.waterCards ++ FirebreakCards.firebreakCards ++ FireCards.fireCards
//    val pattern = cards.filter(c => c.effectCode == effectCode)
    println

case class Card(title: String, typeName: String, description: String, resolve: () => Unit)
  extends BaseCard