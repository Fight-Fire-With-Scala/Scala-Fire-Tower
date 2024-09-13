package it.unibo.model.prolog

import alice.tuprolog
import alice.tuprolog.SolveInfo
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
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
  given Conversion[SolverType, Theory] = t => t.getTheory(t.prologSourcePath)

  extension (g: Grid) def size: Int = math.sqrt(g.cells.size).toInt

  def parseComputedPatterns(solution: SolveInfo): Map[Position, Token] = solution.getSolution match
    case solutionAsStruct: Struct => extractMapPositionTokenFromStruct(solutionAsStruct, 0)
    case _                        => Map.empty

  def parseAllCardsResult(solution: SolveInfo): Map[Int, Map[Position, Token]] =
    solution.getTerm("R") match
      case solutionAsStruct: Struct =>
        val cardId = solutionAsStruct.getArg(1).toString.toInt
        val positionsAndTokensMap = extractMapPositionTokenFromStruct(solutionAsStruct, 0)
        Map(cardId -> positionsAndTokensMap)
      case _                        => Map.empty

  private def extractMapPositionTokenFromStruct(struct: Struct, argId: Int): Map[Position, Token] =
    struct.getArg(argId) match
      case resultVar: Var => resultVar.getLink match
          case resultStruct: Struct =>
            val resultList = parseStruct(resultStruct, List())
            convertToMap(resultList)
          case _                    => Map.empty
      case _              => Map.empty

  private def parseStruct(s: Struct, acc: List[String]): List[String] = s match
    case s if s.isTuple =>
      val acc1 = parseTuple(s.getArg(0), acc)
      parseTuple(s.getArg(1), acc1)
    case s if s.isCons  =>
      s.getArg(0) match
        case struct: Struct =>
          val acc1 = parseStruct(struct, acc)
          s.getArg(1) match
            case struct: Struct => parseStruct(struct, acc1)
            case _              => acc
        case _              => acc
    case _              => acc

  private def parseTuple(s: Term, acc: List[String]): List[String] = s match
    case struct: Struct if s.isTuple => parseStruct(struct, acc)
    case atom                        => atom.toString :: acc

  private def convertToMap(buffer: List[String]): Map[Position, Token] = buffer.grouped(3)
    .collect { case List(s: String, i2: String, i1: String) =>
      (Position(i1.toInt, i2.toInt), ConcreteToken.values.find(_.id == s).getOrElse(Empty))
    }.toMap
