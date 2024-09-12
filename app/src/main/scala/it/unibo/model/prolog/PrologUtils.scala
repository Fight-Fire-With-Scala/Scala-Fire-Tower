package it.unibo.model.prolog

import alice.tuprolog.SolveInfo
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Var
import it.unibo.model.gameboard.grid.Cell
import it.unibo.model.gameboard.grid.ConcreteToken
import it.unibo.model.gameboard.grid.ConcreteToken.Empty
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token

object PrologUtils:
  given Conversion[Rule, Term] = _.term
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[List[?], Term] = _.mkString("[", ",", "]")
  given Conversion[Int, Term] = (int: Int) => Term.createTerm(int.toString)
  given Conversion[Cell, Term] = (cell: Cell) => Term.createTerm(cell.id)
  given Conversion[Token, Term] = (token: Token) => Term.createTerm(token.id)
  
  extension (g: Grid) def size: Int = math.sqrt(g.cells.size).toInt

  def parseComputedPatterns(solution: SolveInfo): Map[Position, Token] =
    val solutionAsStruct = solution.getSolution.asInstanceOf[Struct]
    val result = solutionAsStruct.getArg(0).asInstanceOf[Var].getLink.asInstanceOf[Struct]
    val resultList = parseStruct(result, List())
    convertToMap(resultList)

  private def parseStruct(s: Struct, acc: List[String]): List[String] = s match
    case s if s.isTuple =>
      val acc1 = parseTuple(s.getArg(0), acc)
      parseTuple(s.getArg(1), acc1)
    case s if s.isCons =>
      val acc1 = parseStruct(s.getArg(0).asInstanceOf[Struct], acc)
      parseStruct(s.getArg(1).asInstanceOf[Struct], acc1)
    case _ => acc

  private def parseTuple(s: Term, acc: List[String]): List[String] = s match
    case struct: Struct if s.isTuple => parseStruct(struct, acc)
    case atom => atom.toString :: acc

  private def convertToMap(buffer: List[String]): Map[Position, Token] =
    buffer.reverse.grouped(3).collect {
      case List(i1: String, i2: String, s: String) =>
        (Position(i1.toInt, i2.toInt), ConcreteToken.values.find(_.id == s).getOrElse(Empty))
    }.toMap
