package it.unibo.view

import it.unibo.controller.subscribers.InternalViewMessageHandler
import it.unibo.controller.{
  ControllerModule,
  InternalViewMessage,
  InternalViewSubject,
  ResolveWindPhase,
  ViewMessage,
  ViewSubject
}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.{Direction, GameBoard}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.components.{IHaveView, IUpdateView}
import it.unibo.view.components.game.gameboard.sidebar.{GameInfoComponent, WindRoseComponent}
import monix.reactive.subjects.PublishSubject
import it.unibo.view.components.game.GameComponent
import javafx.event.EventHandler
import javafx.concurrent.{Task, WorkerStateEvent}

object ViewModule:

  trait View extends IUpdateView:
    def show(): Unit
    def startGame(gameBoard: GameBoard): Unit
    def getObservable: PublishSubject[ViewMessage]
    def refresh(gameBoard: GameBoard): Unit
    def setAvailablePatterns(patterns: List[Map[Position, Token]]): Unit
    def setWindDirection(windDirection: Direction): Unit
    def setTurnPhase(currentTurnPhase: String): Unit
    def setTurnPlayer(currentPlayer: String): Unit
    def setTurnNumber(currentTurnNumber: Int): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      private val gameBoardController = GameBoardController()
      private val observableSubject = PublishSubject[ViewMessage]()
      private val internalObservableSubject = PublishSubject[InternalViewMessage]()
      private val gui = GameUIManager(1280, 1024, observableSubject)

      override def show(): Unit = gui.main(Array.empty)

      override def startGame(gameBoard: GameBoard): Unit =
        val task = gui.loadGUIRoot(GUIType.Game)

        given observable: ViewSubject = observableSubject
        given internalObservable: InternalViewSubject = internalObservableSubject

        task.setOnSucceeded(
          new EventHandler[WorkerStateEvent]():
            def handle(t: WorkerStateEvent): Unit =
              val gameComponent = task.getValue
              GameComponent.initialize(gameComponent)
              gameBoardController.initialize(gameComponent)
              setWindDirection(gameBoard.board.windDirection)
              setTurnPhase(gameBoard.gamePhase.toString)
              setTurnNumber(0)
              setTurnPlayer(gameBoard.currentPlayer.name)
              refresh(gameBoard)
              internalObservableSubject.subscribe(InternalViewMessageHandler(gameBoardController))
              observableSubject.onNext(ResolveWindPhase())
        )

        task.run()

      override def refresh(gameBoard: GameBoard): Unit = gameBoardController.refresh(gameBoard)

      override def getObservable: ViewSubject = observableSubject

      override def setAvailablePatterns(patterns: List[Map[Position, Token]]): Unit =
        runOnUIThread(gameBoardController.gameComponent.get.gridComponent.setAvailablePatterns(patterns)
        )

      private def updateOnUIThreadGameInfoComponent(update: GameInfoComponent => Unit): Unit =
        runOnUIThread {
          val components = gameBoardController.gameComponent.get.sidebarComponent.components
          components.collectFirst { case component: GameInfoComponent => component } match
            case Some(component) => update(component)
            case None            => logger.warn(s"Component not found")
        }

      private def updateOnUIThreadWindRoseComponent(update: WindRoseComponent => Unit): Unit =
        runOnUIThread {
          val components = gameBoardController.gameComponent.get.sidebarComponent.components
          components.collectFirst { case component: WindRoseComponent => component } match
            case Some(component) => update(component)
            case None            => logger.warn(s"Component not found")
        }

      override def setTurnPhase(currentTurnPhase: String): Unit =
        updateOnUIThreadGameInfoComponent(_.updateTurnPhase(currentTurnPhase))

      override def setTurnPlayer(currentPlayer: String): Unit =
        updateOnUIThreadGameInfoComponent(_.updateTurnPlayer(currentPlayer))

      override def setTurnNumber(currentTurnNumber: Int): Unit =
        updateOnUIThreadGameInfoComponent(_.updateTurnNumber(currentTurnNumber))

      override def setWindDirection(windDirection: Direction): Unit =
        updateOnUIThreadWindRoseComponent(_.updateWindRoseDirection(windDirection))

  trait Interface extends Provider with Component:
    self: Requirements =>
