/*
Tic-Tac-Toe by Lucas L. Pena
some parts of code were built after referencing
the textbook, various java web sites and of corse stackoverflow but used for UI help
all login was created by me and course materials
ENJOY!
*/
package TicTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TTT extends JFrame {
   
	//Setting Global Constant
	public static final int COLUMNS = 3;
	public static final int ROWS = 3;
	//Calculating constants for GUI dimensions
	public static final int CELL_SIZE = 150;					//cell width and height
	public static final int LINE_WIDTH = 4;                  	//border thickness
	public static final int CANVAS_WIDTH = (CELL_SIZE * COLUMNS);  //overall canvas width
	public static final int CANVAS_HEIGHT = (CELL_SIZE * ROWS);	//overall canvas height
	public static final int LINE_WIDHT_HALF = LINE_WIDTH / 2; 	//half border width
	//Cell constants for displaying crosses and noughts below
	public static final int CELL_PADDING = CELL_SIZE / 8; 				//padding between cell border and symbol
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; //setting all other empty space to symbol
	public static final int SYMBOL_STROKE_WIDTH = 4; 					//setting symbol weight
	public static int NOUGHTwins = 0;
	public static int CROSSwins = 0;
	//Enum used for efficiency in defining game state 
	public enum GameState
	{
		PLAYING, NOUGHT_WON, CROSS_WON, DRAW
	}
	private GameState currentState;	//creating a GameState called currentState
	//Enum used for efficiency in defining cell state
	public enum Player
	{
		EMPTY, NOUGHT, CROSS
	}
	private Player currentPlayer;	//creating a CellState called currentCell
 
	private Player[][] board   ; 	//Creating a  2d array of CellStates called board
	private CreateBox canvas; 	//Create a (JPanel) object
	private JLabel statusBar;  	//Creating a status bar object
	private JLabel authorBar;
 
	//an extends to draw custom shapes for JPanel
	class CreateBox extends JPanel
	{
		public void paintComponent(Graphics graphic) //called with repaint()
		{  
			super.paintComponent(graphic);    			//fill the entire background
			setBackground(new Color(134,38,51)); 		//with garnet
			graphic.setColor(new Color (238,212,132));	//set bars to Gold
			
			for (int row = 1; row < ROWS; ++row)		//draw horizontal bars
			{
				graphic.fillRoundRect(0, CELL_SIZE * row - LINE_WIDHT_HALF, CANVAS_WIDTH-1, LINE_WIDTH, LINE_WIDTH, LINE_WIDTH);
			}
			for (int col = 1; col < COLUMNS; ++col)		//draw the vertical  bars
			{
				graphic.fillRoundRect(CELL_SIZE * col - LINE_WIDHT_HALF, 0, LINE_WIDTH, CANVAS_HEIGHT-1, LINE_WIDTH, LINE_WIDTH);
			}
 
			Graphics2D g2d = (Graphics2D)graphic;
			g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			
			for (int row = 0; row < ROWS; ++row)
			{
				for (int col = 0; col < COLUMNS; ++col)
				{
					int y1 = row * CELL_SIZE + CELL_PADDING;
					int x1 = col * CELL_SIZE + CELL_PADDING;
					if (board[row][col] == Player.CROSS) //if player is cross
					{
						g2d.setColor(Color.RED);	//draw a red cross using the below parameters
						int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
						int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
						g2d.drawLine(x1, y1, x2, y2);
						g2d.drawLine(x2, y1, x1, y2);
					} 
					else if (board[row][col] == Player.NOUGHT) //if player if nought
					{
						g2d.setColor(Color.BLUE);	//draw a blue circle
						g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE); //pass these parameters to draw oval
						
					}
				}
			}
			//generate the status bar message
			if (currentState == GameState.PLAYING) //for playing
			{
				statusBar.setForeground(Color.BLACK);	//color of font is set to black
				if (currentPlayer == Player.NOUGHT)		//if nought is current player
				{
					statusBar.setText("O Turn                 FSU     O wins:" + String.valueOf(NOUGHTwins) + " X wins:" + String.valueOf(CROSSwins));		//display, x turn
				} 
				else									//else cross current player
				{
					statusBar.setText("X Turn                 FSU     O wins:" + String.valueOf(NOUGHTwins) + " X wins:" + String.valueOf(CROSSwins));		//display, x turn
				}
			}
			else if (currentState == GameState.NOUGHT_WON)			//if current state is Nought won
			{
				statusBar.setForeground(Color.BLUE);				//use nought color
				NOUGHTwins++;
				statusBar.setText("'O' Wins! Click to play again!");//to display message
			}
			else if (currentState == GameState.CROSS_WON)			//if current state is cross won
			{
				statusBar.setForeground(Color.RED);					//use cross color
				CROSSwins++;
				statusBar.setText("'X' Wins! Click to play again!");//to display message
			}
			else if (currentState == GameState.DRAW)				//if currentstate is draw
			{
				statusBar.setForeground(Color.MAGENTA);
				statusBar.setText("Draw! Click to play again!");
			} 
		}
   }
	
	public TTT()	//constructor
	{
		//create a CreateBox (JPanel) with constant dimensions created above
		canvas = new CreateBox();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
 
		//create custom mouse listener for canvas window
		canvas.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e) //custom actions on a mouse click
			{  
				int mouseY = e.getY();	//get row coordinates
				int mouseX = e.getX();	//get column coordinates
				int rowSelected = mouseY / CELL_SIZE; 	//calculate row cell
				int colSelected = mouseX / CELL_SIZE;	//calculate column cell
 
				if (currentState == GameState.PLAYING)	//set current state to playing
				{
					//if row selected is valid, and colums are vaid, AND selected cell in 2d array is empty...
					if ((rowSelected >= 0) && (rowSelected < ROWS) && (colSelected >= 0) && (colSelected < COLUMNS) && (board[rowSelected][colSelected] == Player.EMPTY))
					{
						board[rowSelected][colSelected] = currentPlayer; //set current cell to current board coordinates
						ContinueRound(currentPlayer, rowSelected, colSelected); //update round
						if (currentPlayer == Player.NOUGHT)
						{
							currentPlayer = Player.CROSS;
						}
						else
						{
							currentPlayer = Player.NOUGHT;
						}
					}
				}
				else			//current game is over
				{
					Start();	//Start the game
				}
				repaint();  	//call to redraw
			}
		});
 
		statusBar = new JLabel("  "); 	//Status bar (JLabel)
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15)); 		//set font of the status bar
		statusBar.setText("WELCOME CLICK ANYWHERE TO BEGIN!");
		statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));	//set border of the status bar
		authorBar = new JLabel("");
		authorBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20)); 		//set font of the status bar
		authorBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));	//set border of the status bar
		authorBar.setBackground(Color.GREEN);
		authorBar.setForeground(Color.BLACK);
		authorBar.setText("             LUCAS PENA");
		Container thisContainer = getContentPane();				//set thisContainer to getContainerPane
		thisContainer.setLayout(new BorderLayout());			//set layout to the new border layout
		thisContainer.add(statusBar, BorderLayout.PAGE_START);	//add statusbar, to the end of the border (after the canvas)
		thisContainer.add(canvas, BorderLayout.CENTER);			//add the canvas to the center of the container inside the border
		thisContainer.add(authorBar, BorderLayout.PAGE_END);	//add authorbar to the bottom of the page
 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//setting default to close board
		pack();		//pack the JFrame
		setTitle("Tic Tac Toe");	//set title bar of game
		setVisible(true);  			//show JFrame
 
		board = new Player[ROWS][COLUMNS];	//allocate the board 2d array
	}
 
	public void ContinueRound(Player thisPlayer, int rowSelected, int colSelected) 
	{
		if (WinnerBool(thisPlayer, rowSelected, colSelected))	//checking to see if game is over
		{
			if (thisPlayer == Player.NOUGHT)
			{
				currentState = GameState.NOUGHT_WON;
			}
			else
			{
				currentState = GameState.CROSS_WON;
			}
		} 
		else if (DrawBool())	//if draw boolean is true
		{
			currentState = GameState.DRAW;	//change current game state to DRAW
		}
		//if both false, then game state is still Playing
	}
	
	public void Start() 
	{
		for (int row = 0; row < ROWS; ++row) 
		{
			for (int col = 0; col < COLUMNS; ++col) 
			{
				board[row][col] = Player.EMPTY;	//set all cells to empty
			}
		}
		currentState = GameState.PLAYING;	//set the current game state to PLAYING
		currentPlayer = Player.CROSS;      	//set the first player to CROSS
	}
 
	//update the with the current row, column and player passed

 
	public boolean DrawBool() //return true if there are no more empty cells
	{
		for (int row = 0; row < ROWS; ++row)
		{
			for (int col = 0; col < COLUMNS; ++col)
			{
				if (board[row][col] == Player.EMPTY)
				{
					return false;	//for loop for 2d array to see if any empty, return false
				}
			}
		}
		return true;	//if all no cell is empty there is a draw return true
	}
 
	public boolean WinnerBool(Player thisPlayer, int rowSelected, int colSelected) //return true if there is three cells owned by the respective player
	{
		return	(board[0][colSelected] == thisPlayer     	//check if three in column given
				&& board[1][colSelected] == thisPlayer
				&& board[2][colSelected] == thisPlayer
				|| board[rowSelected][0] == thisPlayer		//check if three in row given
				&& board[rowSelected][1] == thisPlayer
				&& board[rowSelected][2] == thisPlayer
				|| rowSelected == colSelected           //check if three in dianogal from 0,0
				&& board[0][0] == thisPlayer
				&& board[1][1] == thisPlayer
				&& board[2][2] == thisPlayer
				|| rowSelected + colSelected == 2  		//check if three in dianogal from 2,0
				&& board[2][0] == thisPlayer
				&& board[1][1] == thisPlayer
				&& board[0][2] == thisPlayer);
	}
 

 
   //MAIN BELOW//
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TTT(); //run TTT
			}
		});
	}
}

//text based game below//
/*
 * package TicTacToe;

import java.util.*;


public class TicTacToe
{
	//Setting Global Constant
	public static final int COLUMNS = 3;
	public static final int ROWS = 3;
	//Enum used for efficiency in defining game state 
	public static enum GameState
	{
		PLAYING, NOUGHT_WON, CROSS_WON, DRAW
	}
	private static GameState currentState;	//creating a GameState called currentState
	//Enum used for efficiency in defining cell state
	public static enum Player
	{
		EMPTY, NOUGHT, CROSS
	}
	private static Player currentPlayer;	//creating a CellState called currentCell
	private static Player[][] board  = new Player[ROWS][COLUMNS]  ; 	//Creating a  2d array of CellStates called board
	public static int rowSelected;
	public static int colSelected;
	public static Scanner input = new Scanner(System.in); // the input Scanner
	
	public static void Start() 
	{
		for (int row = 0; row < ROWS; ++row) 
		{
			for (int col = 0; col < COLUMNS; ++col) 
			{
				board[row][col] = Player.EMPTY;	//set all cells to empty
			}
		}
		currentState = GameState.PLAYING;	//set the current game state to PLAYING
		currentPlayer = Player.CROSS;      	//set the first player to CROSS
	}
	
	public static boolean DrawBool() //return true if there are no more empty cells
	{
		for (int row = 0; row < ROWS; ++row)
		{
			for (int col = 0; col < COLUMNS; ++col)
			{
				if (board[row][col] == Player.EMPTY)
				{
					return false;	//for loop for 2d array to see if any empty, return false
				}
			}
		}
		return true;	//if all no cell is empty there is a draw return true
	}
	
	public static void ContinueRound(Player thisPlayer, int rowSelected, int colSelected) 
	{
		if (WinnerBool(thisPlayer, rowSelected, colSelected))	//checking to see if game is over
		{
			if (thisPlayer == Player.NOUGHT)
			{
				currentState = GameState.NOUGHT_WON;
			}
			else
			{
				currentState = GameState.CROSS_WON;
			}
		} 
		else if (DrawBool())	//if draw boolean is true
		{
			currentState = GameState.DRAW;	//change current game state to DRAW
		}
		//if both false, then game state is still Playing
	}
	
	public static void turn(Player thisPlayer)
	{
		int validsbool = 0;
		while (validsbool == 0)
		{
			int rowHolder;
			int columnHolder;
			if (thisPlayer == Player.NOUGHT)
			{
				 System.out.print("It is nought's turn!\nEnter a row:");
				 rowHolder = input.nextInt();
				 System.out.print("and column:");
				 columnHolder = input.nextInt();
			}
			else
			{
				 System.out.print("It is cross's turn!\nEnter a row:");
				 rowHolder = input.nextInt();
				 System.out.print("and column:");
				 columnHolder = input.nextInt();
			}
			rowHolder = rowHolder-1;
			columnHolder = columnHolder-1;
			
			if ((rowHolder >= 0) && (rowHolder < ROWS) && (columnHolder >= 0) && (columnHolder < COLUMNS) && (board[rowHolder][columnHolder] == Player.EMPTY))
			{
				 System.out.print("Valid Coordinates!\n\n");
				 board[rowHolder][columnHolder] = thisPlayer;
				 validsbool = 1;
			}
			else
			{
			 System.out.print("Invalid Coordinates!\nTry Again");
			}
			
		}
	}
	
	public static void displayBoard()
	{
		for (int r = 0; r < ROWS; ++r)
		{
			for (int c = 0; c < COLUMNS; ++c)
			{
				Player coordinate =board[r][c];
				if (coordinate == Player.EMPTY)
					System.out.print(" _ ");
				if (coordinate == Player.NOUGHT)
					System.out.print(" O ");
				if (coordinate == Player.CROSS)
					System.out.print(" X ");
				
				if (c != 2)
				{
					System.out.print("|");
				}

			}
				System.out.print("\n");
				if (r != 2)
				{
					System.out.print("~~~~~~~~~~");
				}
				System.out.print("\n");
		}
	}
	
	
	public static boolean WinnerBool(Player thisPlayer, int rowSelected, int colSelected) //return true if there is three cells owned by the respective player
	{
		return	(board[0][colSelected] == thisPlayer     	//check if three in column given
				&& board[1][colSelected] == thisPlayer
				&& board[2][colSelected] == thisPlayer
				|| board[rowSelected][0] == thisPlayer		//check if three in row given
				&& board[rowSelected][1] == thisPlayer
				&& board[rowSelected][2] == thisPlayer
				|| rowSelected == colSelected           //check if three in dianogal from 0,0
				&& board[0][0] == thisPlayer
				&& board[1][1] == thisPlayer
				&& board[2][2] == thisPlayer
				|| rowSelected + colSelected == 2  		//check if three in dianogal from 2,0
				&& board[2][0] == thisPlayer
				&& board[1][1] == thisPlayer
				&& board[0][2] == thisPlayer);
	}
	
	
	public static void main(String[] args)
	 {
		 Start();
		 while (currentState == GameState.PLAYING)
		 {
			displayBoard();
			turn(currentPlayer);
			ContinueRound(currentPlayer, rowSelected, colSelected);
			
			if (currentState == GameState.DRAW)
			{
				System.out.println("..Draw!");
			}
			else if (currentState == GameState.CROSS_WON)
			{
				System.out.println("'X' Wins!");
			}
			else if (currentState == GameState.NOUGHT_WON)
			{
				System.out.println("'O' Wins!");
			}
			else if (currentState == GameState.DRAW)
			{
				System.out.println("It's a Draw! Bye!");
			}
			
			if (currentPlayer == Player.NOUGHT)
			{
				currentPlayer = Player.CROSS;
			}
			else
			{
				currentPlayer = Player.NOUGHT;
			}
		 }

	 }
}
*/
