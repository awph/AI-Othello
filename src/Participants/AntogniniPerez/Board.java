package Participants.AntogniniPerez;

import java.util.Arrays;

public class Board {

	//DO NOT TOUCH THOSE 3 VARIABLES
	public static final int Blue = 1;
	public static final int Red = -1;
	public static final int Empty = 0;
	
	public static final int DUMMY_VALUE = -1;// To know where to stop in the possible move array
	
	private static final int BOARD_SIZE = 8;
	
	private int[][] board;//Row major
	
	public void debug__Board()
	{
		for(int[] b : board)
		{
			for(int bb : b)
				System.out.print(bb + "\t");
			System.out.println();
		}
	}
	
	public void debug__addPiece(int row, int col, int currentPlayer)
	{
		System.out.println("Player : " + currentPlayer);
		System.out.println("Row : " + row + " Col : " + col);
		System.out.println(checkHorizontallyLeft2Right(row, col, currentPlayer)); 
		System.out.println(checkHorizontallyRight2Left(row, col, currentPlayer)); 
		System.out.println(checkVerticallyTop2Bottom(row, col, currentPlayer)); 
		System.out.println(checkVerticallyBottom2Top(row, col, currentPlayer));
		System.out.println(checkDiagonallyBottomLeft2TopRight(row, col, currentPlayer));
		System.out.println(checkDiagonallyBottomRight2TopLeft(row, col, currentPlayer));
		System.out.println(checkDiagonallyTopRight2BottomLeft(row, col, currentPlayer));
		System.out.println(checkDiagonallyTopLeft2BottomRight(row, col, currentPlayer));
		
		addPiece(row, col, currentPlayer);
	}
	
	//Debug a state
	public Board(boolean debug)
	{
		board = new int[][]
				{
				{1, 1, 1, 1, 1, 0, 0, 0},
				{1, 1, 1, 1, 1, 1, 0, 0},
				{1, -1, 1, -1, 0, 0, 0, 0},
				{1, -1, 0, -1, -1, 0, 0, 0},
				{1, -1, 1, -1, -1, 0, 0, 0},
				{1, 1, 0, -1, 0, -1, 0, 0},
				{1, 0, -1, -1, 0, 0, 0, 0},
				{0, 0, 0, -1, 0, 0, 0, 0}
				};
		debug__Board();
		int[] possiblesMoves = new int[121];
		getAllPossibleMove(possiblesMoves, -1);
		for(int i : possiblesMoves)
			System.out.print(i + ", ");
	}
	
	public Board()
	{
		board = new int[BOARD_SIZE][BOARD_SIZE];
		init();
	}
	
	public Board(Board board)
	{
		//Don't need the init
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				this.board[i][j] = board.board[i][j];
	}
	
	public void init()
	{
		for(int i = 0; i < 8; ++i)
			Arrays.fill(board[i], Empty);
		
		board[BOARD_SIZE/2-1][BOARD_SIZE/2-1] = Blue;
		board[BOARD_SIZE/2][BOARD_SIZE/2] = Blue;
		board[BOARD_SIZE/2-1][BOARD_SIZE/2] = Red;
		board[BOARD_SIZE/2][BOARD_SIZE/2-1] = Red;
	}
	
	//We suppose that we always receive a valid move via the method getAllPossibleMove
	public void addPiece(int row, int col, int currentPlayer)
	{
		board[row][col] = currentPlayer;
		int oppositePlayer = -currentPlayer;
		
		if(checkHorizontallyLeft2Right(row, col, currentPlayer))
			for(int j = col + 1; j < BOARD_SIZE && board[row][j] == oppositePlayer; ++j)
				board[row][j] = currentPlayer;
		
		if(checkHorizontallyRight2Left(row, col, currentPlayer))
			for(int j = col - 1; j >= 0 && board[row][j] == oppositePlayer; --j)
				board[row][j] = currentPlayer;
		
		if(checkVerticallyTop2Bottom(row, col, currentPlayer))
			for(int i = row + 1; i < BOARD_SIZE && board[i][col] == oppositePlayer; ++i)
				board[i][col] = currentPlayer;
		
		if(checkVerticallyBottom2Top(row, col, currentPlayer))
			for(int i = row - 1; i >= 0 && board[i][col] == oppositePlayer; --i)
				board[i][col] = currentPlayer;
		
		if(checkDiagonallyBottomLeft2TopRight(row, col, currentPlayer))
			for(int i = row - 1, j = col + 1; i >= 0 && j < BOARD_SIZE && board[i][j] == oppositePlayer; --i, ++j)
				board[i][j] = currentPlayer;
		
		if(checkDiagonallyBottomRight2TopLeft(row, col, currentPlayer))
			for(int i = row - 1, j = col - 1; i >= 0 && j >= 0 && board[i][j] == oppositePlayer; --i, --j)
				board[i][j] = currentPlayer;
		
		if(checkDiagonallyTopRight2BottomLeft(row, col, currentPlayer))
			for(int i = row + 1, j = col - 1; i < BOARD_SIZE && j >= 0 && board[i][j] == oppositePlayer; ++i, --j)
				board[i][j] = currentPlayer;
		
		if(checkDiagonallyTopLeft2BottomRight(row, col, currentPlayer))
			for(int i = row + 1, j = col + 1; i < BOARD_SIZE && j < BOARD_SIZE && board[i][j] == oppositePlayer; ++i, ++j)
				board[i][j] = currentPlayer;			
	}
	
	public void getAllPossibleMove(int[] outputArray, int currentPlayer)
	{
		int indexOutputArray = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == Empty && isLegit(i, j, currentPlayer))
				{
					outputArray[indexOutputArray++] = i;
					outputArray[indexOutputArray++] = j;
				}
		outputArray[indexOutputArray] = DUMMY_VALUE;
	}
	
	public boolean isTheGameEnded()
	{
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == Empty && (isLegit(i, j, Blue) || isLegit(i, j, Red)))
					return false;
		return true;
	}
	
	public int getFinalPoints(int currentPlayer)
	{
		int current = 0, opposite = 0, empty = 0;
		int oppositePlayer = -currentPlayer;
		
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer)
					++current;
				else if(board[i][j] == oppositePlayer)
					++opposite;
				else
					++empty;
		
		if(current > opposite)
			return current+empty;
		else if(current < opposite)
			return opposite+empty;
		else
			return current; //We do nothing special if there is the same number of piece blue & red and some empty cases
			
	}
	
	private boolean isLegit(int row, int col, int currentPlayer)
	{
		return  checkHorizontallyLeft2Right(row, col, currentPlayer) || 
				checkHorizontallyRight2Left(row, col, currentPlayer) || 
				
				checkVerticallyTop2Bottom(row, col, currentPlayer) || 
				checkVerticallyBottom2Top(row, col, currentPlayer) ||
				
				checkDiagonallyBottomLeft2TopRight(row, col, currentPlayer) ||
				checkDiagonallyBottomRight2TopLeft(row, col, currentPlayer) ||
				
				checkDiagonallyTopRight2BottomLeft(row, col, currentPlayer) ||
				checkDiagonallyTopLeft2BottomRight(row, col, currentPlayer);
	}
	
	private boolean checkHorizontallyLeft2Right(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = col + 2; j < BOARD_SIZE; ++j)
			if(board[row][j-1] == oppositePlayer && board[row][j] == currentPlayer)
				return true;
			else if(board[row][j-1] != oppositePlayer || board[row][j] != oppositePlayer)
				return false;
		return false;
	}
	
	private boolean checkHorizontallyRight2Left(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = col - 2; j >= 0; --j)
			if(board[row][j+1] == oppositePlayer && board[row][j] == currentPlayer)
				return true;
			else if(board[row][j+1] != oppositePlayer || board[row][j] != oppositePlayer)
				return false;
		return false;		
	}
	
	private boolean checkVerticallyTop2Bottom(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = row + 2; i < BOARD_SIZE; ++i)
			if(board[i-1][col] == oppositePlayer && board[i][col] == currentPlayer)
				return true;
			else if(board[i-1][col] != oppositePlayer || board[i][col] != oppositePlayer)
				return false;
		return false;
	}
	
	private boolean checkVerticallyBottom2Top(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = row - 2; i >= 0; --i)
			if(board[i+1][col] == oppositePlayer && board[i][col] == currentPlayer)
				return true;
			else if(board[i+1][col] != oppositePlayer || board[i][col] != oppositePlayer)
				return false;
		return false;	
	}
	
	private boolean checkDiagonallyTopLeft2BottomRight(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = row + 2, j = col + 2; i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
			if(board[i-1][j-1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i-1][j-1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	private boolean checkDiagonallyBottomRight2TopLeft(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = row - 2, j = col - 2; i >= 0 && j >= 0; --i, --j)
			if(board[i+1][j+1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i+1][j+1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	private boolean checkDiagonallyTopRight2BottomLeft(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = row + 2, j = col - 2; i < BOARD_SIZE && j >= 0; ++i, --j)
			if(board[i-1][j+1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i-1][j+1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	private boolean checkDiagonallyBottomLeft2TopRight(int row, int col, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = row - 2, j = col + 2; i >= 0 && j < BOARD_SIZE; --i, ++j)
			if(board[i+1][j-1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i+1][j-1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
}
