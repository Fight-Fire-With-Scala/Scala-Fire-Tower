package it.unibo.view.components.game.gameboard.hand

import it.unibo.view.components.GraphicComponent
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import scalafx.application.Platform
import javafx.scene.Node

import scala.compiletime.uninitialized
import scala.jdk.CollectionConverters.*

final class HandComponent extends GraphicComponent:
  @FXML
  var handPane: Pane = uninitialized

  private var cardComponents: List[Option[CardComponent]] = List.empty

  @FXML
  def initialize(): Unit = cardComponents = handPane.getChildren.asScala.toList.map(_ => None)

  def setupCard(cardPane: Node, cardComponent: CardComponent, slotIndex: Int): Unit = Platform
    .runLater { () =>
      val slot = handPane.getChildren.get(slotIndex).asInstanceOf[Pane]
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
