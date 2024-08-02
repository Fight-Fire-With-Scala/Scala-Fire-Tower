package it.unibo.model

import monix.reactive.subjects.PublishSubject
import it.unibo.controller.{ModelMessage, ModelSubject, StartGameBoardMessage}
import it.unibo.model.gameboard.GameBoard

object ModelModule:

  trait Model:

    def initialiseModel(): Unit
    def getObservable: ModelSubject

  trait Provider:

    val model: Model

  trait Component:

    class ModelImpl extends Model:
      private val observerSubject = PublishSubject[ModelMessage]()

      def getObservable: ModelSubject = observerSubject

      def initialiseModel(): Unit = observerSubject.onNext(StartGameBoardMessage(GameBoard()))

  trait Interface extends Provider with Component
