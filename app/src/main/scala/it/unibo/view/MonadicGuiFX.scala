package it.unibo.view

import it.unibo.controller.ControllerModule
import it.unibo.view.controllers.GraphicController
import it.unibo.view.controllers.hand.HandController
import it.unibo.view.controllers.hand.cards.CardController
import it.unibo.view.controllers.menu.MenuController
import it.unibo.view.controllers.utils.StartGameNotifier
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

import scala.compiletime.uninitialized

object GUIType extends Enumeration:
  type GUIType = Value
  val Menu: GUIType.Value = Value
  val Hand: GUIType.Value = Value
  val Card: GUIType.Value = Value

  def fxmlPathAndController(
      value: GUIType.Value,
      controller: ControllerModule.Controller
  ): (String, GraphicController) = value match
    case Menu =>
      val notifier = new StartGameNotifier(controller.notifyStartGameSession())
      ("/pages/menu.fxml", new MenuController(notifier))
    case Hand => ("/pages/hand.fxml", new HandController())
    case Card => ("/pages/card.fxml", new CardController())

class MonadicGuiFX(
    val w: Int,
    val h: Int,
    controller: ControllerModule.Controller,
    viewLoader: ViewLoader
) extends JFXApp3:

  private var pane: Pane = uninitialized

  override def start(): Unit =
    pane = new Pane()
    stage = new PrimaryStage:
      scene = new Scene(pane, w, h)
      minHeight = 600
      minWidth = 900
    loadGUI(GUIType.Menu)

  def loadGUI(guiType: GUIType.Value): Unit =
    val (fxmlPath, controllerInstance) = GUIType.fxmlPathAndController(guiType, controller)
    val guiTask = Task {
      val root = viewLoader.load(fxmlPath, controllerInstance)
      Platform.runLater { () =>
        pane.children.clear()
        pane.children.add(root.asInstanceOf[Node])
        stage.show()
      }
    }
    guiTask.runAsyncAndForget
