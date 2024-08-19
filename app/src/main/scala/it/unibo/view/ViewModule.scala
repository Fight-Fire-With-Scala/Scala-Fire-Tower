package it.unibo.view

import it.unibo.controller.{ControllerModule, ResolveWindPhase, ViewMessage, ViewSubject}
import it.unibo.model.gameboard.{Direction, GameBoard}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.components.game.gameboard.sidebar.WindRoseComponent
import monix.reactive.subjects.PublishSubject
import scalafx.application.Platform

object ViewModule:

  trait View:
    def show(): Unit
    def startGame(): Unit
    def getObservable: PublishSubject[ViewMessage]
    def refresh(gameBoard: GameBoard): Unit
    def setAvailablePatterns(patterns: List[Map[Position, Token]]): Unit
    def setWindDirection(windDirection: Direction): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      private val observableSubject = PublishSubject[ViewMessage]()
      private val gui = GameUIManager(1280, 1024, observableSubject)

      override def show(): Unit = gui.main(Array.empty)

      override def startGame(): Unit =
        gui.loadGame()
        observableSubject.onNext(ResolveWindPhase())

      override def refresh(gameBoard: GameBoard): Unit = GameBoardController.refresh(gameBoard)

      override def getObservable: ViewSubject = observableSubject

      override def setAvailablePatterns(patterns: List[Map[Position, Token]]): Unit = Platform
        .runLater(() =>
          GameBoardController.gameComponent.get.gridComponent.availablePatterns = patterns
        )

      override def setWindDirection(windDirection: Direction): Unit = Platform.runLater(() =>
        val components = GameBoardController.gameComponent.get.sidebarComponent.components
        val windRose = components.collectFirst { case windRose: WindRoseComponent => windRose }
        handleWindRose(windRose, windDirection)
      )

      private def handleWindRose(wr: Option[WindRoseComponent], d: Direction): Unit = wr match
        case Some(windRose) => windRose.updateWindRoseDirection(d)
        case None           => logger.warn("WindRoseComponent not found")

  trait Interface extends Provider with Component:
    self: Requirements =>
