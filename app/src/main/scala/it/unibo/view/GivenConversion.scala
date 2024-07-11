package it.unibo.view

import monix.eval.Task

import javax.swing.{JButton, JFrame}

object GivenConversion:

  sealed trait CommonConversion:

    given Conversion[Unit, Task[Unit]] = Task(_)

  object GuiConversion extends CommonConversion:

    given Conversion[JFrame, Task[JFrame]] = Task(_)

    given Conversion[JButton, Task[JButton]] = Task(_)
