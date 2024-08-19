package it.unibo.view

import it.unibo.controller.{ControllerModule, ResolveWindPhase, ViewMessage, ViewSubject}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.{Direction, GameBoard}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.view.Component.updateOnUIThread
import it.unibo.view.components.{IHaveView, IUpdateView}
import it.unibo.view.components.game.gameboard.sidebar.{GameInfoComponent, WindRoseComponent}
import monix.reactive.subjects.PublishSubject

object ViewModule:

  trait View extends IUpdateView:
    def show(): Unit
    def startGame(): Unit
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
      private val observableSubject = PublishSubject[ViewMessage]()
      private val gui = MonadicGuiFX(1280, 1024, observableSubject)

      override def show(): Unit = gui.main(Array.empty)

      override def startGame(): Unit =
        gui.loadGame()
        observableSubject.onNext(ResolveWindPhase())

      override def refresh(gameBoard: GameBoard): Unit = GameBoardController.refresh(gameBoard)

      override def getObservable: ViewSubject = observableSubject

      override def setAvailablePatterns(patterns: List[Map[Position, Token]]): Unit =
        runOnUIThread(GameBoardController.gameComponent.get.gridComponent.availablePatterns =
          patterns
        )

      override def setTurnPhase(currentTurnPhase: String): Unit =
        updateOnUIThread[GameInfoComponent](_.updateTurnPhase(currentTurnPhase))

      override def setTurnPlayer(currentPlayer: String): Unit =
        updateOnUIThread[GameInfoComponent](_.updateTurnPlayer(currentPlayer))

      override def setTurnNumber(currentTurnNumber: Int): Unit =
        updateOnUIThread[GameInfoComponent](_.updateTurnNumber(currentTurnNumber))

      override def setWindDirection(windDirection: Direction): Unit =
        updateOnUIThread[WindRoseComponent](_.updateWindRoseDirection(windDirection))

  trait Interface extends Provider with Component:
    self: Requirements =>

object Component:
  private def handleComponent[T <: IHaveView](
      handler: T => Unit
  )(using components: List[IHaveView], ct: reflect.ClassTag[T]): Unit =
    components.collectFirst { case component: T => component } match
      case Some(component) => handler(component)
      case None            => logger.warn(s"${ct.runtimeClass.getSimpleName} not found")

  given components: List[IHaveView] =
    GameBoardController.gameComponent.get.sidebarComponent.components

  def updateOnUIThread[T <: IHaveView](update: T => Unit)(using reflect.ClassTag[T]): Unit =
    runOnUIThread(handleComponent(update))
