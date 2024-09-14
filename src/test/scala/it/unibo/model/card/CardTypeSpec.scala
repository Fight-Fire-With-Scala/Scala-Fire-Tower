package it.unibo.model.card

import io.circe.yaml.parser
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class CardTypeSpec extends AnyWordSpecLike with Matchers:

  // noinspection ScalaUnusedExpression
  "BaseCard" should:
    val yaml = """
        |title: "Card"
        |description: "Description of Card"
        |effectCode: 1
        |amount: 2
       """.stripMargin

    "be parsed from YAML" in:
      val parsedCardType = parser.parse(yaml).flatMap(_.as[CardType])
      parsedCardType.isRight shouldBe true

      val card = parsedCardType.toOption.get
      card.title shouldBe "Card"
      card.description shouldBe "Description of Card"

    "correctly resolve card effect" in:
      val parsedCard = parser.parse(yaml).flatMap(_.as[CardType])
      parsedCard.isRight shouldBe true

      val card = parsedCard.toOption.get
      card.effectType should not be null
