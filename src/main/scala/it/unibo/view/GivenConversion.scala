package it.unibo.view

import monix.eval.Task
import org.jfree.chart.ChartPanel

import javax.swing.{JButton, JFrame}

object GivenConversion:

  sealed trait CommonConversion:
    given Conversion[Unit, Task[Unit]] = Task(_)

  object GuiConversion extends CommonConversion:
    given Conversion[JFrame, Task[JFrame]] = Task(_)
    given Conversion[JButton, Task[JButton]] = Task(_)
    given Conversion[Environment, Task[Environment]] = Task(_)
    given Conversion[ChartPanel, Task[ChartPanel]] = Task(_)
