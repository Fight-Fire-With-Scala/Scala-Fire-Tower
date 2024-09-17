% Return all the points which are both in the allowed cells and the allowed tokens lists
match_if_both_token_and_cell(Coords, AllowedCells, AllowedTokens) :-
    token(Coords, TType),
    member(TType, AllowedTokens),
    cell(Coords, CType),
    member(CType, AllowedCells).

% Return all the points which are in the allowed cells and do not have a token
match_empty_cells(Coords, AllowedCells) :-
    cell(Coords, CType),
    member(CType, AllowedCells),
    \+ token(Coords, _).

% Return all the points that do not have a neighbor token in the disallowed neighbors list
disallow_neighbors(Coords, DisallowedNeighbors, CardId, EffectId) :-
    \+ check_neighbor(token, [], Coords, DisallowedNeighbors, CardId, EffectId).

% Return true if for the given conditions an action is true at least once
forany(Cond, Action) :- \+forall(Cond, \+Action).

% Return true if a cell with the given coords exists
check_entity(cell, EType, Coords) :- cell(Coords, EType).

% Return true if a token with the given coords exists
check_entity(token, EType, Coords) :- token(Coords, EType).

% Return true if all the points in the pattern have all cells or tokens of the type in
% the requirements list
check_pattern(Entity, Coords, ReqList) :-
    forall(member(EType, ReqList), check_entity(Entity, EType, Coords)).

% Return one neighbor of point (X1, Y1) computed according to the deltas list.
% Namely, if deltas is [(0,1)] and the input is (2,3), it will return (2,4)
neighbor((X1, Y1), (X2, Y2), CardId, EffectId) :-
    deltas(Delta, CardId, EffectId),
    member((DX, DY), Delta),
    X2 is X1 - DX,
    Y2 is Y1 - DY.

% Return true if a point has all neighbor tokens or cells of the type in
% the requirements list
check_neighbor(Entity, PatternList, Coords, ReqList, CardId, EffectId) :-
    forall(
        member(EType, ReqList),
        (
            neighbor(Coords, (NX, NY), CardId, EffectId),
            \+ member((NX, NY, _), PatternList),
            check_entity(Entity, EType, (NX, NY))
        )
    ).

% Return true if every pattern in the pattern list has a cell or token in the requirement
% list
all(Entity, PatternList, Reqlist) :-
    forall(member((X, Y, _), PatternList), check_pattern(Entity, (X, Y), Reqlist)).

% Return true if at least one pattern or neighbor of a pattern in pattern list
% has a required cell and a required token
at_least(Kind, PatternList, AllowedPattern, RequiredCells, RequiredTokens, CardId, EffectId) :-
    forany(
        member((X, Y, AllowedPattern), PatternList),
        (
            Kind = pattern,
            check_pattern(cell, (X, Y), RequiredCells),
            check_pattern(token, (X, Y), RequiredTokens)
            ;
            Kind = neigh,
            check_neighbor(cell, PatternList, (X, Y), RequiredCells, CardId, EffectId),
            check_neighbor(token, PatternList, (X, Y), RequiredTokens, CardId, EffectId)
        )
    ).

% Return the point where a pattern should be applied according to the application point and the direction given
apply_pattern(Direction, (X, Y), SX, SY, PType, CardId, EffectId) :-
    pattern((PX, PY), PType, CardId, EffectId),
    (
        Direction = north, SX is X + PY, SY is Y - PX
        ; Direction = west, SX is X - PX, SY is Y - PY
        ; Direction = east, SX is X + PX, SY is Y + PY
        ; Direction = south, SX is X - PY, SY is Y + PX
    ).

% Return true if the point is in the bounds of the grid, otherwise false
in_bounds((X, Y)) :-
    numRows(NumRows),
    numCols(NumCols),
    X >= 0, X < NumRows,
    Y >= 0, Y < NumCols.

% Given an application point and one or more patterns, return a computed pattern for each cardinal direction
% given in directions, that does not overlap a token that is not in the allowed tokens list
compute_pattern(Coords, Results, AllowedTokens, CardId, EffectId) :-
    directions(Direction, CardId, EffectId),
    member(Dir, Direction),
    findall((SX, SY, PType),
            (
                apply_pattern(Dir, Coords, SX, SY, PType, CardId, EffectId),
                \+ (token((SX, SY), TType), \+ member(TType, AllowedTokens)),
                in_bounds((SX, SY))
            ),
    Results).

% Given a point X, Y, apply the pattern of the grid by computing the sum with the given offsets and
% in each of the given cardinal directions, excluding results that are not in the bounds of the grid
% or that do overlap a token that is not in the allowed tokens list
compute_pattern_with_offset((OffsetX, OffsetY), (X, Y), AppliedPattern, AllowedTokens, CardId, EffectId) :-
    AdjustedX is X + OffsetX,
    AdjustedY is Y + OffsetY,
    findall((SX, SY, PType),
        (
            apply_pattern(east, (AdjustedX, AdjustedY), SX, SY, PType, CardId, EffectId),
            \+ (token((SX, SY), TType), \+ member(TType, AllowedTokens)),
            in_bounds((SX, SY))
        ),
        AppliedPattern).

% Return true whether a pattern in the pattern list is outside of the grid
% or it does only apply already existing tokens of the given kind to the grid
is_pattern_without_effect(PatternList, Kind) :-
    forall(
        member((X, Y, _), PatternList),
        (token((X, Y), Kind); not(in_bounds((X, Y))))
    ).