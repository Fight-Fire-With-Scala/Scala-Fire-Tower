package it.unibo.model.gameboard

import io.circe.yaml.parser
import it.unibo.model.board.Board
import it.unibo.model.cards.{Card, CardType, Deck}
import org.junit.runner.RunWith
import it.unibo.model.grid.Grid
import it.unibo.model.cards.resolvers.wind.{WindChoice, WindDirection}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyFlatSpec with Matchers {

  "A Board with Random Wind and Standard Grid" should "initialize with a valid wind direction and a standard grid" in {
    val board = Board.withRandomWindAndStandardGrid
    board.grid shouldBe a [Grid]
    board.grid.cells.size shouldBe Grid.positionNumber
    Seq(WindDirection.North, WindDirection.South, WindDirection.East, WindDirection.West) should contain (board.windDirection)
  }

  "Playing a card with Wind effect" should "update the wind direction in the board" in{
    val initialWindDirection = WindDirection.North
    val newWindDirection = WindDirection.South
    val board = Board(Grid.standard, initialWindDirection)
    val gameBoard = GameBoard(board, Deck(List.empty))

    val windCardYaml =
      """
        |title: "Sud"
        |typeName: "Wind"
        |description: "Cambia la direzione del vento verso Sud. OPPURE
        |Tira il dado per scegliere una nuova direzione del vento. OPPURE
        |Metti una gemma di fuoco a Nord di un'altra gemma di fuoco."
        |effectCode: 5
        |amount: 2
        |""".stripMargin
    val parsedCard = parser.parse(windCardYaml).flatMap(_.as[CardType])
    val cardType = parsedCard.toOption.get

    val updatedGameBoard = gameBoard.resolveCardPlayed(Card(0, cardType), Some(WindChoice.UpdateWind))
    updatedGameBoard.board.windDirection shouldBe newWindDirection
  }


}