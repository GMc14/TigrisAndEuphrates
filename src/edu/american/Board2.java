//package edu.american;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Toolkit;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//
//import javax.swing.BorderFactory;
//import javax.swing.JButton;
//import javax.swing.JComponent;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.TransferHandler;
//
//public class Board2
//{
//	//flood fill
//	
//	/**Number of tiles that can be placed horizontally*/
//	public static final int NUMBER_TILES_X = 16;
//	/**Number of tiles that can be placed vertically*/
//	public static final int NUMBER_TILES_Y = 11;
//	/**Tile width in pixels*/
//	public static int TILE_WIDTH;
//	/**Tile height in pixels*/
//	public static int TILE_HEIGHT;
//	/**Width of the play area*/
//	public static int BOARD_WIDTH;
//	/**Height of the play area*/
//	public static int BOARD_HEIGHT;
//	/**Width of the window*/
//	public static int WINDOW_WIDTH;
//	/**Height of the window*/
//	public static int WINDOW_HEIGHT;
//	
//	/**The frame*/
//	private JFrame frame;
//	
//	/**Represent the squares on the board*/
//	private Tile[][] squares;
//	
//	/**Numeric representation of the board*/
//	private int[][] filledIn;
//	
//	/**Will be added to the components so they can be dragged*/
//	private MouseListener listener;
//	
//	/**Determines if we dropped a tile or not*/
//	private boolean didDrop = false;
//	
//	/**Number of pieces placed this turn*/
//	private int numberPlaced=0;
//	
//	/**Ends the current turn*/
//	private JButton turn;
//	
//	/**Display word status*/
//	private JLabel status; 
//	
//	/**Skips this phase of the turn*/
//	private JButton pass;
//
//	/**The game*/
//	private Game game;
//	
//	/**Where the user area should start*/
//	private int boardOffset;
//	
//	/**This players list of tiles*/
//	private Tile[] playerTiles;
//	
//	/**This players list of leaders*/
//	private Tile[] leaderTiles;
//	
//	/**This is the current player*/
//	private Player player;
//	
//	/**This is the tile the player is dragging*/
//	private Tile tileBeingDragged;
//	
//	/**This holds the player information*/
//	private JPanel playerPanel;
//	
//	/**This holds the board*/
//	private JPanel boardPanel;
//	
//	/**This is a map of the current kingdoms on the board*/
//	private int[][] kingdomMap;
//	
//	private int numberOfKingdoms=0;
//	
//	private int row,col,trow,tcol,counter=0;
//	
//	/**Row and column list of where kingdoms join
//	 * [row1,col1,row2,col2][row1,col1,row2,col2]
//	 * */
//	private int[][] kingdomJoin;
//	
//	/**Constructs the board*/
//	public Board(final Game game)
//	{
////		super();
//		
//		this.game = game;
//
//		//initialize arrays
//		playerTiles = new Tile[6];
//		leaderTiles = new Tile[4];
//		kingdomMap = new int[NUMBER_TILES_Y][NUMBER_TILES_X];
//		filledIn = new int[NUMBER_TILES_Y][NUMBER_TILES_X];
//		kingdomJoin = new int[2][4];
//		
//		
//		//set initial player tile arrays to null
//		for (int i=0;i<6;i++)
//			playerTiles[i] = null;
//		for (int i=0;i<4;i++)
//			leaderTiles[i] = null;
//		
//		//initialize kingdom mapping to all -1 (no kingdoms)
//		for (int i=0;i<NUMBER_TILES_Y;i++)
//		{
//			for (int j=0;j<NUMBER_TILES_X;j++)
//			{
//				kingdomMap[i][j] = -1;
//				filledIn[i][j] = -1;
//			}
//		}
//	
//		//get size of the screen 
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		Dimension scrnsize = toolkit.getScreenSize();
//		
//		//get the screen size based on the resolution of the screen being used
//		WINDOW_WIDTH = (int)scrnsize.getWidth();
//		WINDOW_HEIGHT = (int)scrnsize.getHeight() - 22;
//		
//		//calculate the tile width in relation to the board size
//		TILE_WIDTH = (int)WINDOW_WIDTH / (NUMBER_TILES_X+4);
//		TILE_HEIGHT = (int)WINDOW_HEIGHT / (NUMBER_TILES_Y);
//		
//		//calculate the board size in relation to the tile sizes
//		BOARD_WIDTH = TILE_WIDTH * NUMBER_TILES_X;
//		BOARD_HEIGHT = TILE_HEIGHT * NUMBER_TILES_Y;
//		
//		boardOffset = WINDOW_WIDTH-3*TILE_WIDTH;
//
//		//set up the frame
//		frame = new JFrame("Tigris and Euphrates");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
//		
//		//create the panels
//		playerPanel = new JPanel();
//		boardPanel = new JPanel();
//		
//		//set up drag and drop
//	    listener = new DragMouseAdapter();
//
//	    //put the JLabels on the board
//	    squares = new Tile[NUMBER_TILES_Y][NUMBER_TILES_X];
//	    
//		for (int i=0;i<NUMBER_TILES_Y;i++)
//		{
//			for (int j=0;j<NUMBER_TILES_X;j++)
//			{
//				//check for temple
//				if ((i==1 && j==1) || (i==2 && j==5) || (i==0 && j==10) || (i==1 && j==15) || (i==4 && j==13) || (i==6 && j==8) || (i==7 && j==1) || (i==9 && j==5) || (i==10 && j==10) || (i==8 && j==14))
//				{
//					filledIn[i][j] = Tile.TEMPLE;
//					squares[i][j] = new Tile(Tile.TEMPLE);
//					squares[i][j].setOnBoard(true);
//					
//					//this is also a kingdom!
//					kingdomMap[i][j] = Tile.TEMPLE;
//				}
//				//check for river
//				else if ((i==3 && j<4) || (i==2 && j==3) || (j==4 && i<3) || (i==0 && (j>4 && j<9)) || (j==12 && (i>=0 && i<3)) || (j==13 && (i>1 && i<4)) || (j==14 && (i>2 && i<7)) || (j==15 && (i>2 && i<5)) || (i==6 && (j>11 && j<14)) || (j==12 && (i>6 && i<9)) || (i==8 && (j>5 && j<12)) || (i==7 && (j>2 && j<7)) || (i==6 && j<4)) 
//				{
//					filledIn[i][j] = Tile.RIVER;
//					squares[i][j] = new Tile(Tile.RIVER);
//				}
//				//otherwise its empty
//				else
//				{
//					filledIn[i][j] = Tile.BOARD;
//					squares[i][j] = new Tile(Tile.BOARD);
//				}
//				squares[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
//				squares[i][j].setBounds(j*TILE_WIDTH, i*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
//			    squares[i][j].addMouseListener(listener);
//			    squares[i][j].setTransferHandler(new DragHandler("icon"));
//			    boardPanel.add(squares[i][j]);
//			}
//		}
//			    
//		//setup the buttons
//		turn = new JButton("End turn");
//		pass = new JButton("Pass");
//		
//		turn.setBounds(boardOffset, WINDOW_HEIGHT-3*TILE_HEIGHT,70,20);
//		pass.setBounds(turn.getX()+turn.getWidth()+5, WINDOW_HEIGHT-3*TILE_HEIGHT,60,20);
//		
//		//ends a turn by clearing all the player tiles
//		//the actions listener removes all the players tiles and leaders from the board as well as removes all played and disabled tiles from the players tile[] array
//		turn.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				for (int i=0;i<playerTiles.length;i++)
//					if (!playerTiles[i].isEnabled())
//					{
//						playerPanel.remove(playerTiles[i]);
//						playerTiles[i]=null;
//					}
//			
//				player.setTiles(playerTiles);
//				player.setLeaders(leaderTiles);
//				
//				game.gameLock.lockResume();
//		}});
//		
//		//just unlocks the game thread and moves to the next phase
//		pass.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e){game.gameLock.lockResume();}});
//		
//		//add the buttons to the player panel
//		playerPanel.add(turn);
//		playerPanel.add(pass);
//		
//		//setup the status bar
//		status = new JLabel();
//		status.setFont(new Font("Courier New",Font.BOLD,14));
//		status.setBounds(boardOffset-(int)(TILE_WIDTH/2), WINDOW_HEIGHT - 2 * TILE_HEIGHT,WINDOW_WIDTH - boardOffset,20);
//		playerPanel.add(status);
//
//		//add everything to th frame
//		frame.add(boardPanel);
//		frame.add(playerPanel);
//
//		playerPanel.setLayout(null);
//		playerPanel.setBounds(BOARD_WIDTH, 0, WINDOW_WIDTH-BOARD_WIDTH, WINDOW_HEIGHT);
//		playerPanel.setName("playerArea");
//		playerPanel.addMouseListener(listener);
//		playerPanel.setTransferHandler(new DragHandler("icon"));
//			
//	    //board panel
//	    boardPanel.setLayout(null);
//		boardPanel.setBounds(0, 0, BOARD_WIDTH, WINDOW_HEIGHT);
//		boardPanel.setName("boardPanel");
//
//		frame.setVisible(true);
//		
//		generateKingdomMap();
//		printMap();
//	}
//	
//	private void generateKingdomMap()
//	{
//		for (int i=0;i<NUMBER_TILES_Y;i++)
//		{
//			for (int j=0;j<NUMBER_TILES_X;j++)
//			{
//				if (kingdomMap[i][j] != -1)
//					floodFill(i,j,numberOfKingdoms++);
//			}
//		}
//	}
//	private void floodFill(int row, int col,int kingdomNumber)
//	{
//		int oldNumber;
//		oldNumber = kingdomMap[row][col];
//		kingdomMap[row][col]=kingdomNumber;
//		
//		//go up
//		if(row>0 && kingdomMap[row-1][col] == oldNumber) floodFill(row-1,col,kingdomNumber);
//		//go down
//		if(row+1<NUMBER_TILES_Y && kingdomMap[row+1][col] == oldNumber) floodFill(row+1,col,kingdomNumber);
//		//go left
//		if(col>0 && kingdomMap[row][col-1] == oldNumber) floodFill(row,col-1,kingdomNumber);
//		//go right
//		if(col+1<NUMBER_TILES_X && kingdomMap[row][col+1] == oldNumber) floodFill(row-1,col,kingdomNumber);
//	}
//	public void setPassEnabled(boolean state) {pass.setEnabled(state);}
//	public void setEndTurnEnabled(boolean state) {turn.setEnabled(state);}
//	
//	/** Display the players tiles on the screen
//	 * @param tiles: array of tiles the player owns
//	 * @param leaders: array of the players leaders
//	 */
//	public void showPlayerArea(Player p)
//	{	
//		this.player = p;
//		
//		//clean up the players tiles
//		for (int i=0;i<6;i++)
//			if (playerTiles[i]!=null)
//				playerPanel.remove(playerTiles[i]);
//		
//		//location of where the player tiles start
//		int x,y;
//				
//		//display the users tiles
//		for (int i=0;i<6;i++)
//		{
//			//get location for the tile
//			x = (i % 2) * TILE_WIDTH + boardOffset;
//			y = (int)i/2 * TILE_HEIGHT + TILE_HEIGHT;
//			
//			playerTiles[i] = new Tile((player.getTiles())[i].getType());
//			playerTiles[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
//			playerTiles[i].setBounds(x,y, TILE_WIDTH, TILE_HEIGHT);
//		    playerTiles[i].addMouseListener(listener);
//		    playerTiles[i].setTransferHandler(new DragHandler("icon"));
//		    playerPanel.add(playerTiles[i]);
//		}
//		
//		displayLeaders(player.getLeaders());
//		frame.repaint();
//	}
//
//	private void displayLeaders(Tile[] leaders)
//	{
//		int x,y;
//		//clean up the players leader tiles
//		for (int i=0;i<4;i++)
//			if (leaderTiles[i] != null)
//				playerPanel.remove(leaderTiles[i]);
//		
//		//display the users leaders
//		for (int i=0;i<4;i++)
//		{
//			System.out.println("LEADER["+i+"]: "+leaders[i].isOnBoard());
//			if (!leaders[i].isOnBoard())
//			{
//				x = (i % 2) * TILE_WIDTH + boardOffset;
//				y = (int)i/2 * TILE_HEIGHT + (5*TILE_HEIGHT);
//				
//				leaderTiles[i] = (player.getLeaders())[i];
//				leaderTiles[i].setBounds(x,y,TILE_WIDTH,TILE_HEIGHT);
//				leaderTiles[i].addMouseListener(listener);
//			    leaderTiles[i].setTransferHandler(new DragHandler("icon"));
//			    playerPanel.add(leaderTiles[i]);
//			}
//		}
//	}
//	private void printMap()
//	{
//		for (int i=0;i<NUMBER_TILES_Y;i++)
//		{
//			for (int j=0;j<NUMBER_TILES_X;j++)
//			{
//				if (kingdomMap[i][j] == -1) System.out.print("  ");
//				else System.out.print(kingdomMap[i][j]+ " ");
//			}
//			System.out.println();
//		}
//	}
//	private boolean canPlaceLeader(int row, int col)
//	{
//		//check if it's inside the board then check up,down,left,right for temple
//		if (col>NUMBER_TILES_X) return false;
//
//		//check to make sure we're not joining two kingdoms
//
//		//moving a leader from hand to board
//		if ((row > 0 && (filledIn[row-1][col] == Tile.TEMPLE)) || (row < NUMBER_TILES_Y && filledIn[row+1][col] == Tile.TEMPLE) || (col > 0 && filledIn[row][col-1] == Tile.TEMPLE) || (col+1 < NUMBER_TILES_X && filledIn[row][col+1] == Tile.TEMPLE))
//				return true;
//		return false;
//	}
//	
//	private boolean canPlaceFarm(int row, int col)
//	{
//		if (col>NUMBER_TILES_X) return false;
//		if (filledIn[row][col] == Tile.RIVER) return true;
//		return false;
//	}
//	
//	private void doesJoinKingdom(int row, int col)
//	{
//		for (int i=0;i<2;i++)
//			for (int j=0;j<4;j++)
//				kingdomJoin[i][j] = -1;
//		
//		//left matches top
//		if ((col > 0 && row > 0) && (kingdomMap[row][col-1] >= 0 && kingdomMap[row-1][col] >= 0) && (kingdomMap[row][col-1] == kingdomMap[row-1][col]))
//		{
//			System.out.println("LEFT MATCHES TOP");
//			{kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row-1; kingdomJoin[0][3] = col;}
//		}
//
//		//left matches rights
//		if ((col > 0 && col+1 < NUMBER_TILES_X) && (kingdomMap[row][col-1] >= 0 && kingdomMap[row][col+1] >= 0) && (kingdomMap[row][col-1] == kingdomMap[row][col+1]))
//		{
//			System.out.println("LEFT MATCHES RIGHT");
//			if (kingdomJoin[0][0] == -1)
//				{kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col+1;}
//			else
//				{kingdomJoin[1][0] = row; kingdomJoin[1][1] = col-1; kingdomJoin[1][2] = row; kingdomJoin[1][3] = col+1;}
//		}		
//		
//		//left matches down
//		if ((col > 0 && row+1 < NUMBER_TILES_Y) && (kingdomMap[row][col-1] >= 0 && kingdomMap[row+1][col] >= 0) && (kingdomMap[row][col-1] == kingdomMap[row+1][col]))
//		{
//			System.out.println("LEFT MATCHES DOWN");
//			if (kingdomJoin[0][0] == -1)
//				{kingdomJoin[0][0] = row; kingdomJoin[0][1] = col-1; kingdomJoin[0][2] = row+1; kingdomJoin[0][3] = col;}
//			else 
//				{kingdomJoin[1][0] = row; kingdomJoin[1][1] = col-1; kingdomJoin[1][2] = row+1; kingdomJoin[1][3] = col;}
//		}
//
//		//up matches right
//		if ((row > 0 && col+1 < NUMBER_TILES_X) && (kingdomMap[row-1][col] >= 0 && kingdomMap[row][col+1] >= 0) && (kingdomMap[row-1][col] == kingdomMap[row][col+1]))
//		{
//			System.out.println("UP MATCHES RIGHT");
//			if (kingdomJoin[0][0] == -1)
//				{kingdomJoin[0][0] = row-1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col+1;}
//			else 
//				{kingdomJoin[1][0] = row-1; kingdomJoin[1][1] = col; kingdomJoin[1][2] = row; kingdomJoin[1][3] = col+1;}
//		}
//
//		//up matches down
//		if ((row > 0 && row+1 < NUMBER_TILES_Y) && (kingdomMap[row-1][col] >= 0 && kingdomMap[row+1][col] >= 0) && (kingdomMap[row-1][col] == kingdomMap[row+1][col]))
//		{
//			System.out.println("UP MATCHES DOWN");
//			if (kingdomJoin[0][0] == -1)
//				{kingdomJoin[0][0] = row-1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row+1; kingdomJoin[0][3] = col;}
//			else 
//				{kingdomJoin[1][0] = row-1; kingdomJoin[1][1] = col; kingdomJoin[1][2] = row+1; kingdomJoin[1][3] = col;}
//		}
//
//		//right matches down
//		if ((col+1 > NUMBER_TILES_X && row+1 < NUMBER_TILES_Y) && (kingdomMap[row+1][col] >= 0 && kingdomMap[row][col+1] >= 0) && (kingdomMap[row+1][col] == kingdomMap[row][col+1]))
//		{
//			System.out.println("RIGHT MATCHES DOWN");
//			if (kingdomJoin[0][0] == -1)
//				{kingdomJoin[0][0] = row+1; kingdomJoin[0][1] = col; kingdomJoin[0][2] = row; kingdomJoin[0][3] = col+1;}
//			else 
//				{kingdomJoin[1][0] = row+1; kingdomJoin[1][1] = col; kingdomJoin[1][2] = row; kingdomJoin[1][3] = col+1;}
//		}
//	}
//	public void setStatus(String status) {this.status.setText(status);}
//	/** 
//	 * @return Current number of tiles placed this turn
//	 */
//	public int getNumberPlaced() {return numberPlaced;}
//	
//	private class DragMouseAdapter extends MouseAdapter
//	{
//		public void mousePressed(MouseEvent e) 
//		{
//			//return if user clicked on on the player area
//			
//			Tile c = (Tile) e.getSource();
//			System.out.println("CLICKED TILE NAME: "+c.getName()+" IS ON BOARD: "+c.isOnBoard());
//			if (c.getName().equals("playerArea")) return;
//			if(game.inPhase3)
//			{
//				//disable the tiles the player is selecting
//				if (!c.isLeader() && !c.isOnBoard()) c.setEnabled(!c.isEnabled());
//				return;
//			}
//			
//			//return if the player tried to move a tile on the board (that isn't a leader)
//			if (c.isOnBoard() && !c.isLeader()) return;
//		  
//			//player tried to move a tile in phase 1
//			if (game.inPhase1 && !c.isLeader()) return;
//		  
//			//player tried to move a leader in phase 2
//			if (game.inPhase2 && c.isLeader()) return;
//		  
//			//we have made it through the gauntlet so we know we can drag this
//			tileBeingDragged = c;
//			c.getTransferHandler().exportAsDrag(c, e, TransferHandler.COPY);
//		}
//	}
//	private class DragHandler extends TransferHandler
//	{
//		public DragHandler(String icon) {super(icon);}
//		/** Called repeatedly during drag operation to determine if a location is droppable
//		 * @params comp: this is the tile the mouse is over
//		 */
//		public boolean canImport(JComponent comp, DataFlavor[] flavor)
//		{
//			trow = comp.getY() / TILE_HEIGHT;
//			tcol = comp.getX() / TILE_WIDTH;
//			
//			//phase 1 leader placement
//			if (game.inPhase1)
//			{
////				if (!comp.getName().equals("playerArea")) System.out.println(doesJoinKingdom(trow,tcol));
////				if (!comp.getName().equals("playerArea") && doesJoinKingdom(trow,tcol)) return false; 
//				//get this players leader tiles to check if they one we're looking at is on the board
//				if(tileBeingDragged.isOnBoard() && comp.getName().equals("playerArea")) return true;
//				if(!canPlaceLeader(trow,tcol)) return false;
//			}
//			
////			//player is moving a leader that's on the board
//			if (game.inPhase2 && tileBeingDragged.getType() == Tile.FARM) return canPlaceFarm(trow,tcol);
//			if (comp.getName().equals("board")) return true;
//			
//			return false;
//		}
//		/** Called after a drop.  Affects the tile where the drop is happening.
//		 * @params comp this is the tile where something is being dropped
//		 */
//		public boolean importData(JComponent comp, Transferable t)
//		{
//			//leader from board to player area
//			Tile droppedTile = (Tile)comp;
//			//this is where we're trying to drop the tile
//			row = comp.getY() / TILE_HEIGHT;
//			col = comp.getX() / TILE_WIDTH;
//			trow = tileBeingDragged.getY() / TILE_HEIGHT;
//			tcol = tileBeingDragged.getX() / TILE_WIDTH;
//
//			System.out.println("import ROW: "+row+" COL: "+col);
//
//			//moving a leader from board to the player area
//			if (tileBeingDragged.isOnBoard() && comp.getName().equals("playerArea"))
//			{
//				//get the leader number and set it to off the board
//				int leaderNumber = tileBeingDragged.getLeaderNumber();
//				leaderTiles[leaderNumber] = tileBeingDragged;
//				leaderTiles[leaderNumber].setOnBoard(false);				
//				filledIn[row][col]=Tile.BOARD;
//				displayLeaders(leaderTiles);
//				
//				frame.repaint();
//				didDrop = true;
//				return true;
//			}
//			else if (super.importData(comp, t))
//			{		
//
//				//check for phase and to see if a leader can be dropped here
//				if (game.inPhase1)
//				{
//					System.out.println("TILE BEING DRAGGED NAME: "+tileBeingDragged.getName()+" IS ON BOARD: "+tileBeingDragged.isOnBoard());
//					System.out.println("DROPPED TILE NAME: "+droppedTile.getName()+" IS ON BOARD: "+droppedTile.isOnBoard());
//					
//					//dropping a leader from the player area to the board
//					if (droppedTile.isOnBoard() && canPlaceLeader(row,col))
//					{
//						int leaderNumber = tileBeingDragged.getLeaderNumber();
//						System.out.println("MOVING FROM HAND TO BOARD");
//						
//						droppedTile = tileBeingDragged;
//						droppedTile.setOnBoard(true);
//						droppedTile.setName(tileBeingDragged.getName());
//						System.out.println("DROPPED TILE NAME: "+droppedTile.getName()+" IS ON BOARD: "+droppedTile.isOnBoard());
//						
//						leaderTiles[leaderNumber].setOnBoard(true);
//						filledIn[row][col] = tileBeingDragged.getType();
//						
//						displayLeaders(leaderTiles);
//						frame.repaint();
//					}
//					//moving a leader that's on the board already
//					else if (tileBeingDragged.isOnBoard())
//					{
//						droppedTile = new Tile(Tile.BOARD);
//						droppedTile.setName("board");
//						droppedTile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//						droppedTile.setBounds(tileBeingDragged.getX(),tileBeingDragged.getY(), TILE_WIDTH, TILE_HEIGHT);
//						droppedTile.addMouseListener(listener);
//						droppedTile.setTransferHandler(new DragHandler("icon"));
//						boardPanel.remove(tileBeingDragged);
//						boardPanel.add(droppedTile);
//						
//						kingdomMap[trow][tcol] = -1;
//						filledIn[row][col] = tileBeingDragged.getType();
//					}		
//					System.out.println("TILE BEING DRAGGED NAME: "+tileBeingDragged.getName()+" IS ON BOARD: "+tileBeingDragged.isOnBoard());
//				}
//				else 
//				{
//					System.out.println("import NAME OF DROPPED: "+droppedTile.getName());
//					filledIn[row][col] = tileBeingDragged.getType();
//					droppedTile.setOnBoard(true);
//					droppedTile.setName(tileBeingDragged.getName());
//				}
//				didDrop = true;
//				//alert that we did a drop
//				return true;
//			}
//			return false;
//		}
//		/** Called after a drop.  Affects the tile being dropped and drop location.
//		 * params source: this is the tile that is being dropped
//		 */
//		public void exportDone(JComponent source, Transferable data, int action)
//		{
//			if(didDrop)
//			{
//				Tile clickedTile = (Tile)source;
//				System.out.println("EXPORT TROW: "+trow+" TCOL: "+tcol);
//				System.out.println("EXPORT ROW: "+row+" COL: "+col);
//				System.out.println("TILE BEING DRAGGED NAME: "+tileBeingDragged.getName()+" IS ON BOARD: "+tileBeingDragged.isOnBoard());
//				//end the phase accordingly
//				if (game.inPhase1) 
//				{				
//					didDrop = false;
//					game.gameLock.lockResume();
//				}
//				if (game.inPhase2)
//				{
//					numberPlaced++;
//					if (numberPlaced == 3) {numberPlaced = 0;game.gameLock.lockResume();}
//					didDrop = false;
//					source.setEnabled(false);
//					//remove the tile that was moved 
//					playerPanel.remove(source);
//				}
//				doesJoinKingdom(row,col);
//
//				for (int i=0;i<2;i++)
//				{
//					for (int j=0;j<4;j++)
//						System.out.print(kingdomJoin[i][j]+" ");
//					System.out.println();
//				}
//				//didn't join any kingdoms
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
////				printMap();
//				frame.repaint();
//			}
//		}
//	}
//}
