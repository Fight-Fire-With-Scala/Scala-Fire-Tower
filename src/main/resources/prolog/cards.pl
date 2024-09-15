explosion(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _, CardId, EffectId),
    \+ is_pattern_without_effect(R, _).

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

ember_first_phase(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w], [f]),
    compute_pattern(Coords, R, _, CardId, EffectId).

ember_second_phase(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    compute_pattern(Coords, R, _, CardId, EffectId).

firebreak(Coords, R, CardId, EffectId) :-
    compute_pattern(Coords, R, _, CardId, EffectId),
    \+ at_least(neigh, R, _, [], [k], CardId, EffectId),
    \+ at_least(pattern, R, _, [], [k], CardId, EffectId),
    \+ at_least(pattern, R, _, [], [f], CardId, EffectId),
    \+ at_least(pattern, R, _, [t], [], CardId, EffectId),
    \+ at_least(pattern, R, _, [ef], [], CardId, EffectId),
    \+ is_pattern_without_effect(R, k).

dozer_line(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    firebreak(Coords, R, CardId, EffectId).

scratch_line(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k], CardId, EffectId),
    firebreak(Coords, R, CardId, EffectId).

deforest(R, CardId, EffectId) :-
    match_empty_cells(Coords, [w]),
    disallow_neighbors(Coords, [k], CardId, EffectId),
    compute_pattern(Coords, R, _, CardId, EffectId).

reforest(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w], [k]),
    compute_pattern(Coords, R, _, CardId, EffectId).

smoke_jumper(R, CardId, EffectId) :-
    match_if_both_token_and_cell(Coords, [w, t], [f]),
    compute_pattern_with_offset((-1, -1), Coords, R, _, CardId, EffectId),
    \+ is_pattern_without_effect(R, _).

water(R, CardId, EffectId) :-
    (
        match_if_both_token_and_cell(Coords, [w, ef], [f, k]);
        match_empty_cells(Coords, [w, ef])
    ),
    compute_pattern(Coords, R, _, CardId, EffectId),
    \+ at_least(pattern, R, _, [t], [], CardId, EffectId),
    at_least(pattern, R, _, [], [f], CardId, EffectId).

bucket(R, CardId, EffectId) :-
    (
        match_if_both_token_and_cell(Coords, [w, t, ef], [f, k]);
        match_empty_cells(Coords, [w, t, ef])
    ),
    compute_pattern(Coords, R, _, CardId, EffectId),
    at_least(pattern, R, _, [t], [f], CardId, EffectId).
