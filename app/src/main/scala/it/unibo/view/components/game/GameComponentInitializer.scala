package it.unibo.view.components.game

import it.unibo.controller.ViewSubject
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.{CardComponent, HandComponent}
import it.unibo.view.components.game.gameboard.sidebar.{DeckComponent, GameInfoComponent, SidebarComponent, WindRoseComponent}

trait GameComponentInitializer:
  private def loadHand: HandComponent =
    val cardComponents = List.fill(5)(new CardComponent())
    val handComponent = HandComponent(cardComponents)
    handComponent

  private def loadSidebar(using viewObservable: ViewSubject): SidebarComponent =
    val subComponents = List(WindRoseComponent(), DeckComponent(), GameInfoComponent())
    SidebarComponent(subComponents)

  private def loadGrid(using viewObservable: ViewSubject): GridComponent =
    new GridComponent(viewObservable)

  protected def loadGame(gc: GameComponent)(using viewObservable: ViewSubject): GameComponent =
    gc.setupGrid(loadGrid)
    gc.setupSidebar(loadSidebar)
    gc.setupHand(loadHand)
    gc

  def initialize(gameComponent: GameComponent)(using viewObservable: ViewSubject): GameComponent
