package TicTacToe;

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
