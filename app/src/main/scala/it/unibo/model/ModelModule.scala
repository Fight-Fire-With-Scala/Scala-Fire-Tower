package it.unibo.model

import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.Grid
import monix.reactive.subjects.PublishSubject

import scala.compiletime.uninitialized

object ModelModule:

  trait Model:

    def initialiseModel(): Unit
    def getObservable : PublishSubject[Grid]

  trait Provider:

    val model: Model

  trait Component:

    class ModelImpl extends Model:
      private val observerSubject = PublishSubject[Grid]()
      private var board : Board = uninitialized
      
      def getObservable: PublishSubject[Grid] = observerSubject

      def initialiseModel(): Unit =
        board = Board.withRandomWindAndStandardGrid
        observerSubject.onNext(board.grid)
  

  trait Interface extends Provider with Component
