%Base case
concat_lists([], []).
%Recursive case
concat_lists([List | Rest], R):-
    concat_lists(Rest, RestResult),
    append(List, RestResult, R).

token((0,10),f).
