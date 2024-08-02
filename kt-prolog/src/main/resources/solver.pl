at_least_a_cell(List, N) :-
  member((X, Y, _), List),
  cell((X, Y), N), !.

at_least_a_neighbor(List, N) :-
    member((X, Y, _), List),
    neighbor_is((X, Y), N).

neighbor_is((X, Y), N) :-
    neighbor((X, Y), (NX, NY)),
    in_bounds((NX, NY)),
    cell((NX, NY), N), !.

neighbor((X1, Y1), (X2, Y2)) :-
    deltas(Delta),
    member((DX, DY), Delta),
    X2 is DX + X1,
    Y2 is DY + Y1.

appends([], []).

appends([L|Ls], Result) :-
    appends(L, Rest, Result),
    appends(Ls, Rest).

appends([], L, L).
appends([H|T1], L2, [H|T3]) :-
    appends(T1, L2, T3).

forall(Generator, Test) :-
    \+ (Generator, \+ Test).

invalid_pattern(List) :-
    forall(member((X, Y, _), List),
           (cell((X, Y), f); not(in_bounds((X, Y))))).

in_bounds((X, Y)) :-
    numRows(NumRows),
    numCols(NumCols),
    X >= 0, X < NumRows,
    Y >= 0, Y < NumCols.

map_to_assoc([], []).
map_to_assoc([(X, Y, PType)|T], [(X, Y)-PType|Result]) :-
    map_to_assoc(T, Result).

main(FinalResult, AType, NType, CType) :-
    findall(Result, pattern_find(Result, AType, NType, CType), Results),
    appends(Results, FinalResult).

pattern_find(Results, AType, NType, CType) :-
    cell(Coords, AType),
    findall(Result, pattern_fit(Coords, Result, NType, CType), Results).

pattern_fit(Coords, TypedResults, NType, CType) :-
    directions(Direction),
    member(Dir, Direction),
    findall((SX, SY, PType), pattern_apply(Dir, Coords, SX, SY, PType), Results),
    not(invalid_pattern(Results)),
    at_least_a_cell(Results, CType),
    at_least_a_neighbor(Results, NType),
    map_to_assoc(Results, TypedResults).

pattern_apply(Direction, (X, Y), SX, SY, PType) :-
    pattern((PX, PY), PType),
    ( Direction = north, SX is X - PY, SY is Y + PX
    ; Direction = west, SX is X - PX, SY is Y - PY
    ; Direction = east, SX is X + PX, SY is Y + PY
    ; Direction = south, SX is X + PY, SY is Y - PX
    ).