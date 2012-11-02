/* Leader placement movement appears finished
 * Tile placement appears finished
 * Trading in tiles appears finished
 * Kingdom mapping appears to be finished
 * Removing leaders from kingdoms
 */
package edu.american;

import java.util.ArrayList;

public class Game {

	/**The board*/
	private Board board;
	
	/**The tiles the game will be played with*/
	private ArrayList<Tile> tiles;
	
	/**Holds the players currently playing the game*/
	private ArrayList<Player> players;

	/**The game Database*/
	private Database database;
	
	/**Which player is in control of the current turn*/
	public int playersTurn=0;
	
	/**This lock will block the game until the user drops 3 tiles*/
	public Lock gameLock;
	
	/**Number of actions taken this turn*/
	private int numberActionsTaken=0;
	
	/**Action the user is taking*/
	private int currentAction=0;
	
	/**Shows if the player has pressed the end turn button*/
	private boolean hasEndedTurn=false;

	public Game()
	{
		//create the game lock
		gameLock = new Lock();
		
		//create the board
		board = new Board(this);
		
		//set up the database
		database = new Database();

		//initialize the tiles list		
		tiles = new ArrayList<Tile>();
		
		//generate the game tiles and shuffle them
		tiles = database.initializeTiles();

		//initialize the players list
		players = new ArrayList<Player>();
		
		startGame();
	}
	private void startGame()
	{
		Tile[] playerTiles = new Tile[6];

		//add a player & give them their initial tiles
		players.add(new Player(0));
		for (int i=0;i<6;i++)
		{
			playerTiles[i] = tiles.get(0);
			tiles.remove(0);
		}		
		players.get(playersTurn).setTiles(playerTiles);
		
		//play the game
		while (true)
		{
			try {Thread.sleep(100);} catch(InterruptedException e){}

			//display tiles and leaders for this player
			board.showPlayerArea(players.get(playersTurn));
			
			doTurn();
			
			//restock player tiles
			System.out.println("PLAYERS TILES");
			
			for (int i=0;i<6;i++)
			{
				System.out.println(players.get(playersTurn).getTiles()[i]);
				if (players.get(playersTurn).getTiles()[i] == null)
				{
					playerTiles[i] = tiles.get(0);
					tiles.remove(0);
				}
				else playerTiles[i] = players.get(playersTurn).getTiles()[i];
			}
			players.get(playersTurn).setTiles(playerTiles);
		}
		
	}
	private void doTurn()
	{		
		//action 1
		gameLock.lockWait();
		System.out.println("ACTION 1");
		if (hasEndedTurn) {hasEndedTurn=false; board.clearActions(); numberActionsTaken=0; return;}
		numberActionsTaken++;
		
		//action 2
		gameLock.lockWait();
		System.out.println("ACTION 2");
		if (hasEndedTurn) {hasEndedTurn=false; board.clearActions(); numberActionsTaken=0; return;}
		numberActionsTaken++;
		
		//wait for user to end turn & reset all turn counters
		board.setTradeEnabled(false);
		gameLock.lockWait();
		board.setTradeEnabled(true); 
		hasEndedTurn=false;
		numberActionsTaken=0;
		board.clearActions();
	}

	public void tradeTiles()
	{
		Tile[] playerTiles;
		playerTiles = (players.get(playersTurn)).getTiles();

		//get new tiles
		for (int i=0;i<playerTiles.length;i++)
		{
			if (playerTiles[i] == null || !playerTiles[i].isEnabled())
			{
				playerTiles[i] = tiles.get(0);
				tiles.remove(0);
			}
		}
		players.get(playersTurn).setTiles(playerTiles);
		board.showPlayerArea((players.get(playersTurn)));
		
//		for (Tile tile : players.get(playersTurn).getTiles())
//			System.out.println(tile.isEnabled());
		
	}
	//getters and setters
	public ArrayList<Player> getPlayers() {return players;}
	public void setPlayers(ArrayList<Player> players) {this.players = players;}
	public int getCurrentAction() {return currentAction;}
	public void setCurrentAction(int currentAction) {this.currentAction = currentAction;}
	public int getNumberActionsTaken() {return numberActionsTaken;}
	public void setHasEndedTurn(boolean hasEndedTurn) {this.hasEndedTurn = hasEndedTurn;}
	
	public static void main(String[] args) 
	{
		new Game();
	}
}
