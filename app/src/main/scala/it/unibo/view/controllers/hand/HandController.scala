package it.unibo.view.controllers.hand

import it.unibo.view.controllers.GraphicController
import it.unibo.view.controllers.hand.cards.CardController
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import scalafx.application.Platform
import javafx.scene.Node

import scala.compiletime.uninitialized

class HandController extends GraphicController:
  @FXML
  var handPane: Pane = uninitialized
  @FXML
  var slot0: Pane = uninitialized
  @FXML
  var slot1: Pane = uninitialized
  @FXML
  var slot2: Pane = uninitialized
  @FXML
  var slot3: Pane = uninitialized
  @FXML
  var slot4: Pane = uninitialized

  private var cardControllers: List[Option[CardController]] = List.fill(5)(None)

  def setupCard(cardPane: Node, cardController: CardController, slotIndex: Int): Unit = Platform
    .runLater { () =>
      val slot = slotIndex match
        case 0 => slot0
        case 1 => slot1
        case 2 => slot2
        case 3 => slot3
        case 4 => slot4
      slot.getChildren.clear()
      val pane = cardPane.asInstanceOf[Pane]
      pane.layoutXProperty().bind(slot.widthProperty().subtract(pane.widthProperty()).divide(2))
      pane.layoutYProperty().bind(slot.heightProperty().subtract(pane.heightProperty()).divide(2))
      pane.getStyleClass.add("default")
      slot.getChildren.add(pane)
      cardControllers = cardControllers.updated(slotIndex, Some(cardController))
    }

  def updateHand(cards: List[it.unibo.model.cards.Card]): Unit = Platform.runLater { () =>
    cards.zipWithIndex.foreach { case (card, index) =>
      cardControllers(index).foreach(_.setCard(Some(card)))
    }
  }
