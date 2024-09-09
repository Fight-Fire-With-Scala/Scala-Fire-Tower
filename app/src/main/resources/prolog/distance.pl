manhattan_distance((X1, Y1), (X2, Y2), Distance) :-
    Distance is abs(X1 - X2) + abs(Y1 - Y2).

min_distance_to_fire(Towers, ClosestTower, MinDist) :-
    findall((Dist, Tower), (member(Tower, Towers), tower_position(Tower, TowerPos), token(FirePos, f), manhattan_distance(TowerPos, FirePos, Dist)), Dists),
    min_member((MinDist, ClosestTower), Dists).

closest_tower_to_fire(ClosestTower) :-
    min_distance_to_fire([my_tower1, my_tower2], ClosestMyTower, MyTowersDist),
    min_distance_to_fire([opponent_tower1, opponent_tower2], ClosestOpponentTower, OpponentTowersDist),
    (MyTowersDist < OpponentTowersDist -> ClosestTower = ClosestMyTower ; ClosestTower = ClosestOpponentTower).