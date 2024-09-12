package it.unibo.view.components.game

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.view.components.ISidebarComponent
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.CardComponent
import it.unibo.view.components.game.gameboard.hand.HandComponent
import it.unibo.view.components.game.gameboard.sidebar.DeckComponent
import it.unibo.view.components.game.gameboard.sidebar.DiceComponent
import it.unibo.view.components.game.gameboard.sidebar.GameInfoComponent
import it.unibo.view.components.game.gameboard.sidebar.SidebarComponent
import it.unibo.view.components.game.gameboard.sidebar.WindRoseComponent
import monix.eval.Task

trait GameComponentInitializer:
  private def loadHand(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): HandComponent =
    val cardComponents = List.fill(6)(new CardComponent())
    val handComponent = HandComponent(cardComponents)
    handComponent

  private def loadSidebar(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): SidebarComponent =
    val subComponents: List[ISidebarComponent] =
      List(WindRoseComponent(), DeckComponent(), GameInfoComponent(), DiceComponent())
    SidebarComponent(subComponents)

  private def loadGrid(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): GridComponent = new GridComponent()

  protected def loadGame(gc: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent] =
    val gridTask = gc.setupGrid(loadGrid)
    val sidebarTask = gc.setupSidebar(loadSidebar)
    val handTask = gc.setupHand(loadHand)

    Task.parZip3(gridTask, sidebarTask, handTask).map { case (b, c, d) => }
      .flatMap(combinedResult => Task(gc))

  def initialize(gameComponent: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent]
