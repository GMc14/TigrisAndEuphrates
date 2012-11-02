package edu.american;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class Board
{
	/**These hold the action the player is currently taking
	 * 
	 * */
	private final int PLACING_LEADER=0;
	private final int MOVING_LEADER=4;
	private final int PLACING_TILE=1;
	private final int TRADING=2;
	
	private final int POSSIBLE_ACTIONS=3;
	private final int ACTIONS_PER_TURN=2;
	
	/**Number of tiles that can be placed horizontally*/
	public static final int NUMBER_TILES_X = 16;
	/**Number of tiles that can be placed vertically*/
	public static final int NUMBER_TILES_Y = 11;
	/**Tile width in pixels*/
	public static int TILE_WIDTH;
	/**Tile height in pixels*/
	public static int TILE_HEIGHT;
	/**Width of the play area*/
	public static int BOARD_WIDTH;
	/**Height of the play area*/
	public static int BOARD_HEIGHT;
	/**Width of the window*/
	public static int WINDOW_WIDTH;
	/**Height of the window*/
	public static int WINDOW_HEIGHT;
	
	/**The frame*/
	private JFrame frame;
	
	/**Represent the squares on the board*/
	private Tile[][] squares;
	
	/**Will be added to the components so they can be dragged*/
	private MouseListener listener;
	
	/**Ends the current turn*/
	private JButton turn;

	/**Trades in the selected tiles*/
	private JButton trade;

	/**Display actions a user can take*/
	private JLabel actionsToTake; 
	
	/**Display actions a user can take*/
	private JLabel[][] actionsTaken; 
	
	/**The game*/
	private Game game;
	
	/**Where the user area should start*/
	private int boardOffset;
	
	/**This players list of tiles*/
	private Tile[] playerTiles;
	
	/**This players list of leaders*/
	private Tile[] leaderTiles;
	
	/**This is the current player*/
	private Player player;
	
	/**This is the tile the player is dragging*/
	private Tile tileBeingDragged;
	
	/**This holds the player information*/
	private JPanel playerPanel;
	
	/**This holds the board*/
	private JPanel boardPanel;
	
	/**This is a map of the current kingdoms on the board*/
	private int[][] kingdomMap;
	
	private int numberOfKingdoms=0;
	
	private int pickedUpRow,pickedUpCol,droppedRow,droppedCol;;
	
	/**Row and column list of where kingdoms join
	 * [row1,col1,row2,col2][row1,col1,row2,col2]
	 * */
	private int[][] kingdomJoin;
	
	/**Determines whether or not a piece joined a kingdom*/
	private boolean didJoin;
	
	/**Constructs the board*/
	public Board(final Game game)
	{
		this.game = game;

		//initialize arrays
		playerTiles = new Tile[6];
		leaderTiles = new Tile[4];
		kingdomMap = new int[NUMBER_TILES_Y][NUMBER_TILES_X];
		squares = new Tile[NUMBER_TILES_Y][NUMBER_TILES_X];
		kingdomJoin = new int[2][4];
		
		
		//set initial player tile arrays to null
		for (int i=0;i<6;i++)
			playerTiles[i] = null;
		for (int i=0;i<4;i++)
			leaderTiles[i] = null;
		
		//initialize kingdom mapping to all -1 (no kingdoms)
		for (int i=0;i<NUMBER_TILES_Y;i++)
			for (int j=0;j<NUMBER_TILES_X;j++)
				kingdomMap[i][j] = -1;
	
		//get size of the screen 
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		
		//get the screen size based on the resolution of the screen being used
		WINDOW_WIDTH = (int)scrnsize.getWidth();
		WINDOW_HEIGHT = (int)scrnsize.getHeight() - 22;
		
		//calculate the tile width in relation to the board size
		TILE_WIDTH = (int)WINDOW_WIDTH / (NUMBER_TILES_X+4);
		TILE_HEIGHT = (int)WINDOW_HEIGHT / (NUMBER_TILES_Y);
		
		//calculate the board size in relation to the tile sizes
		BOARD_WIDTH = TILE_WIDTH * NUMBER_TILES_X;
		BOARD_HEIGHT = TILE_HEIGHT * NUMBER_TILES_Y;
		
		boardOffset = WINDOW_WIDTH-3*TILE_WIDTH;

		//set up the frame
		frame = new JFrame("Tigris and Euphrates");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		
		//create the panels
		playerPanel = new JPanel();
		boardPanel = new JPanel();
		
		//set up drag and drop
	    listener = new DragMouseAdapter();

	    //put the JLabels on the board
	    squares = new Tile[NUMBER_TILES_Y][NUMBER_TILES_X];
	    
		for (int i=0;i<NUMBER_TILES_Y;i++)
		{
			for (int j=0;j<NUMBER_TILES_X;j++)
			{
				//check for temple
				if ((i==1 && j==1) || (i==2 && j==5) || (i==0 && j==10) || (i==1 && j==15) || (i==4 && j==13) || (i==6 && j==8) || (i==7 && j==1) || (i==9 && j==5) || (i==10 && j==10) || (i==8 && j==14))
				{
					squares[i][j] = new Tile(Tile.TEMPLE);
					squares[i][j].setOnBoard(true);
					
					//this is also a kingdom!
					kingdomMap[i][j] = Tile.TEMPLE;
				}
				//check for river
				else if ((i==3 && j<4) || (i==2 && j==3) || (j==4 && i<3) || (i==0 && (j>4 && j<9)) || (j==12 && (i>=0 && i<3)) || (j==13 && (i>1 && i<4)) || (j==14 && (i>2 && i<7)) || (j==15 && (i>2 && i<5)) || (i==6 && (j>11 && j<14)) || (j==12 && (i>6 && i<9)) || (i==8 && (j>5 && j<12)) || (i==7 && (j>2 && j<7)) || (i==6 && j<4)) 
				{
					squares[i][j] = new Tile(Tile.RIVER);
				}
				//otherwise its empty
				else
				{
					squares[i][j] = new Tile(Tile.BOARD);
				}
				squares[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				squares[i][j].setBounds(j*TILE_WIDTH, i*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
			    squares[i][j].addMouseListener(listener);
			    squares[i][j].setTransferHandler(new DragHandler("icon"));
			    boardPanel.add(squares[i][j]);
			}
		}
			    
		//setup the buttons
		turn = new JButton("End turn");		
		turn.setBounds((int) (boardOffset), WINDOW_HEIGHT-4*TILE_HEIGHT,70,20);
		
		//ends a turn by clearing all the player tiles
		//the actions listener removes all the players tiles and leaders from the board as well as removes all played and disabled tiles from the players tile[] array
		turn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (int i=0;i<playerTiles.length;i++)
				{
					if (playerTiles[i] != null && !playerTiles[i].isEnabled())
					{
						playerPanel.remove(playerTiles[i]);
						playerTiles[i]=null;
					}
				}
				player.setTiles(playerTiles);
				player.setLeaders(leaderTiles);
//				for (int i=0;i<6;i++) System.out.println(player.getTiles()[i]);
				game.gameLock.lockResume();
				game.setHasEndedTurn(true);

			}
		});
		playerPanel.add(turn);
		
		//setup the buttons
		trade = new JButton("Trade In");		
		trade.setBounds((int) (turn.getX() + 80), WINDOW_HEIGHT-4*TILE_HEIGHT,70,20);
		
		trade.addActionListener(new ActionListener() 
		{public void actionPerformed(ActionEvent e)
		{
			actionsTaken[TRADING][game.getNumberActionsTaken()].setText(""+(1+game.getNumberActionsTaken()));
			game.tradeTiles();
			game.gameLock.lockResume();
		}});
				
		//add the buttons to the player panel
		playerPanel.add(trade);
		
		//setup the actions taken area
		actionsTaken= new JLabel[POSSIBLE_ACTIONS][ACTIONS_PER_TURN];
		for (int i=0; i<POSSIBLE_ACTIONS; i++)
		{
			int x,y;
			for (int j=0; j<ACTIONS_PER_TURN; j++)
			{
				//get location for the tile
				x = (int) (j * 10 + (boardOffset - .5 * Board.TILE_WIDTH));
				y = i * 18 + (WINDOW_HEIGHT - 4 * TILE_HEIGHT - 37);
				
				actionsTaken[i][j] = new JLabel();
				actionsTaken[i][j].setFont(new Font("Courier New",Font.BOLD,14));
				actionsTaken[i][j].setBounds(x, y, 30, 3 * TILE_HEIGHT);
//				actionsTaken[i][j].setText("1");
				playerPanel.add(actionsTaken[i][j]);
			}
		}
				
		//setup the available actions
		actionsToTake = new JLabel();
		actionsToTake.setFont(new Font("Courier New",Font.BOLD,14));
		actionsToTake.setBounds(actionsTaken[0][0].getX()+40, WINDOW_HEIGHT - 4 * TILE_HEIGHT-20, WINDOW_WIDTH - boardOffset, 3 * TILE_HEIGHT);
		actionsToTake.setText("<html>Place/move leader<br>Place tile<br>Swap Tile(s) (Hold Shift)</html>");
		playerPanel.add(actionsToTake);

		//add everything to the frame
		frame.add(boardPanel);
		frame.add(playerPanel);

		playerPanel.setLayout(null);
		playerPanel.setBounds(BOARD_WIDTH, 0, WINDOW_WIDTH-BOARD_WIDTH, WINDOW_HEIGHT);
		playerPanel.setName("playerArea");
		playerPanel.addMouseListener(listener);
		playerPanel.setTransferHandler(new DragHandler("icon"));
			
	    //board panel
	    boardPanel.setLayout(null);
		boardPanel.setBounds(0, 0, BOARD_WIDTH, WINDOW_HEIGHT);
		boardPanel.setName("boardPanel");

		frame.setVisible(true);
		
		generateKingdomMap();
		printMap();
	}
	
	private void generateKingdomMap()
	{
		for (int i=0;i<NUMBER_TILES_Y;i++)
		{
			for (int j=0;j<NUMBER_TILES_X;j++)
			{
				if (kingdomMap[i][j] != -1)
					floodFill(i,j,numberOfKingdoms++);
			}
		}
	}
	int count = 0;
	private void floodFill(int row, int col,int kingdomNumber)
	{
		count++;
		if (count == 10) {count=0;return;}
		int oldNumber;
		oldNumber = kingdomMap[row][col];
		kingdomMap[row][col]=kingdomNumber;
		System.out.println("FLOOD: "+row+" "+col+" OLDNUMBER: "+oldNumber+" KN: "+kingdomNumber);
		
		//go up
		if(row>0 && kingdomMap[row-1][col] == oldNumber) floodFill(row-1,col,kingdomNumber);
		//go down
		if(row+1<NUMBER_TILES_Y && kingdomMap[row+1][col] == oldNumber) floodFill(row+1,col,kingdomNumber);
		//go left
		if(col>0 && kingdomMap[row][col-1] == oldNumber) floodFill(row,col-1,kingdomNumber);
		//go right
		if(col+1<NUMBER_TILES_X && kingdomMap[row][col+1] == oldNumber) floodFill(row,col+1,kingdomNumber);
	}
	
	/** Display the players tiles on the screen
	 * @param tiles: array of tiles the player owns
	 * @param leaders: array of the players leaders
	 */
	public void showPlayerArea(Player p)
	{	
		this.player = p;
		
		//clean up the players tiles
		for (int i=0;i<6;i++)
			if (playerTiles[i]!=null)
				playerPanel.remove(playerTiles[i]);
		
		//location of where the player tiles start
		int x,y;
				
		//display the users tiles
		for (int i=0;i<6;i++)
		{
			//get location for the tile
			x = (i % 2) * TILE_WIDTH + boardOffset;
			y = (int)i/2 * TILE_HEIGHT + TILE_HEIGHT;
			
			playerTiles[i] = new Tile((player.getTiles())[i].getType());
			playerTiles[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			playerTiles[i].setBounds(x,y, TILE_WIDTH, TILE_HEIGHT);
		    playerTiles[i].addMouseListener(listener);
		    playerTiles[i].setTransferHandler(new DragHandler("icon"));
		    playerPanel.add(playerTiles[i]);
		}
		
		displayLeaders(player.getLeaders());
		frame.repaint();
	}

	private void displayLeaders(Tile[] leaders)
	{
		int x,y;
		//clean up the players leader tiles
		for (int i=0;i<4;i++)
			if (leaderTiles[i] != null)
				playerPanel.remove(leaderTiles[i]);
		
		//display the users leaders
		for (int i=0;i<4;i++)
		{
//			System.out.println("LEADER["+i+"]: "+leaders[i].isOnBoard());
			if (!leaders[i].isOnBoard())
			{
				x = (i % 2) * TILE_WIDTH + boardOffset;
				y = (int)i/2 * TILE_HEIGHT + (5*TILE_HEIGHT);
				
				leaderTiles[i] = (player.getLeaders())[i];
				leaderTiles[i].setBounds(x,y,TILE_WIDTH,TILE_HEIGHT);
				leaderTiles[i].addMouseListener(listener);
			    leaderTiles[i].setTransferHandler(new DragHandler("icon"));
			    playerPanel.add(leaderTiles[i]);
			}
		}
	}
	private void printMap()
	{
		for (int i=0;i<NUMBER_TILES_Y;i++)
		{
			for (int j=0;j<NUMBER_TILES_X;j++)
			{
				if (kingdomMap[i][j] == -1) System.out.print("  ");
				else System.out.print(kingdomMap[i][j]+ " ");
			}
			System.out.println();
		}
	}
	private boolean canPlaceLeader(int row, int col)
	{
		//check if it's inside the board then check up,down,left,right for temple
		if (col>NUMBER_TILES_X) return false;

		//check to make sure we're not placing on another tile
		if (squares[row][col].getType() != Tile.BOARD) return false;

		//moving a leader from hand to board
		if ((row > 0 && (squares[row-1][col].getType() == Tile.TEMPLE)) || (row < NUMBER_TILES_Y && squares[row+1][col].getType() == Tile.TEMPLE) || (col > 0 && squares[row][col-1].getType() == Tile.TEMPLE) || (col+1 < NUMBER_TILES_X && squares[row][col+1].getType() == Tile.TEMPLE))
				return true;
		return false;
	}
	
	private boolean canPlaceTile(Tile tile, int row, int col)
	{
		if (col>NUMBER_TILES_X) return false;
		if (tile.getType() == Tile.FARM)
		{
			if (squares[row][col].getType() == Tile.RIVER) return true;
			else return false;
		}
		if (squares[row][col].getType() == Tile.BOARD) return true;
		return false;
	}
	
	public void clearActions()
	{
		for (int i=0;i<POSSIBLE_ACTIONS;i++)
			for (int j=0;j<ACTIONS_PER_TURN;j++)
				actionsTaken[i][j].setText("");
	}
	
	public void setTradeEnabled(boolean state) {trade.setEnabled(state);}
	private void doesJoinKingdom(int row, int col)
	{
		for (int i=0;i<2;i++)
			for (int j=0;j<4;j++)
				kingdomJoin[i][j] = -1;
		
		//left joins top
		if ((col > 0 && row > 0) && (kingdomMap[row][col-1] >= 0 && kingdomMap[row-1][col] >= 0) && (kingdomMap[row][col-1] != kingdomMap[row-1][col]))
		{
			System.out.println("LEFT JOINS TOP");
			{kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row-1; kingdomJoin[0][3] = col;}
			didJoin = true;
		}

		//left joins right
		if ((col > 0 && col+1 < NUMBER_TILES_X) && (kingdomMap[row][col-1] >= 0 && kingdomMap[row][col+1] >= 0) && (kingdomMap[row][col-1] != kingdomMap[row][col+1]))
		{
			System.out.println("LEFT JOINS RIGHT");
			if (kingdomJoin[0][0] == -1)
				{kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col+1;}
			else if ((kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row][col+1]) && (kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row][col-1]))
				{kingdomJoin[1][0] = row; kingdomJoin[1][1] = col-1; kingdomJoin[1][2] = row; kingdomJoin[1][3] = col+1;}
			didJoin = true;
		}		
		
		//left joins down
		if ((col > 0 && row+1 < NUMBER_TILES_Y) && (kingdomMap[row][col-1] >= 0 && kingdomMap[row+1][col] >= 0) && (kingdomMap[row][col-1] != kingdomMap[row+1][col]))
		{
			System.out.println("LEFT JOINS DOWN");
			if (kingdomJoin[0][0] == -1)
				{kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row+1; kingdomJoin[0][3] = col;}
			else if ((kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row+1][col]) && (kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row][col-1]))
				{kingdomJoin[1][0] = row; kingdomJoin[1][1] = col-1; kingdomJoin[1][2] = row+1; kingdomJoin[1][3] = col;}
			didJoin = true;
		}

		//up joins right
		if ((row > 0 && col+1 < NUMBER_TILES_X) && (kingdomMap[row-1][col] >= 0 && kingdomMap[row][col+1] >= 0) && (kingdomMap[row-1][col] != kingdomMap[row][col+1]))
		{
			System.out.println("UP JOINS RIGHT");
			if (kingdomJoin[0][0] == -1)
				{kingdomJoin[0][0] = row-1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col+1;}
			else if ((kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row-1][col]) && (kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row][col+1])) 
				{kingdomJoin[1][0] = row-1; kingdomJoin[1][1] = col; kingdomJoin[1][2] = row; kingdomJoin[1][3] = col+1;}
			didJoin = true;
		}

		//up joins down
		if ((row > 0 && row+1 < NUMBER_TILES_Y) && (kingdomMap[row-1][col] >= 0 && kingdomMap[row+1][col] >= 0) && (kingdomMap[row-1][col] != kingdomMap[row+1][col]))
		{
			System.out.println("UP JOINS DOWN");
			if (kingdomJoin[0][0] == -1)
				{kingdomJoin[0][0] = row-1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row+1; kingdomJoin[0][3] = col;}
			else if ((kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row-1][col]) && (kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row+1][col]))
				{kingdomJoin[1][0] = row-1; kingdomJoin[1][1] = col; kingdomJoin[1][2] = row+1; kingdomJoin[1][3] = col;}
			didJoin = true;
		}

		//right joins down
		if ((col+1 > NUMBER_TILES_X && row+1 < NUMBER_TILES_Y) && (kingdomMap[row+1][col] >= 0 && kingdomMap[row][col+1] >= 0) && (kingdomMap[row][col+1] != kingdomMap[row+1][col]))
		{
			System.out.println("RIGHT JOINS DOWN");
			if (kingdomJoin[0][0] == -1)
				{kingdomJoin[0][0] = row+1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col+1;}
			else if ((kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row][col+1]) && (kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]] != kingdomMap[row+1][col]))
				{kingdomJoin[1][0] = row+1; kingdomJoin[1][1] = col; kingdomJoin[1][2] = row; kingdomJoin[1][3] = col+1;}
			didJoin = true;
		}
		
		//didn't join but extended a kingdom
		if (kingdomJoin[0][0] == -1)
		{
			//left
			if (col > 0 && kingdomMap[row][col-1] != -1) {kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col;}
			//right
			if (col < (NUMBER_TILES_X -1) && kingdomMap[row][col+1] != -1) {kingdomJoin[0][0] = row; kingdomJoin[0][1] = col+1; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col;}
			//up
			if (row > 0 && kingdomMap[row-1][col] != -1) {kingdomJoin[0][0] = row-1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col;}
			//down
			if (row < (NUMBER_TILES_Y -1) && kingdomMap[row+1][col] != -1) {kingdomJoin[0][0] = row+1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col;}
			
			didJoin = false;
		}
	}
	private void resetKingdomJoin() {for (int i=0;i<2;i++)for (int j=0;j<4;j++)kingdomJoin[i][j]=-1;}
	public void setActionsTaken(String status,int row,int col) {this.actionsTaken[row][col].setText(status);}

	private class DragMouseAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent e) 
		{
			//trading in tiles
			if (e.isShiftDown()) return;
			//turn is over
			if (game.getNumberActionsTaken() >= 2) return;
//			System.out.println("E: "+e.getSource());
			Tile c;
			try {c = (Tile) e.getSource();}
			catch (Exception ex) {return;}
//			System.out.println("ENABLED: "+c.isEnabled());
			
			//tile has been disabled
			if (!c.isEnabled()) return;
			
			pickedUpRow = c.getY() / TILE_HEIGHT;
			pickedUpCol = c.getX() / TILE_WIDTH;

			System.out.println("mousePressed TYPE: "+c.getType()+" NAME: "+c.getName()+" IS ON BOARD: "+c.isOnBoard());			
			
			//return if user clicked on on the player area
			if (!c.isLeader() && c.isOnBoard()) return;
			
			//determine the players action
			if (c.isLeader() && !c.isOnBoard()) game.setCurrentAction(PLACING_LEADER);
			else if (c.isLeader() && c.isOnBoard()) game.setCurrentAction(MOVING_LEADER);
			else game.setCurrentAction(PLACING_TILE);

			tileBeingDragged = c;
			c.getTransferHandler().exportAsDrag(c, e, TransferHandler.COPY);
		}
		public void mouseClicked(MouseEvent e)
		{
			System.out.println("CLICK: "+e.getSource());
			Tile c;
			try {c = (Tile) e.getSource();}
			catch (Exception ex) {return;}

			Tile[] playerTiles = new Tile[6];
			int i = 0;
			
			if (!c.isLeader() && !c.isOnBoard()) c.setEnabled(!c.isEnabled());
			
			//set the players tiles to the state of the tiles on the board
			for (Component t : playerPanel.getComponents())
			{
				try 
				{
					if (((Tile)t).getType() < 4)
						playerTiles[i++] = (Tile)t;
				}
				catch (Exception ex) {continue;}
			}
			player.setTiles(playerTiles);
		}
	}
	private class DragHandler extends TransferHandler
	{
		private int trow,tcol;

		public DragHandler(String icon) {super(icon);}
		/** Called repeatedly during drag operation to determine if a location is droppable
		 * @params comp: this is the tile the mouse is over
		 */
		public boolean canImport(JComponent comp, DataFlavor[] flavor)
		{
			Tile c;
			try {c = (Tile) comp;}
			catch (Exception e) {return false;} 
			
//			System.out.println("canImport TYPE: "+c.getType()+" NAME: "+c.getName()+" IS ON BOARD: "+c.isOnBoard());

			trow = c.getY() / TILE_HEIGHT;
			tcol = c.getX() / TILE_WIDTH;
			if (trow < NUMBER_TILES_Y && tcol < NUMBER_TILES_X) doesJoinKingdom(trow,tcol);
			for (int i=0;i<2;i++)
			{
				for (int j=0;j<4;j++)
					System.out.print(kingdomJoin[i][j]+" ");
				System.out.println();
			}

			//placing a leader incorrectly
			System.out.println(canPlaceLeader(trow,tcol));
			if ((game.getCurrentAction() == PLACING_LEADER || game.getCurrentAction() == MOVING_LEADER) && (!canPlaceLeader(trow,tcol) || didJoin)) 
			{
				System.out.println("LEADER INCORRECT: "+trow+" "+tcol);
				resetKingdomJoin();
				return false;
			}
			//placing a tile incorrectly
			if (game.getCurrentAction() == PLACING_TILE && (!canPlaceTile(tileBeingDragged,trow,tcol) || (kingdomJoin[0][0] != -1 && kingdomJoin[1][0] != -1))) 
			{
				resetKingdomJoin();
				return false;
			}
			
			
			return true;
		}
		/** Called after a drop.  Affects the tile where the drop is happening.
		 * @params comp this is the tile where something is being dropped
		 */
		public boolean importData(JComponent comp, Transferable t)
		{
			Tile tileBeingDroppedOn = (Tile)comp;
			
			//this is where we're trying to drop the tile
			droppedRow = tileBeingDroppedOn.getY() / TILE_HEIGHT;
			droppedCol = tileBeingDroppedOn.getX() / TILE_WIDTH;

//			System.out.println("importData TYPE: "+tileBeingDroppedOn.getType()+" NAME: "+tileBeingDroppedOn.getName()+" IS ON BOARD: "+tileBeingDroppedOn.isOnBoard());
			
			//replace the drop location with the tile was picked up
			boardPanel.remove(squares[droppedRow][droppedCol]);
			squares[droppedRow][droppedCol] = new Tile(tileBeingDragged.getType());
//			squares[droppedRow][droppedCol] = tileBeingDragged;
			squares[droppedRow][droppedCol].setOnBoard(true);
			squares[droppedRow][droppedCol].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			squares[droppedRow][droppedCol].setBounds(droppedCol*TILE_WIDTH, droppedRow*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
		    squares[droppedRow][droppedCol].addMouseListener(listener);
		    squares[droppedRow][droppedCol].setTransferHandler(new DragHandler("icon"));
			boardPanel.add(squares[droppedRow][droppedCol]);

//			Component[] c = boardPanel.getComponents();
//			for (Component x : c) System.out.println(x.toString());
			
			//moving a leader anywhere 
			if (game.getCurrentAction() == PLACING_LEADER || game.getCurrentAction() == MOVING_LEADER) 
			{
				//set leader to on board
				player.getLeaders()[tileBeingDragged.getLeaderNumber()].setOnBoard(true);
				displayLeaders(player.getLeaders());
			}
			//moved a leader to the board
			if (game.getCurrentAction() == PLACING_LEADER) actionsTaken[game.getCurrentAction()][game.getNumberActionsTaken()].setText(""+(1+game.getNumberActionsTaken()));
			
			//moved a leader that was on the board already
			else if (game.getCurrentAction() == MOVING_LEADER)
			{
				boardPanel.remove(tileBeingDragged);
				//replace the leader that was picked up with an empty board tile
				squares[pickedUpRow][pickedUpCol] = new Tile(Tile.BOARD);
				squares[pickedUpRow][pickedUpCol].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				squares[pickedUpRow][pickedUpCol].setBounds(pickedUpCol*TILE_WIDTH, pickedUpRow*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
			    squares[pickedUpRow][pickedUpCol].addMouseListener(listener);
			    squares[pickedUpRow][pickedUpCol].setTransferHandler(new DragHandler("icon"));
				boardPanel.add(squares[pickedUpRow][pickedUpCol]);
				
				//advance the action
				actionsTaken[PLACING_LEADER][game.getNumberActionsTaken()].setText(""+(1+game.getNumberActionsTaken()));
			}
			
			//placing a tile
			else if (game.getCurrentAction() == PLACING_TILE)
			{	
				actionsTaken[PLACING_TILE][game.getNumberActionsTaken()].setText(""+(1+game.getNumberActionsTaken()));
				playerPanel.remove(tileBeingDragged);
				tileBeingDragged.setEnabled(false);
			}
			game.gameLock.lockResume();
			return true;
		}
		/** Called after a drop.  Affects the tile being dropped. Manages kingdom properties.
		 * params source: this is the tile that is being dropped
		 */
		public void exportDone(JComponent source, Transferable data, int action)
		{
				Tile c = (Tile)source;

//				System.out.println("exportDone TYPE: "+c.getType()+" NAME: "+c.getName()+" IS ON BOARD: "+c.isOnBoard());
				System.out.println("ROW: "+droppedRow+" COL: "+droppedCol);
				
				doesJoinKingdom(droppedRow,droppedCol);

				for (int i=0;i<2;i++)
				{
					for (int j=0;j<4;j++)
						System.out.print(kingdomJoin[i][j]+" ");
					System.out.println();
				}
				//didn't join anything
				if (kingdomJoin[0][0] == -1) kingdomMap[droppedRow][droppedCol] = numberOfKingdoms++;
				
				//joined or extended
				else 
				{
					//joining
					if ((kingdomJoin[0][2] != droppedRow) != (kingdomJoin[0][3] != droppedCol))
					{
						floodFill(kingdomJoin[0][0],kingdomJoin[0][1],numberOfKingdoms);
						floodFill(kingdomJoin[0][2],kingdomJoin[0][3],numberOfKingdoms);
						kingdomMap[droppedRow][droppedCol] = numberOfKingdoms++;
					}
					//extending
					else 
						kingdomMap[droppedRow][droppedCol] = kingdomMap[kingdomJoin[0][0]][kingdomJoin[0][1]];
					
				}
				printMap();				

				//didn't join any kingdoms
//				if (kingdomJoin[0][0] == -1)
//				{	
//					System.out.println("NOT JOINING");
//					//check left
//					if (col > 0 && kingdomMap[row][col-1] >=0) kingdomMap[row][col] = kingdomMap[row][col-1];
//					
//					//check up
//					else if (row > 0 && kingdomMap[row-1][col] >=0) kingdomMap[row][col] = kingdomMap[row-1][col];
//					
//					//check right
//					else if (col+1 < NUMBER_TILES_X && kingdomMap[row][col+1] >=0) kingdomMap[row][col] = kingdomMap[row][col+1];
//	
//					//check down
//					else if (row+1 < NUMBER_TILES_Y && kingdomMap[row+1][col] >=0) kingdomMap[row][col] = kingdomMap[row+1][col];
//					
//					//create a new kingdom
//					else kingdomMap[row][col] = numberOfKingdoms++;
//				}				

				frame.repaint();
		}
	}
}
