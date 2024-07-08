package it.unibo.view

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

import it.unibo.controller.ControllerModule
import it.unibo.view.charts.ChartManager
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.jfree.chart.ChartPanel

import GivenConversion.GuiConversion.given

class MonadicGui(val width: Int, val height: Int, controller: ControllerModule.Controller):

  private val frame = createFrame()
  private val btn = createButton()
  private val canvas = createCanvas()
  private val chartManager = ChartManager()
  private val chartPanel = createChart()

  private val p = for
    fr <- frame
    jb <- btn
    cnv <- canvas
    ch <- chartPanel
    _ <- fr.setLayout(new BorderLayout())
    _ <- fr.add(cnv, BorderLayout.NORTH)
    _ <- fr.add(jb, BorderLayout.SOUTH)
    _ <- fr.add(ch, BorderLayout.EAST)
    _ <- fr.setVisible(true)
  yield ()
  p.runAsyncAndForget

  def render(i: Int): Unit = SwingUtilities.invokeLater { () =>
    chartManager.addValue(i, i * i)
    chartPanel.foreach(c => c.repaint())
    canvas.foreach { c =>
      c.element = i
      c.invalidate()
      c.repaint()
    }
  }

  private def createFrame(): Task[JFrame] =
    for
      fr <- new JFrame("Chrono")
      _ <- fr.setSize(width, height)
      _ <- fr.setLocationRelativeTo(null)
      _ <- fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    yield fr

  private def createButton(): Task[JButton] =
    for
      jb <- new JButton()
      _ <- jb.setText("Start")
      _ <- jb.addActionListener(e => controller.notifyStart())
    yield jb

  private def createCanvas(): Task[Environment] =
    for
      cnv <- new Environment(width - 600, height - 400)
      _ <- cnv.setSize(width - 600, height - 400)
      _ <- cnv.setVisible(true)
    yield cnv

  private def createChart(): Task[ChartPanel] =
    for
      ch <- chartManager.emptyLineChart("Grafico di prova", "Virtual time", "Virtual time ^ 2", "Squared vt")
      _ <- ch.setPreferredSize(Dimension(300, 200))
    yield ch

class Environment(val w: Int, val h: Int) extends JPanel:
  var element = 0
  override def getPreferredSize = new Dimension(w, h)
  override def paintComponent(graphics: Graphics): Unit =
    graphics.setColor(Color.BLACK)
    graphics.drawString(element.toString, 50, 50)