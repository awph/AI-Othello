package Participants.AntogniniPerez;

import java.util.Arrays;

public class Board {

	//DO NOT TOUCH THOSE 3 VARIABLES
	public static final int Blue = 1;
	public static final int Red = -1;
	public static final int Empty = 0;
	
	// To know where to stop in the possible move array
	public static final int DUMMY_VALUE = -1;
	
	private static final int BOARD_SIZE = 8;
	
	//MUST HAVE THE SIZE OF BOARD_SIZExBOARD_SIZE, http://www.samsoft.org.uk/reversi/diagram7.gif
	private static final int[][] POSITION_SCORE = 
		{
		{99,   -8,    8,    6,    6,    8,   -8,    99},
		{-8,   24,   -4,  -3,   -3,   -4,  24,    -8},    
		{ 8,     6,    7,    4,    4,    7,    6,    8},    
		{ 6,    -3,    4,    0,    0,    4,   -3,    6},    
		{ 6,    -3,    4,    0,    0,    4,   -3,    6},    
		{ 8,     6,    7,    4,    4,    7,    6,    8},    
		{-8,   24,   -4,  -3,   -3,   -4,  24,    -8},    
		{99,   -8,    8,    6,    6,    8,   -8,    99}
		};
	
	
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
		
		if(checkHorizontallyLeft2Right(row, col, currentPlayer))
			actionHorizontallyLeft2Right(row, col, currentPlayer);
		
		if(checkHorizontallyRight2Left(row, col, currentPlayer))
			actionHorizontallyRight2Left(row, col, currentPlayer);
		
		if(checkVerticallyTop2Bottom(row, col, currentPlayer))
			actionVerticallyTop2Bottom(row, col, currentPlayer);
		
		if(checkVerticallyBottom2Top(row, col, currentPlayer))
			actionVerticallyBottom2Top(row, col, currentPlayer);
		
		if(checkDiagonallyBottomLeft2TopRight(row, col, currentPlayer))
			actionDiagonallyBottomLeft2TopRight(row, col, currentPlayer);
		
		if(checkDiagonallyBottomRight2TopLeft(row, col, currentPlayer))
			actionDiagonallyBottomRight2TopLeft(row, col, currentPlayer);
		
		if(checkDiagonallyTopRight2BottomLeft(row, col, currentPlayer))
			actionDiagonallyTopRight2BottomLeft(row, col, currentPlayer);
		
		if(checkDiagonallyTopLeft2BottomRight(row, col, currentPlayer))
			actionDiagonallyTopLeft2BottomRight(row, col, currentPlayer);
	}
	
	/*
	 *========================================================*
	 *					COMPUTATIONAL PART
	 *========================================================*
	 */
	
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
	
	/**
     * A Piece is considered as irreversible if and only if theses 3 sates are
     * correct: - The left or right side are filled by the same piece color -
     * The top or bottom side are filled by the same piece color - 2 diagonally
     * neighbouring size are filled by the same piece color
     */
	private boolean isIrreversiblePiece(int row, int col, int currentPlayer)
	{
		
		// State 1
        boolean isStateOneValid = true;
        {
                // Left side
                isStateOneValid = checkIrreversiblePieceHorizontallyLeft(row, col, currentPlayer);

                // Right side
                if (!isStateOneValid)
                        isStateOneValid = checkIrreversiblePieceHorizontallyRight(row, col, currentPlayer);

                if (!isStateOneValid)
                        return false;
        }

        // State 2
        boolean isStateTwoValid = true;
        {
                // Top side
                isStateTwoValid = checkIrreversibleVerticallyTop(row, col, currentPlayer);

                // Bottom side
                if (!isStateTwoValid)
                        isStateTwoValid = checkIrreversiblePieceVerticallyBottom(row, col, currentPlayer);

                if (!isStateTwoValid)
                        return false;
        }

        // State 3
        boolean isStateThreeValid = true;
        {
                // Left diagonals
                if (checkIrreversiblePieceDiagonallyTopLeft(row, col, currentPlayer))
                        isStateThreeValid = checkIrreversiblePieceDiagonallyBottomLeft(row, col, currentPlayer);

                if (isStateThreeValid)
                        return true;

                // Top diagonals
                isStateThreeValid = true;
                if (checkIrreversiblePieceDiagonallyTopLeft(row, col, currentPlayer))
                        isStateThreeValid = checkIrreversiblePieceDiagonallyTopRight(row, col, currentPlayer);

                if (isStateThreeValid)
                        return true;

                // Right diagonals
                isStateThreeValid = true;
                if (checkIrreversiblePieceDiagonallyTopRight(row, col, currentPlayer))
                        isStateThreeValid = checkIrreversiblePieceDiagonallyBottomRight(row, col, currentPlayer);

                if (isStateThreeValid)
                        return true;

                // Bottom diagonals
                isStateThreeValid = true;
                if (checkIrreversiblePieceDiagonallyBottomRight(row, col, currentPlayer))
                        isStateThreeValid = checkIrreversiblePieceDiagonallyBottomLeft(row, col, currentPlayer);

                return false;// NOT(State1 AND State2 AND State3)
        }

	}
	
	/*
	 *========================================================*
	 *						SCORE PART
	 *========================================================*
	 */
	
	/*
	 * Return the number of irreverisble pieces
	 */
	public int getNbIrreversiblePiece(int currentPlayer)
	{
		int out = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer && isIrreversiblePiece(i, j, currentPlayer))
					++out;
		return out;
		
	}
	/*
	 * Return the number of possibilites that has the currentPlayer to play with this state of the game
	 */
	public int getMobilityScore(int currentPlayer)
	{
		int[] possibleMove = new int[121];
		getAllPossibleMove(possibleMove, currentPlayer);
		
		int i;
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		return i/2;//(i-1+1)/2
	}
	
	/*
	 * Return a score related with the position of the currentPlayer's pieces with the position matrix
	 */
	public int getPositionScore(int currentPlayer)
	{
		int out = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer)
					out += POSITION_SCORE[i][j];
		return out;
	}
	
	/*
	 * Return a score that count the number of currentPlayer's piece, with the empty cases if he wins
	 */
	public int getScoreAtEndOfGame(int currentPlayer)
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
	
	/*
	 *========================================================*
	 *						CHECK PART
	 *========================================================*
	 */
	
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

	
	private boolean checkIrreversiblePieceHorizontallyRight(int row, int col, int currentPlayer)
	{
		for(int j = col + 1; j < BOARD_SIZE; ++j)
			if(board[row][j] != currentPlayer)
				return false;
		return true;
	}
	
	private boolean checkIrreversiblePieceHorizontallyLeft(int row, int col, int currentPlayer)
	{
		for(int j = col - 1; j >= 0; --j)
			if(board[row][j] != currentPlayer)
				return false;
		return true;
	}
	
	private boolean checkIrreversiblePieceVerticallyBottom(int row, int col, int currentPlayer)
	{
		for(int i = row + 1; i < BOARD_SIZE; ++i)
			if(board[i][col] != currentPlayer)
				return false;
		return true;
	}
	
	private boolean checkIrreversibleVerticallyTop(int row, int col, int currentPlayer)
	{
		for(int i = row - 1; i >= 0; --i)
			if(board[i][col] != currentPlayer)
				return false;
		return true;
	}
	
	private boolean checkIrreversiblePieceDiagonallyBottomRight(int row, int col, int currentPlayer) 
	{
		for(int i = row + 1, j = col + 1; i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	private boolean checkIrreversiblePieceDiagonallyTopLeft(int row, int col, int currentPlayer) 
	{
        for(int i = row - 1, j = col - 1; i >= 0 && j >= 0; --i, --j)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	private boolean checkIrreversiblePieceDiagonallyBottomLeft(int row, int col, int currentPlayer) 
	{
		for(int i = row + 1, j = col - 1; i < BOARD_SIZE && j >= 0; ++i, --j)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	private boolean checkIrreversiblePieceDiagonallyTopRight(int row, int col, int currentPlayer) 
	{
        for(int i = row - 1, j = col + 1; i >= 0 && j < BOARD_SIZE; --i, ++j)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	/*
	 *========================================================*
	 *						ACTION PART
	 *========================================================*
	 */
	
	private void actionHorizontallyLeft2Right(int row, int col, int currentPlayer)
	{
		for(int j = col + 1; j < BOARD_SIZE && board[row][j] == -currentPlayer; ++j)
			board[row][j] = currentPlayer;
	}
	
	private void actionHorizontallyRight2Left(int row, int col, int currentPlayer)
	{
		for(int j = col - 1; j >= 0 && board[row][j] == -currentPlayer; --j)
			board[row][j] = currentPlayer;
	}
	
	private void actionVerticallyTop2Bottom(int row, int col, int currentPlayer)
	{
		for(int i = row + 1; i < BOARD_SIZE && board[i][col] == -currentPlayer; ++i)
			board[i][col] = currentPlayer;
	}
	
	private void actionVerticallyBottom2Top(int row, int col, int currentPlayer)
	{
		for(int i = row - 1; i >= 0 && board[i][col] == -currentPlayer; --i)
			board[i][col] = currentPlayer;
	}
	
	private void actionDiagonallyTopLeft2BottomRight(int row, int col, int currentPlayer)
	{
		for(int i = row + 1, j = col + 1; i < BOARD_SIZE && j < BOARD_SIZE && board[i][j] == -currentPlayer; ++i, ++j)
			board[i][j] = currentPlayer;	
	}
	
	private void actionDiagonallyBottomRight2TopLeft(int row, int col, int currentPlayer)
	{
		for(int i = row - 1, j = col - 1; i >= 0 && j >= 0 && board[i][j] == -currentPlayer; --i, --j)
			board[i][j] = currentPlayer;
	}
	
	private void actionDiagonallyTopRight2BottomLeft(int row, int col, int currentPlayer)
	{
		for(int i = row + 1, j = col - 1; i < BOARD_SIZE && j >= 0 && board[i][j] == -currentPlayer; ++i, --j)
			board[i][j] = currentPlayer;
	}
	
	private void actionDiagonallyBottomLeft2TopRight(int row, int col, int currentPlayer)
	{
		for(int i = row - 1, j = col + 1; i >= 0 && j < BOARD_SIZE && board[i][j] == -currentPlayer; --i, ++j)
			board[i][j] = currentPlayer;
	}
}
