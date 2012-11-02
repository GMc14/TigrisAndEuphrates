package edu.american;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends JLabel 
{	
	/**The types of tiles we can have*/
	public static final int SETTLEMENT=0;
	public static final int MARKET=1;
	public static final int TEMPLE=2;
	public static final int FARM=3;
	public static final int PRINCE=4;
	public static final int TRADER=5;
	public static final int PRIEST=6;
	public static final int FARMER=7;
	public static final int RIVER=-1;
	public static final int BOARD=-2;
	
	/**Determine if a leader token is on the board or in the players hand*/
	private boolean isOnBoard = false;

	/**The icon that will be displayed for this tile*/
	private ImageIcon image;
	
	/**Stores what type of tile this is*/
	private int type;
	
	/**Stores which element this leader is in the leader array*/
	private int leaderNumber;
	
	/**Determines if this tile is a leader or not*/
	private boolean isLeader;

	/**Constructs a tile --- used by the game*/
	public Tile(int type)
	{
		//set the type
		this.type = type;
		this.isLeader = false;
		
		//load the image corresponding to this type
		switch (type)
		{
			case (SETTLEMENT): image = new ImageIcon(ClassLoader.getSystemResource("images/settlement.gif")); this.setName("settlement"); break;
			case (MARKET): image = new ImageIcon(ClassLoader.getSystemResource("images/market.gif")); this.setName("market"); break;
			case (TEMPLE): image = new ImageIcon(ClassLoader.getSystemResource("images/temple.gif")); this.setName("temple"); break;
			case (FARM): image = new ImageIcon(ClassLoader.getSystemResource("images/farm.gif")); this.setName("farm"); break;
			case (PRINCE): image = new ImageIcon(ClassLoader.getSystemResource("images/prince.gif")); this.isLeader=true; this.setName("prince"); this.leaderNumber=0; break;
			case (TRADER): image = new ImageIcon(ClassLoader.getSystemResource("images/trader.gif")); this.isLeader=true; this.setName("trader"); this.leaderNumber=1; break;
			case (PRIEST): image = new ImageIcon(ClassLoader.getSystemResource("images/priest.gif")); this.isLeader=true; this.setName("priest"); this.leaderNumber=2; break;
			case (FARMER): image = new ImageIcon(ClassLoader.getSystemResource("images/farmer.gif")); this.isLeader=true; this.setName("farmer"); this.leaderNumber=3; break;
			case (RIVER): image = new ImageIcon(ClassLoader.getSystemResource("images/river.gif")); isOnBoard = true; this.setName("river"); break;
			case (BOARD): image = new ImageIcon(ClassLoader.getSystemResource("images/board.gif")); isOnBoard = true; this.setName("board"); break;
		}
		this.setIcon(image);
	}
	
	//getters and setters
	public boolean isOnBoard() {return isOnBoard;}
	public void setOnBoard(boolean onBoard) {this.isOnBoard = onBoard;}
	public ImageIcon getImage() {return image;}
	public int getType() {return type;}
	public void setType(int type) {this.type = type;}
	public int getLeaderNumber() {return leaderNumber;}
	public void setLeaderNumber(int leaderNumber) {this.leaderNumber = leaderNumber;}
	public boolean isLeader() {return isLeader;}
	public void setLeader(boolean isLeader) {this.isLeader = isLeader;}
}
