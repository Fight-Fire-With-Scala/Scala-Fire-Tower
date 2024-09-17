% Return all the available application points of the given pattern that are computed
% from the set of points that are fire tokens on woods or tower cells, checking whether
% the central point of the 3x3 pattern, computed with the offset (-1, -1),
% returns a pattern that applies new fire tokens to the grid.
explosion(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _, CardId, EffectId),
    \+ is_pattern_without_effect(R, _).

% Return all the available application points of the given pattern that are computed
% from the set of points that are fire tokens on woods or tower cells, checking whether
% the resulting pattern satisfies a set of constraints, namely that at least a point of
% the pattern has a neighbor of type fire token or eternal fire cell and that
% the pattern applies new fire tokens to the grid.
fire(R, CardId, EffectId) :-
    (
        match_if_both_token_and_cell(Coords, [w, t], [f]);
        match_empty_cells(Coords, [w, t])
    ),
    compute_pattern(Coords, R, [k], CardId, EffectId),
    (
        at_least(neigh, R, _, [ef], [], CardId, EffectId) -> true;
        at_least(neigh, R, _, [], [f], CardId, EffectId)
    ),
    \+ is_pattern_without_effect(R, f).

% Return all the available application points of the given pattern that are computed
% from the set of points that are fire tokens on cells of type woods.
ember_first_phase(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w], [f]),
    compute_pattern(Coords, R, _, CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are empty cells of type woods.
ember_second_phase(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    compute_pattern(Coords, R, _, CardId, EffectId).

% Return all the available application points of the given pattern for which holds a set
% of constraints, namely that there aren't any other firebreak tokens in the neighbourhood
% of the pattern, that there are not already any tokens in the points where the 
% pattern will be applied, that the pattern never overlaps with a cell of type tower or
% eternal fire and that the pattern applies new firebreak tokens to the grid.
firebreak(Coords, R, CardId, EffectId) :-
    compute_pattern(Coords, R, _, CardId, EffectId),
    \+ at_least(neigh, R, _, [], [k], CardId, EffectId),
    \+ at_least(pattern, R, _, [], [k], CardId, EffectId),
    \+ at_least(pattern, R, _, [], [f], CardId, EffectId),
    \+ at_least(pattern, R, _, [t], [], CardId, EffectId),
    \+ at_least(pattern, R, _, [ef], [], CardId, EffectId),
    \+ is_pattern_without_effect(R, k).

% Return all the available application points of the given pattern that are computed
% from the set of points that are empty cells of type woods that satisfy the 
% constraints given in the "firebreak" rule.
dozer_line(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    firebreak(Coords, R, CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are empty cells of type woods and that do not have a
% neighbor token of type firebreak that satisfy the constraints given in the 
% "firebreak" rule.
scratch_line(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k], CardId, EffectId),
    firebreak(Coords, R, CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are empty cells of type woods and that do not have a
% neighbor token of type firebreak.
deforest(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k], CardId, EffectId),
    compute_pattern(Coords, R, _, CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are firebreak tokens on woods cells.
reforest(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w], [k]),
    compute_pattern(Coords, R, _, CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are fire tokens on woods or tower cells, checking
% the central point of the 3x3 pattern, computed with the offset (-1, -1).
smoke_jumper(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _, CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are firebreak or fire tokens on woods cells or that
% are empty woods or eternal fire cells, checking whether the resulting pattern satisfies 
% a set of constraints, namely that there aren't any points of the pattern that are on
% cells of type tower and that at least a point of the pattern is of type fire token.  
water(R, CardId, EffectId) :-
    (
        match_if_both_token_and_cell(Coords, [w, ef], [f, k]);
        match_empty_cells(Coords, [w, ef])
    ),
    compute_pattern(Coords, R, _, CardId, EffectId),
    \+ at_least(pattern, R, _, [t], [], CardId, EffectId),
    at_least(pattern, R, _, [], [f], CardId, EffectId).

% Return all the available application points of the given pattern that are computed
% from the set of points that are are firebreak or fire tokens on tower or woods cells
% or that are empty woods or tower cells for which there is at least a point of the pattern
% that overlaps a fire token on a tower cell.
bucket(R, CardId, EffectId) :-
    (
        match_if_both_token_and_cell(Coords, [w, t], [f, k]);
        match_empty_cells(Coords, [w, t])
    ),
    compute_pattern(Coords, R, _, CardId, EffectId),
    at_least(pattern, R, _, [t], [f], CardId, EffectId).
