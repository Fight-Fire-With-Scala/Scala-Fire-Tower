% Main function to get the closest cell to the tower
main(R) :-
    get_all_cards_result(AllResults),
    writeln('All results: '), writeln(AllResults),
    tower_position(TowerPos),
    writeln('Tower position: '), writeln(TowerPos),
    find_closest(AllResults, TowerPos, Closest),
    R = Closest.


% Base case: Only one element in the list
find_closest([(Coords, CardId)], TowerPos, (Coords, CardId)) :- !.
find_closest([(Coords1, CardId1), (Coords2, CardId2) | Rest], TowerPos, Closest) :-
    find_min_distance(Coords1, TowerPos, MinDist1),
    find_min_distance(Coords2, TowerPos, MinDist2),
    (MinDist1 =< MinDist2 ->
        find_closest([(Coords1, CardId1) | Rest], TowerPos, Closest)
    ;
        find_closest([(Coords2, CardId2) | Rest], TowerPos, Closest)
    ).

% Helper predicate to find the minimum distance and corresponding coordinate
find_min_distance([(X,Y,T)], TowerPos, MinDist) :-

    manhattan_distance((X,Y), TowerPos, MinDist).
find_min_distance([(X1,Y1,T1), (X2,Y2,T2) | Rest], TowerPos, MinDist) :-

    manhattan_distance((X1,Y1), TowerPos, Dist1),
    manhattan_distance((X2,Y2), TowerPos, Dist2),
    (Dist1 =< Dist2 ->
        find_min_distance([(X1,Y1,T1) | Rest], TowerPos, MinDist)
    ;
        find_min_distance([(X2,Y2,T2) | Rest], TowerPos, MinDist)
    ).

