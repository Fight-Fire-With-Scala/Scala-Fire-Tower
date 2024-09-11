% Main predicate: finds the tuple (MinDist, ClosestTowerPos) in the list Dists
min_member_2((MinDist, ClosestTowerPos), [Head|Tail]) :-
    % Start with the first element as the initial minimum
    min_member_helper(Tail, Head, (MinDist, ClosestTowerPos)).

% Base case: when the list is empty, the current minimum is the final result
min_member_helper([], (MinDist, ClosestTowerPos), (MinDist, ClosestTowerPos)).

% Recursive case: compare current head with the current minimum
min_member_helper([(Dist, Pos) | Tail], (CurMinDist, CurMinPos), (MinDist, MinPos)) :-
    ( Dist < CurMinDist ->
        % If Dist is smaller, update the minimum
        min_member_helper(Tail, (Dist, Pos), (MinDist, MinPos))
    ;
        % Otherwise, keep the current minimum
        min_member_helper(Tail, (CurMinDist, CurMinPos), (MinDist, MinPos))
    ).

% Find the minimum distance from a set of tower positions to any fire cell and return the closest tower position
min_distance_to_fire(TowerPositions, ClosestTowerPos, MinDist) :-
    findall((Dist, TowerPos),
        (
            member(TowerPos, TowerPositions),
            token(FirePos, f),
            manhattan_distance(TowerPos, FirePos, Dist)
        ),
    Dists),
    min_member_2((MinDist, ClosestTowerPos), Dists).

closest_tower_to_fire(ClosestTower) :-
    findall(Pos, towerPosition(Pos), MyTowerPositions),
    findall(Pos, enemyTowerPosition(Pos), EnemyTowerPositions),
    min_distance_to_fire(MyTowerPositions, ClosestMyTowerPos, MyTowersDist),
    min_distance_to_fire(EnemyTowerPositions, ClosestEnemyTowerPos, EnemyTowersDist),
    (MyTowersDist < EnemyTowersDist -> ClosestTower = ClosestMyTowerPos ; ClosestTower = ClosestEnemyTowerPos).