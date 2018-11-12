package wumpusWorld;

import java.util.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

public class GameFrame extends JFrame{

	private  List<GameInfo> gameInfos;
	private JPanel mainPanel;
	private GamePanel gp;
	private BoardPanel bp;
	
	private GameFrame(String s){
		super(s);
		//this.setSize(600, 600);
		
		
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		Query q1 =
	            new Query(
	                "consult",
	                new Term[] {new Atom("Prolog/WumpusWorld.pl")}
	            );
		
		  System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed"));
		  q1.close();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		gameInfos = new LinkedList<GameInfo>();
		gameInfos.add(new GameInfo(0));
		
		bp = new BoardPanel(gameInfos.get(0));
		mainPanel.add(bp);
		
		//adiciona painel com infos basicas
		gp = new GamePanel(gameInfos.get(0));
		mainPanel.add(gp);
		
		this.add(mainPanel);
		this.pack();
		
		
		
	}
	
	//cada ciclo de jogo, avançando de estado em estado
	public void GameTick()
	{
		//pega novas informações, do próximo estado
		//proximo estado == ultimo estado + 1
		GameInfo nextGameInfo = new GameInfo(gameInfos.get(gameInfos.size() - 1).state + 1);
		gameInfos.add(nextGameInfo);
		
		//atualiza display
		gp.UpdateGamePanel(nextGameInfo);
		
	}

	public static void main(String[] args) {
		GameFrame gf= new GameFrame("Wumpus World");
		gf.setVisible(true);
		
		//a cada n segundos, se jogo não acabou, GameTick
		while(! gf.gameInfos.get(gf.gameInfos.size() - 1).gameOver)
		{		
			//System.out.println("gameOver:" + gf.gameInfos.get(gf.gameInfos.size() - 1).gameOver);
			//espera um segundo
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			gf.GameTick();
			
			//dá nove de dano neste estado só pra ver se funciona
			int state = gf.gameInfos.get(gf.gameInfos.size() - 1).state;
			Query q2 = new Query("dealDamage", new Term[] {new org.jpl7.Integer(state), new org.jpl7.Integer(9)});
		    if(q2.hasNext())
		    {
		    	
		    }
		    q2.close();
		}

	}

}

//classe do painel
class GamePanel extends JPanel
{
	private JLabel label0, label1, label2;
	
	GamePanel(GameInfo gameInfo)
	{
		super(new GridLayout(3,1));  //3 rows, 1 column
        
		label0 = new JLabel("current state: " + gameInfo.state, JLabel.CENTER);
		
		//Create the first label.
        label1 = new JLabel("Player Energy: " + gameInfo.playerEnergy, JLabel.CENTER);
        
        /*//Set the position of its text, relative to its icon:
        label1.setVerticalTextPosition(JLabel.BOTTOM);
        label1.setHorizontalTextPosition(JLabel.CENTER);*/

        //Create the other labels.
        label2 = new JLabel("gameOver: " + gameInfo.gameOver, JLabel.CENTER);

        //Create tool tips, for the heck of it.
        label1.setToolTipText("The energy the player has left");
        label2.setToolTipText("is the game over?");

        //Add the labels.
        add(label0);
        add(label1);
        add(label2);
	}
	
	void UpdateGamePanel(GameInfo gameInfo)
	{
		label0.setText("current state: " + gameInfo.state);
		label1.setText("Player Energy: " + gameInfo.playerEnergy);
		label2.setText("gameOver: " + gameInfo.gameOver);
	}
	
}

class BoardPanel extends JPanel
{	
	private int tileSize = 50;
	private GameInfo gi;
	
	public void paint(Graphics g)
	{
		//desenha um grid vazio
		for(int i = 0; i < gi.N; i++)
		{
			for(int j = 0; j < gi.N; j++)
			{			
				g.setColor(Color.BLACK);
				g.drawRect(tileSize * i, tileSize * j, tileSize, tileSize);
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(tileSize * i, tileSize * j, tileSize-1, tileSize-1);
				
				//dependendo do que estiver na casa, desenha essas coisas
				if(gi.tiles[i][j] == TileType.pit)
				{
					//desenha pit
					g.setColor(Color.BLACK);
					g.fillOval(tileSize * i, tileSize * j, tileSize, tileSize);
				}
				
			}
		}
		
	}
	
	BoardPanel(GameInfo gameInfo)
	{
		gi = gameInfo;
		
		//pega infos e monta tabuleiro
		this.setPreferredSize(new Dimension(gi.N * tileSize, gi.N * tileSize));
		
	}
}