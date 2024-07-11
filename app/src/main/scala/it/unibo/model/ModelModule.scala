package it.unibo.model

object ModelModule:

  trait Model:

    def init(): Unit

    def updateVirtualTime(t: Int): Unit

    def getVirtualTime: Int

  trait Provider:

    val model: Model

  trait Component:

    class ModelImpl extends Model:

      private var virtualTime: Int = 0

      def init(): Unit = virtualTime = 0

      def updateVirtualTime(t: Int): Unit = virtualTime = t

      def getVirtualTime: Int = virtualTime

  trait Interface extends Provider with Component
