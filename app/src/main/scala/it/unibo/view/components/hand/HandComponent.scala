package it.unibo.view.components.hand

import it.unibo.view.components.GraphicComponent
import it.unibo.view.components.hand.cards.CardComponent
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import scalafx.application.Platform
import javafx.scene.Node

import scala.compiletime.uninitialized

class HandComponent extends GraphicComponent:
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

  private var cardComponents: List[Option[CardComponent]] = List.fill(5)(None)

  def setupCard(cardPane: Node, cardComponent: CardComponent, slotIndex: Int): Unit = Platform
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
      cardComponents = cardComponents.updated(slotIndex, Some(cardComponent))
    }

  def updateHand(cards: List[it.unibo.model.cards.Card]): Unit = Platform.runLater { () =>
    cards.zipWithIndex.foreach { case (card, index) =>
      cardComponents(index).foreach(_.setCard(Some(card)))
    }
  }
