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
import it.unibo.model.gameboard.Pattern

object PrologUtils:
  given Conversion[Rule, Term]         = _.term
  given Conversion[String, Term]       = Term.createTerm(_)
  given Conversion[List[?], Term]      = _.mkString("[", ",", "]")
  given Conversion[Int, Term]          = (int: Int) => Term.createTerm(int.toString)
  given Conversion[Cell, Term]         = (cell: Cell) => Term.createTerm(cell.id)
  given Conversion[Token, Term]        = (token: Token) => Term.createTerm(token.id)
  given Conversion[SolverType, Theory] = t => t.getTheory(t.prologSourcePath)


  val defaultId = "0"

  extension (g: Grid) def size: Int = math.sqrt(g.cells.size).toInt

  given Ordering[Struct] with
    override def compare(s1: Struct, s2: Struct): Int =
      val (x1, y1) = extractPair(s1)
      val (x2, y2) = extractPair(s2)
      Ordering[(Int, Int)].compare((x1, y1), (x2, y2))

  private def extractPair(struct: Struct): (Int, Int) =
    val tuple = struct.getArg(0).asInstanceOf[Struct]
    val x     = tuple.getArg(0).toString.toInt
    val y     = tuple.getArg(1).toString.toInt
    (x, y)

  def parseComputedPatterns(solution: SolveInfo): Pattern = solution.getSolution match
    case solutionAsStruct: Struct => extractMapPositionTokenFromStruct(solutionAsStruct, 0)
    case _                        => Map.empty

  def parseClosestTowerPosition(solution: SolveInfo): Set[Position] =
    solution.getTerm("ClosestTower") match
      case struct: Struct =>
        val x = struct.getArg(0).toString.toInt
        val y = struct.getArg(1).toString.toInt
        Set(Position(x, y))
      case _ => Set.empty

  def parseAllCardsResult(solution: SolveInfo): (Option[Int], Pattern) =
    solution.getTerm("R") match
      case solutionAsStruct: Struct =>
        val positionsAndTokensMap = extractMapPositionTokenFromStruct(solutionAsStruct, 0)
        if positionsAndTokensMap.isEmpty then None -> positionsAndTokensMap
        else
          val cardId = solutionAsStruct.getArg(1).toString.toInt
          Some(cardId) -> positionsAndTokensMap
      case _ => None -> Map.empty

  private def extractMapPositionTokenFromStruct(struct: Struct, argId: Int): Pattern =
    struct.getArg(argId) match
      case resultVar: Var =>
        resultVar.getLink match
          case resultStruct: Struct =>
            val resultList = parseStruct(resultStruct, List())
            convertToMap(resultList)
          case _ => Map.empty
      case _ => Map.empty

  private def parseStruct(s: Struct, acc: List[String]): List[String] = s match
    case s if s.isTuple =>
      val acc1 = parseTuple(s.getArg(0), acc)
      parseTuple(s.getArg(1), acc1)
    case s if s.isCons =>
      s.getArg(0) match
        case struct: Struct =>
          val acc1 = parseStruct(struct, acc)
          s.getArg(1) match
            case struct: Struct => parseStruct(struct, acc1)
            case _              => acc
        case _ => acc
    case _ => acc

  private def parseTuple(s: Term, acc: List[String]): List[String] = s match
    case struct: Struct if s.isTuple => parseStruct(struct, acc)
    case atom                        => atom.toString :: acc

  private def convertToMap(buffer: List[String]): Pattern = buffer
    .grouped(3)
    .collect { case List(s: String, i2: String, i1: String) =>
      (Position(i1.toInt, i2.toInt), ConcreteToken.values.find(_.id == s).getOrElse(Empty))
    }
    .toMap
