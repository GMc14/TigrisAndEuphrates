package edu.american;

public class Player 
{
	/**These are the tiles the player owns*/
	private Tile[] tiles;
	/**The players leaders*/
	private Tile[] leaders;
	
	/** Constructs a player
	 * @params The player number
	 */
	public Player(int playerNumber)
	{
		tiles = new Tile[6];
		leaders = new Tile[4];
		
		//initialize the leaders
		leaders[0] = new Tile(Tile.PRINCE);
		leaders[1] = new Tile(Tile.TRADER);
		leaders[2] = new Tile(Tile.PRIEST);
		leaders[3] = new Tile(Tile.FARMER);
	}
	
	public Tile[] getTiles() {return tiles;}
	public void setTiles(Tile[] tiles) {this.tiles = tiles;}
	
	public Tile[] getLeaders() {return leaders;}
	public void setLeaders(Tile[] leaders) {this.leaders = leaders;}
}
