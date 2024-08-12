match_if_both_token_and_cell(Coords, AllowedCells, AllowedTokens) :-
    token(Coords, TType),
    member(TType, AllowedTokens),
    cell(Coords, CType),
    member(CType, AllowedCells).

match_empty_cells(Coords, AllowedCells) :-
    cell(Coords, CType),
    member(CType, AllowedCells),
    \+ token(Coords, _).

disallow_neighbors(Coords, DisallowedNeighbors) :-
    \+ check_neighbor(token, [], Coords, DisallowedNeighbors).

forany(Cond, Action) :- \+forall(Cond, \+Action).

check_entity(cell, EType, Coords) :- cell(Coords, EType).

check_entity(token, EType, Coords) :- token(Coords, EType).

check_pattern(Entity, Coords, ReqList) :-
    forall(member(EType, ReqList), check_entity(Entity, EType, Coords)).

neighbor((X1, Y1), (X2, Y2)) :-
    deltas(Delta),
    member((DX, DY), Delta),
    X2 is DX + X1,
    Y2 is DY + Y1.

check_neighbor(Entity, PatternList, Coords, ReqList) :-
    forall(
        member(EType, ReqList),
        (
            neighbor(Coords, (NX, NY)),
            \+ member((NX, NY, _), PatternList),
            check_entity(Entity, EType, (NX, NY))
        )
    ).

all(Entity, PatternList, Reqlist) :-
    forall(member((X, Y, _), PatternList), check_pattern(Entity, (X, Y), Reqlist)).

at_least(Kind, PatternList, AllowedPattern, RequiredCells, RequiredTokens) :-
    forany(
        member((X, Y, AllowedPattern), PatternList),
        (
            Kind = pattern,
            check_pattern(cell, (X, Y), RequiredCells),
            check_pattern(token, (X, Y), RequiredTokens)
            ;
            Kind = neigh,
            check_neighbor(cell, PatternList, (X, Y), RequiredCells),
            check_neighbor(token, PatternList, (X, Y), RequiredTokens)
        )
    ).

apply_pattern(Direction, (X, Y), SX, SY, PType) :-
    pattern((PX, PY), PType),
    (
        Direction = north, SX is X - PY, SY is Y + PX
        ; Direction = west, SX is X - PX, SY is Y - PY
        ; Direction = east, SX is X + PX, SY is Y + PY
        ; Direction = south, SX is X + PY, SY is Y - PX
    ).

in_bounds((X, Y)) :-
    numRows(NumRows),
    numCols(NumCols),
    X >= 0, X < NumRows,
    Y >= 0, Y < NumCols.

compute_pattern(Coords, Results, AllowedTokens) :-
    directions(Direction),
    member(Dir, Direction),
    findall((SX, SY, PType),
            (
                apply_pattern(Dir, Coords, SX, SY, PType),
                \+ (token((SX, SY), TType), \+ member(TType, AllowedTokens)),
                in_bounds((SX, SY))
            ),
    Results).

compute_pattern_with_offset((OffsetX, OffsetY), (X, Y), AppliedPattern, AllowedTokens) :-
    AdjustedX is X + OffsetX,
    AdjustedY is Y + OffsetY,
    findall((SX, SY, PType),
        (
            apply_pattern(east, (AdjustedX, AdjustedY), SX, SY, PType),
            \+ (token((SX, SY), TType), \+ member(TType, AllowedTokens)),
            in_bounds((SX, SY))
        ),
        AppliedPattern).

is_pattern_without_effect(PatternList, Kind) :-
    forall(
        member((X, Y, _), PatternList),
        (token((X, Y), Kind); not(in_bounds((X, Y))))
    ).