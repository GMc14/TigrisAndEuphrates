package edu.american;

import java.util.ArrayList;
import java.util.Collections;

public class Database 
{
	public Database()
	{
	}
	
	public ArrayList<Tile> initializeTiles()
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//create settlement tiles 
		for (int i=0;i<30;i++)
			tiles.add(new Tile(Tile.SETTLEMENT));
		for (int i=0;i<30;i++)
			tiles.add(new Tile(Tile.MARKET));
		for (int i=0;i<57;i++)
			tiles.add(new Tile(Tile.TEMPLE));
		//create the farm tiles
		for (int i=0;i<36;i++)
			tiles.add(new Tile(Tile.FARM));
		
		Collections.shuffle(tiles);
		return tiles;
	}
}
