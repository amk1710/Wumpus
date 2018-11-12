%meio mock... nao sei se deveria pensar em usar "estados" http://www-di.inf.puc-rio.br/~bfeijo//cursosjogos/gameai/Exemplo_de_formalizacao_e_Frame_Problem.pdf

%inicialization
%gera 8 pits
:- initialization(genPitLocations(8)).



%s0 = 0 é o estado inicial
%**************funções básicas para tratar proximo estado e último estado*************************

nextState(State0, State1) :- 
	State1 is State0 + 1.
	
previousState(State1, State0) :-
	State0 is State1 - 1.
	
%algumas regras fundamentais do jogo:

%jogo acabou se qualquer estado passado é gameOver ou se vida nesse é <= 0

%*********************END: funções básicas para tratar proximo estado e último estado**************

%***********************VIDA E GAME OVER**************************
gameOver(State) :-
	%se estado é menor que 0, então não gameover
	(State =< 0, !, fail).	

gameOver(State) :- 
	previousState(State, PreviousState),
	playerEnergy(State, CurrentEnergy),
	((CurrentEnergy =< 0) ; gameOver(PreviousState) ) , !.
	

%damagedOn informa quanto dano o player tomou naquele estado
%deal damage é usada para atualizar esse valor, com a função asserta, que insere uma clausula no inicio da base de conhecimento
dealDamage(State, Damage) :- 
	Damage >= 0, 
	damagedOn(State, OldDamage),
	NewDamage is Damage + OldDamage,
	%retract(damagedOn(State, OldDamage)),
	asserta(damagedOn(State,NewDamage)), !.
	
:- dynamic damagedOn/2.
damagedOn(_, 0).

%player Energy calcula a vida do player baseado no estado 0 e em quanto dano ele tomou em cada estado
playerEnergy(State, 100) :-
	State =< 0.

playerEnergy(State, Energy) :- 
	previousState(State, PreviousState),
	damagedOn(PreviousState, DamageDone),
	playerEnergy(PreviousState, PreviousEnergy), 
	Energy is PreviousEnergy - DamageDone,
	!.
	
%***********************end: VIDA E GAME OVER**************************
	
%*********************FUNCIONALIDADE DO MAPA***************************
%obs: no enunciado, o labirinto é indexado em 1, mas tomei a liberdade de indexar em 0

%mapa é 12x12
n_constant(12).

%Estado, posição X, Y, e i tipo do tile(empty, pit, out_of_bounds)
%tileType independe do estado
%tileType(x, y, type)

%define tiles que são pit

%genRandomPairsList gera uma lista de pares unicos com inteiros [L, U]
%cuidado: gera loop infinito se a qtd pedido é maior que a quantidade possível de permutações
%ex. não ha 10 pares unicos de inteiros E [0,2], visto que o num máximo é 3x3 = 9.
%cuidado: não usar com L >= U

genRandomPairsList(Qtd, L, U, List):-
	genRandomPairsList_acc(Qtd, L, U, List, []).

genRandomPairsList_acc(Qtd, L, U, List, ListAcc) :-
	Qtd =:= 0,
	List = ListAcc.

genRandomPairsList_acc(Qtd, L, U, List, ListAcc) :- 
	Qtd > 0,
	NQtd is Qtd - 1,
	%tenta gerar um novo elem que não esteja na lista
	(
	random_between(L, U, R1),
	random_between(L, U, R2),
	Elem = [R1, R2],
	%elem tem que ser unico
	\+ member(Elem, ListAcc),
	%elem não pode ser [0,0]
	Elem \= [0,0],
	genRandomPairsList_acc(NQtd, L, U, List, [Elem|ListAcc])
	);
	%senão, tenta de novo
	genRandomPairsList_acc(Qtd, L, U, List, ListAcc).

assertaPit([]) :-
	true.
	
assertaPit([Head|Tail]) :-
	[X,Y] = Head,
	asserta(tileType(X,Y,pit)),
	assertaPit(Tail).
	
genPitLocations(Qtd) :-
	n_constant(N),
	N_1 is N - 1,
	genRandomPairsList(Qtd, 0, N_1, List),
	assertaPit(List).


%se tile ainda não foi definido e está dentro da grid NxN, ele é um tile vazio
:- dynamic tileType/3.
tileType(X, Y, Type) :-
	n_constant(N),
	X >= 0 , X < N,
	Y >= 0 , Y < N,
	Type = empty.

% X ou Y < 0 ou > N, tile está out_of_bounds
tileType(X, Y, Type) :-
	n_constant(N),
	((X < 0 ; X >= N) ;	(Y < 0 ; Y >= N)) ,
	Type = out_of_bounds.
	

%*********************end: FUNCIONALIDADE DO MAPA***************************

%*********************PLAYER************************************************

%playerAt(State, X, Y)
%playerAt()

%*********************end: PLAYER************************************************

	
	