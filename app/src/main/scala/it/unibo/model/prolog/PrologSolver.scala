package it.unibo.model.prolog

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.cards.effects.{PatternEffect, PatternComputationEffect}
import it.unibo.model.gameboard.board.Board
import it.unibo.model.prolog.Scala2P.mkPrologEngine

object PrologSolver:
  val solverProgram: String =
    """
    |    points_of_interest(Coords, AllowedCells, AllowedTokens) :-
    |        (
    |            cell(Coords, CType), member(CType, AllowedCells);
    |            token(Coords, TType), member(TType, AllowedTokens)
    |        ).
    |
    |    pattern_fit(Coords, Results) :-
    |        directions(Direction),
    |        member(Dir, Direction),
    |        findall((SX, SY, PType),
    |                pattern_apply(Dir, Coords, SX, SY, PType),
    |                Results).
    |
    |    forany(Cond, Action) :-
    |       \+forall(Cond,\+Action).
    |
    |    all(Entity, PatternList, Reqlist) :-
    |        forall(member((X, Y, _), PatternList), check_pattern(Entity, (X, Y), Reqlist)).
    |
    |    at_least(Kind, Entity, PatternList, ReqList) :-
    |        forany(
    |            member((X, Y, _), PatternList),
    |            ( Kind = pattern, check_pattern(Entity, (X, Y), ReqList)
    |            ; Kind = neigh, check_neighbor(Entity, PatternList, (X, Y), ReqList)
    |            )
    |        ).
    |
    |    check_pattern(Entity, Coords, ReqList) :-
    |        forall(
    |            member(EType, ReqList),
    |            ( Entity = cell, cell(Coords, EType)
    |            ; Entity = token, token(Coords, EType)
    |            )
    |        ).
    |
    |    check_neighbor(Entity, PatternList, Coords, ReqList) :-
    |        forall(
    |            member(EType, ReqList),
    |            (
    |                neighbor(Coords, (NX, NY)),
    |                \+ member((NX, NY, _), PatternList),
    |                in_bounds((NX, NY)),
    |                ( Entity = cell, cell((NX, NY), EType)
    |                ; Entity = token, token((NX, NY), EType)
    |                )
    |            )
    |        ).
    |
    |    pattern_apply(Direction, (X, Y), SX, SY, PType) :-
    |        pattern((PX, PY), PType),
    |        ( Direction = north, SX is X - PY, SY is Y + PX
    |        ; Direction = west, SX is X - PX, SY is Y - PY
    |        ; Direction = east, SX is X + PX, SY is Y + PY
    |        ; Direction = south, SX is X + PY, SY is Y - PX
    |        ).
    |
    |    neighbor((X1, Y1), (X2, Y2)) :-
    |        deltas(Delta),
    |        member((DX, DY), Delta),
    |        X2 is DX + X1,
    |        Y2 is DY + Y1.
    |
    |    in_bounds((X, Y)) :-
    |        gridSize(GridSize),
    |        X >= 0, X < GridSize,
    |        Y >= 0, Y < GridSize.
    |""".stripMargin
  
  val engine: Term => Theory => LazyList[SolveInfo] = (t: Term) => mkPrologEngine(t)

  def solveWithProlog(theory: Theory, goal: Term): LazyList[SolveInfo] = engine(goal)(theory)

  def solveWithProlog(b: Board, p: PatternEffect): PatternComputationEffect =
    PatternComputationEffect(List.empty)
