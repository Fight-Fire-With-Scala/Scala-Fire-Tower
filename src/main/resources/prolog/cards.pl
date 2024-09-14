explosion(R, CardId) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _, CardId),
    \+ is_pattern_without_effect(R, _).

fire(R, CardId) :-
    (
        match_if_both_token_and_cell(Coords, [w, t], [f]);
        match_empty_cells(Coords, [w, t])
    ),
    compute_pattern(Coords, R, [k], CardId),
    (
        at_least(neigh, R, _, [ef], [], CardId) -> true;
        at_least(neigh, R, _, [], [f], CardId)
    ),
    \+ is_pattern_without_effect(R, f).

ember_first_phase(R, CardId) :-
    match_if_both_token_and_cell(Coords, [w], [f]),
    compute_pattern(Coords, R, _, CardId).

firebreak(Coords, R, CardId) :-
    compute_pattern(Coords, R, _, CardId),
    \+ at_least(neigh, R, _, [], [k], CardId),
    \+ at_least(pattern, R, _, [], [k], CardId),
    \+ at_least(pattern, R, _, [], [f], CardId),
    \+ at_least(pattern, R, _, [t], [], CardId),
    \+ at_least(pattern, R, _, [ef], [], CardId),
    \+ is_pattern_without_effect(R, k).

dozer_line(R, CardId) :-
    match_empty_cells(Coords, [w]),
    firebreak(Coords, R, CardId).

scratch_line(R, CardId) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k], CardId),
    firebreak(Coords, R, CardId).

deforest(R, CardId) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k], CardId),
    compute_pattern(Coords, R, _, CardId).

reforest(R, CardId) :-
    match_if_both_token_and_cell(Coords, [w], [k]),
    compute_pattern(Coords, R, _, CardId).

smoke_jumper(R, CardId) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _, CardId),
    \+ is_pattern_without_effect(R, _).

water(R, CardId) :-
    (
        match_if_both_token_and_cell(Coords, [w, ef], [f, k]);
        match_empty_cells(Coords, [w, ef])
    ),
    compute_pattern(Coords, R, _, CardId),
    \+ at_least(pattern, R, _, [t], []),
    at_least(pattern, R, _, [], [f]).

bucket(R, CardId) :-
    (
        match_if_both_token_and_cell(Coords, [w, t, ef], [f, k]);
        match_empty_cells(Coords, [w, t, ef])
    ),
    compute_pattern(Coords, R, _, CardId),
    at_least(pattern, R, _, [t], [f]).
