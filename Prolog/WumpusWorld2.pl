%2 try


%initial conditions

:- dynamic playerEnergy/1.
playerEnergy(100).

dealDamage(D) :-
	playerEnergy(X),
	Z is X,
	E is Z - D,
	retract(playerEnergy(Z)),
	assert(playerEnergy(E)).

nb_setval(life, 100).

