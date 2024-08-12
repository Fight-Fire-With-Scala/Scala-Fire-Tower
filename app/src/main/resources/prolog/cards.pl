explosion(RR) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, [k]),
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
