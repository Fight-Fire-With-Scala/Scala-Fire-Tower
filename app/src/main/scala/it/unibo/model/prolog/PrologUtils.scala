package it.unibo.model.prolog

import alice.tuprolog.{SolveInfo, Struct, Term, Var}
import it.unibo.model.gameboard.grid.ConcreteToken.Empty
import it.unibo.model.gameboard.grid.{Cell, ConcreteToken, Grid, Position, Token}

import scala.collection.mutable.ListBuffer

object PrologUtils:
  given Conversion[Rule, Term] = _.term
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[List[?], Term] = _.mkString("[", ",", "]")
  given Conversion[Int, Term] = (int: Int) => Term.createTerm(int.toString)
  given Conversion[Cell, Term] = (cell: Cell) => Term.createTerm(cell.id)
  given Conversion[Token, Term] = (token: Token) => Term.createTerm(token.id)

  given Ordering[Struct] with
    override def compare(s1: Struct, s2: Struct): Int =
      val (x1, y1) = extractPair(s1)
      val (x2, y2) = extractPair(s2)
      Ordering[(Int, Int)].compare((x1, y1), (x2, y2))

  private def extractPair(struct: Struct): (Int, Int) =
    val tuple = struct.getArg(0).asInstanceOf[Struct]
    val x = tuple.getArg(0).toString.toInt
    val y = tuple.getArg(1).toString.toInt
    (x, y)

  extension (g: Grid) def size: Int = math.sqrt(g.cells.size).toInt
  
  // TODO use an accumulator with a List instead of a ListBuffer
  def parseComputedPatterns(solution: SolveInfo): Map[Position, Token] =
    val resultBuffer = ListBuffer[String]()
    val solutionAsStruct = solution.getSolution.asInstanceOf[Struct]
    val result = solutionAsStruct.getArg(0).asInstanceOf[Var].getLink.asInstanceOf[Struct]
    parseStruct(result, resultBuffer)
    convertToMap(resultBuffer.toList)

  private def parseStruct(s: Struct, buffer: ListBuffer[String]): Unit = s match
    case s if s.isTuple =>
      parseTuple(s.getArg(0), buffer)
      parseTuple(s.getArg(1), buffer)
    case s if s.isCons  =>
      parseStruct(s.getArg(0).asInstanceOf[Struct], buffer)
      parseStruct(s.getArg(1).asInstanceOf[Struct], buffer)
    case _              =>

  private def parseTuple(s: Term, buffer: ListBuffer[String]): Unit = s match
    case struct: Struct if s.isTuple => parseStruct(struct, buffer)
    case atom                        => buffer += atom.toString

  private def convertToMap(buffer: List[String]): Map[Position, Token] =
    val grouped = buffer.grouped(3).toList
    grouped.collect { case List(i1: String, i2: String, s: String) =>
      (Position(i1.toInt, i2.toInt), ConcreteToken.values.find(_.id == s).getOrElse(Empty))
    }.toMap
