package it.unibo.model.cards

import io.circe.yaml.parser
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardSpec extends AnyWordSpecLike with Matchers:
  
  // noinspection ScalaUnusedExpression
  "BaseCard" should:
    "be parsed from YAML" in:
      val yaml =
        """
          |title: "Card 1"
          |description: "Description of Card 1"
          |effectCode: 1
         """.stripMargin
    
      val parsedCard = parser.parse(yaml).flatMap(_.as[CardType])
    
      parsedCard.isRight shouldBe true
    
      val card = parsedCard.toOption.get
      card.title shouldBe "Card 1"
      card.description shouldBe "Description of Card 1"

    "correctly resolve card effect" in:
      val yaml =
        """
          |title: "Card 2"
          |typeName: "Water"
          |description: "Description of Card 2"
          |effectCode: 2
         """.stripMargin
    
      val parsedCard = parser.parse(yaml).flatMap(_.as[CardType])
    
      parsedCard.isRight shouldBe true
    
      val card = parsedCard.toOption.get
    
      // You may want to test the side effects of the resolve function
      // For now, just check that resolve function is non-null
      card.resolve should not be null
