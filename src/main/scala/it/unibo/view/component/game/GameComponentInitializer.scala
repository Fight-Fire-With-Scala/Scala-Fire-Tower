package it.unibo.view.component.game

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.view.component.ViewComponent
import it.unibo.view.component.game.gameboard.grid.GridComponent
import it.unibo.view.component.game.gameboard.hand.CardComponent
import it.unibo.view.component.game.gameboard.hand.HandComponent
import it.unibo.view.component.game.gameboard.sidebar.{ BicolumnComponent, DeckComponent, DiceComponent, GameInfoComponent, SidebarComponent, WindRoseComponent }
import monix.eval.Task

trait GameComponentInitializer:
  private def loadHand(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): HandComponent =
    val cardComponents = List.fill(6)(new CardComponent())
    val handComponent  = HandComponent(cardComponents)
    handComponent

  private def loadSidebar(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): SidebarComponent =
    val bicolumnPane = BicolumnComponent(DeckComponent(), DiceComponent())
    val subComponents: List[ViewComponent] =
      List(WindRoseComponent(), GameInfoComponent(), bicolumnPane)
    SidebarComponent(subComponents)

  private def loadGrid(using viewObservable: ViewSubject): GridComponent = new GridComponent()

  protected def loadGame(gc: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent] =
    val gridTask    = gc.setupGrid(loadGrid)
    val sidebarTask = gc.setupSidebar(loadSidebar)
    val handTask    = gc.setupHand(loadHand)

    Task
      .parZip3(gridTask, sidebarTask, handTask)
      .map { case (b, c, d) => }
      .flatMap(combinedResult => Task(gc))

  def initialize(gameComponent: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent]
