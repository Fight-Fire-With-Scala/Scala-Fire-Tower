% Main function to get the closest cell to the tower
main(R) :-
    get_all_cards_result(AllResults),
    findall(TowerPos, tower_position(TowerPos), TowerPositions),
    find_closest_to_all_towers(AllResults, TowerPositions, R).

find_closest_to_all_towers([],_,([],_)):- !.

find_closest_to_all_towers(AllResults, [Tower], Closest):-
    find_closest(AllResults, Tower, Closest, ClosestDist, MinDistTmp).

find_closest_to_all_towers(AllResults, [Tower1, Tower2 | RestTowers], Closest):-
    find_closest(AllResults, Tower1, Closest1, ClosestDist1, MinDistTmp),
    find_closest(AllResults, Tower2, Closest2, ClosestDist2, MinDistTmp), 
    (ClosestDist1 < ClosestDist2 ->
        find_closest_to_all_towers(AllResults, [Tower1 | RestTowers], Closest), 
        Closest = Closest1
    ;
        find_closest_to_all_towers(AllResults, [Tower2 | RestTowers], Closest),
        Closest = Closest2
    ).

% Base case: Only one element in the list
find_closest([(Coords, CardId)], TowerPos, (Coords, CardId),  MinDistTmp, MinDistTmp) :- !.

find_closest([(Coords1, CardId1), (Coords2, CardId2) | Rest], TowerPos, Closest, ClosestDist, MinDistTmp) :-
    find_min_distance(Coords1, TowerPos, MinDist1),
    find_min_distance(Coords2, TowerPos, MinDist2),
    (MinDist1 < MinDist2 ->
        find_closest([(Coords1, CardId1) | Rest], TowerPos, Closest, ClosestDist, MinDist1)
        
    ;
        find_closest([(Coords2, CardId2) | Rest], TowerPos, Closest, ClosestDist, MinDist2)
        
    ).
    
% Helper predicate to find the minimum distance and corresponding coordinate
find_min_distance([(X,Y,T)], TowerPos, MinDist) :-

    manhattan_distance((X,Y), TowerPos, MinDist).
find_min_distance([(X1,Y1,T1), (X2,Y2,T2) | Rest], TowerPos, MinDist) :-

    manhattan_distance((X1,Y1), TowerPos, Dist1),
    manhattan_distance((X2,Y2), TowerPos, Dist2),
    (Dist1 < Dist2 ->
        find_min_distance([(X1,Y1,T1) | Rest], TowerPos, MinDist)
    ;
        find_min_distance([(X2,Y2,T2) | Rest], TowerPos, MinDist)
    ).

