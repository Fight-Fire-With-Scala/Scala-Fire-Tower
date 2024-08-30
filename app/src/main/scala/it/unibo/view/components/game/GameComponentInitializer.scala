package it.unibo.view.components.game

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.ISidebarComponent
import it.unibo.view.components.game.gameboard.hand.{CardComponent, HandComponent}
import it.unibo.view.components.game.gameboard.sidebar.{DeckComponent, GameInfoComponent, SidebarComponent, WindRoseComponent}
import it.unibo.view.logger
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
      List(WindRoseComponent(), DeckComponent(), GameInfoComponent())
    SidebarComponent(subComponents)

  private def loadGrid(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): GridComponent = new GridComponent(viewObservable)

  protected def loadGame(gc: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent] =
    val gridTask = gc.setupGrid(loadGrid)
    val sidebarTask = gc.setupSidebar(loadSidebar)
    val handTask = gc.setupHand(loadHand)

    Task.parZip3(gridTask, sidebarTask, handTask).map { case (b, c, d) =>
      logger.info(s"All tasks completed")
    }.flatMap(combinedResult => Task(gc))

  def initialize(gameComponent: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent]
