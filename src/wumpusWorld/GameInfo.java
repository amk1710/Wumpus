package wumpusWorld;

import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

//classe só pra organizar as info e facilitar passar como argumentos
//representa todo o jogo no estado state
public class GameInfo {

	/*infos básicas do jogo, atualizadas a cada estado
	Se os estados forem consultados fora de ordem e/ou repetidas vezes, 
	pode gerar resultados inconsistentes devido a efeitos colaterais das consultas*/
	
	//o estado que será consultado. 
	public int state;
	public int playerEnergy;
	public boolean gameOver;
	
	//tabuleiro (vai ser 12x12, mas esse númer tá definido no prolog)
	public TileType[][] tiles;
	public int N; 
	
	//supoe que já está aberta a consulta ao arquivo WumpusWorld.pl
	public GameInfo(int state) {

		this.state = state;
		
		//playerEnergy
		Query q2 = new Query("playerEnergy", new Term[] {new org.jpl7.Integer(state), new Variable("X")});
	    if(q2.hasNext())
	    {
	    	Map binding = q2.next();
	    	Term t = (Term) binding.get("X");
	    	playerEnergy = t.intValue();
	    }
	    q2.close();
	    
	    //gameOver
	    Query q3 = new Query("gameOver", new Term[] {new org.jpl7.Integer(state)});
	    gameOver = q3.hasNext();
	    
	    q3.close();
	    
	    //N (tamanho do tabuleiro NxN
	    Query q4 = new Query("n_constant(X)");
	    if(q4.hasNext())
	    {
	    	Map binding = q4.next();
	    	Term t = (Term) binding.get("X");
	    	N = t.intValue();
	    }
	    q4.close();
	    
	    tiles = new TileType[N][N];
	    //pega infos do tabuleiro NxN
	    for(int i = 0; i < N; i++)
	    {
	    	for(int j = 0; j < N; j++)
	    	{
	    		Query q5 = new Query("tileType", new Term[] {new org.jpl7.Integer(i), new org.jpl7.Integer(j), new Variable("X")});
	    	    if(q5.hasNext())
	    	    {
	    	    	Map binding = q5.next();
	    	    	Term t = (Term) binding.get("X");
	    	    	tiles[i][j] = TileType.valueOf(t.name());
	    	    }
	    	    q5.close();
	    	}
	    }
	    
	    
	    
	    
		
	}

}
