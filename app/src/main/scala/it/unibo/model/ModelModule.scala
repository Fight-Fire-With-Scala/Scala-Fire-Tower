package it.unibo.model

import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.Grid

import scala.compiletime.uninitialized

trait Observer:
  def updateGrid(): Unit

object ModelModule:

  trait Model:

    def initialiseModel(): Unit

    def addViewObserver(observer: Observer): Unit

    def getBoard: Board

  trait Provider:

    val model: Model

  trait Component:

    class ModelImpl extends Model:
      private var viewObserver: Option[Observer] = None
      private var board: Board = uninitialized

      private def updateView(): Unit = viewObserver match
        case Some(observer) => observer.updateGrid()
        case None => ()

      def addViewObserver(observer: Observer): Unit = this.viewObserver = Some(observer)

      def initialiseModel(): Unit =
        board = Board.withRandomWindAndStandardGrid
        updateView()

      def getBoard: Board = board

  trait Interface extends Provider with Component
