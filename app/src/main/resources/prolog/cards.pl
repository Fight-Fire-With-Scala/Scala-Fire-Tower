explosion(R) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _),
    \+ is_pattern_without_effect(R, _).
    
fire(R) :-
    (
        match_if_both_token_and_cell(Coords, [w, t], [f]);
        match_empty_cells(Coords, [w, t])
    ),
    compute_pattern(Coords, R, [k]),
    (
        at_least(neigh, R, _, [ef], []) -> true;
        at_least(neigh, R, _, [], [f])
    ),
    \+ is_pattern_without_effect(R, f).

ember_first_phase(R) :-
    match_if_both_token_and_cell(Coords, [w], [f]),
    compute_pattern(Coords, R, _).

firebreak(Coords, R) :-
    compute_pattern(Coords, R, _),
    \+ at_least(neigh, R, _, [], [k]),
    \+ at_least(pattern, R, _, [], [k]),
    \+ at_least(pattern, R, _, [], [f]),
    \+ at_least(pattern, R, _, [t], []),
    \+ at_least(pattern, R, _, [ef], []),
    \+ is_pattern_without_effect(R, k).

dozer_line(R) :-
    match_empty_cells(Coords, [w]),
    firebreak(Coords, R).

scratch_line(R) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k]),
    firebreak(Coords, R).

deforest(R) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k]),
    compute_pattern(Coords, R, _).

reforest(R) :-
    match_if_both_token_and_cell(Coords, [w], [k]),
    compute_pattern(Coords, R, _).

smoke_jumper(R) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _),
    \+ is_pattern_without_effect(R, _).

water(R) :-
    (
        match_if_both_token_and_cell(Coords, [w, ef], [f, k]);
        match_empty_cells(Coords, [w, ef])
    ),
    compute_pattern(Coords, R, _),
    \+ at_least(pattern, R, _, [t], []),
    at_least(pattern, R, _, [], [f]).

bucket(R) :-
    (
        match_if_both_token_and_cell(Coords, [w, t, ef], [f, k]);
        match_empty_cells(Coords, [w, t, ef])
    ),
    compute_pattern(Coords, R, _),
    at_least(pattern, R, _, [t], [f]).