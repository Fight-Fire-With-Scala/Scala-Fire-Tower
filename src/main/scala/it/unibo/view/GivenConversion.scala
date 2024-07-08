package it.unibo.view

import javax.swing.JButton
import javax.swing.JFrame

import monix.eval.Task
import org.jfree.chart.ChartPanel

object GivenConversion:

  sealed trait CommonConversion:
    given Conversion[Unit, Task[Unit]] = Task(_)

  object GuiConversion extends CommonConversion:
    given Conversion[JFrame, Task[JFrame]] = Task(_)
    given Conversion[JButton, Task[JButton]] = Task(_)
    given Conversion[Environment, Task[Environment]] = Task(_)
    given Conversion[ChartPanel, Task[ChartPanel]] = Task(_)
