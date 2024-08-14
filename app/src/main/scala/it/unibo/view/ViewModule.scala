package it.unibo.view

import it.unibo.controller.{ControllerModule, ResolveWindPhase, ViewMessage, ViewSubject}
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import monix.reactive.subjects.PublishSubject
import scalafx.application.Platform

object ViewModule:

  trait View:
    def show(): Unit
    def startGame(): Unit
    def getObservable: PublishSubject[ViewMessage]
    def refresh(gameBoard: GameBoard): Unit
    def setAvailablePatterns(patterns: List[Map[Position, Token]]): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      private val observableSubject = PublishSubject[ViewMessage]()
      private val gui = MonadicGuiFX(1600, 900, new FXMLViewLoader, observableSubject)

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

  trait Interface extends Provider with Component:
    self: Requirements =>
