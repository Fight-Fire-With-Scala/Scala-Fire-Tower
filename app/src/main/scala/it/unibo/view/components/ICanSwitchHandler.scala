package it.unibo.view.components

import javafx.event.{EventHandler, EventType}
import javafx.scene.input.MouseEvent

trait Switchable[T]:
  def switch(toState: T): Unit
  def getCurrentState: T

trait ICanSwitchHandler[T] extends Switchable[T]:

  protected var currentState: T
  protected var stateHandlers: Map[T, Map[EventType[MouseEvent], List[EventHandler[MouseEvent]]]] =
    Map()

  protected def applyState(state: T): Unit

  private def updateHandlers(
      state: T,
      action: (EventType[MouseEvent], EventHandler[MouseEvent]) => Unit
  ): Unit = stateHandlers.get(state).foreach { eventHandlers =>
    eventHandlers.foreach { case (eventType, handlers) =>
      handlers.foreach(handler => action(eventType, handler))
    }
  }

  protected def disableActualHandlers(): Unit =
    updateHandlers(currentState, getPane.removeEventHandler)

  protected def enableActualHandlers(): Unit = updateHandlers(currentState, getPane.addEventHandler)

  protected def onSwitch(state: T): Unit =
    updateHandlers(currentState, getPane.removeEventHandler)
    updateHandlers(state, getPane.addEventHandler)

  def addHandler(
      state: T,
      eventType: EventType[MouseEvent],
      handler: EventHandler[MouseEvent]
  ): Unit =
    val eventHandlers = stateHandlers.getOrElse(state, Map())
    val handlers = eventHandlers.getOrElse(eventType, List())
    stateHandlers += (state -> (eventHandlers + (eventType -> (handler :: handlers))))


  def removeHandler(
      state: T,
      eventType: EventType[MouseEvent],
      handler: EventHandler[MouseEvent]
  ): Unit =
    val eventHandlers = stateHandlers.getOrElse(state, Map())
    val handlers = eventHandlers.getOrElse(eventType, List()).filterNot(_ == handler)
    stateHandlers += (state -> (eventHandlers + (eventType -> handlers)))

  def getHandlers(state: T, eventType: EventType[MouseEvent]): List[EventHandler[MouseEvent]] =
    stateHandlers.getOrElse(state, Map()).getOrElse(eventType, List())

  override def switch(toState: T): Unit =
    stateHandlers.find(_._1 == toState).foreach { case (state, _) =>
      applyState(state)
      onSwitch(state)
      currentState = state
    }

  override def getCurrentState: T = currentState

  protected def getPane: javafx.scene.Node
