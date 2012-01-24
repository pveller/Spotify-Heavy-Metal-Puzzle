attendees([], []).
attendees((E1, _), [E1]).
attendees((_, E2), [E2]).
attendees([H|T], Solution) :- 
	attendees(H, AttendeeFromFirstTeam), 
	attendees(T, AttendeesFromOtherTeams), 
	append(AttendeeFromFirstTeam, AttendeesFromOtherTeams, Accumulator), 
	sort(Accumulator, Solution).

optimize(Result, ListA, ListB) :- 
	length(ListA, X), 
	length(ListB, Y),
	(\=(X,Y) -> compare(Result, X, Y) ; (member(1099,ListA) -> Result = (<)	; Result = (>))).
		

solve(AllTeams, OptimalSolution) :- 
	setof(Solution, attendees(AllTeams, Solution), UniqueSolutions),
	predsort(optimize, UniqueSolutions, [OptimalSolution|_]),
	length(OptimalSolution, L),
	print(L).
	
	











