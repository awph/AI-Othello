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
		{-8,   -24,   -4,  -3,   -3,   -4,  -24,    -8},    
		{ 8,     -4,    7,    4,    4,    7,    -4,    8},    
		{ 6,    -3,    4,    0,    0,    4,   -3,    6},    
		{ 6,    -3,    4,    0,    0,    4,   -3,    6},    
		{ 8,     -4,    7,    4,    4,    7,    -4,    8},    
		{-8,   -24,   -4,  -3,   -3,   -4,  -24,    -8},    
		{99,   -8,    8,    6,    6,    8,   -8,    99}
		};
	private final static double MAX_SCORE = 4.0*142.0;
	
	private int[][] board;//Row major
	private int oldPlayer;	
	private boolean hasAPlayerPassed;
	private int ithMove;
	
	public Board()
	{
		board = new int[BOARD_SIZE][BOARD_SIZE];
		init();
	}
	
	public Board(Board board)
	{
		this();
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
		
		oldPlayer = Blue;
		hasAPlayerPassed = false;
		ithMove = 0;
	}
	
	//We suppose that we always receive a valid move via the method getAllPossibleMove
	public void addPiece(int row, int col, int currentPlayer)
	{
		++ithMove;
		if(oldPlayer == currentPlayer)
			hasAPlayerPassed = !hasAPlayerPassed;
		
		oldPlayer = currentPlayer;
		
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
	
	public int getIthMove()
	{
		return ithMove;
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
	
	public double getParity(int currentPlayer)
	{
		return (hasAPlayerPassed && currentPlayer == Blue || !hasAPlayerPassed && currentPlayer == Red) ? 1.0 : 0.0;
	}
	
	public double getPieceDifference(int currentPlayer)
	{
		int his = 0, mine = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer)
					++mine;
				else if(board[i][j] == -currentPlayer)
					++his;
		
		return (mine-his)/64.0;
	}
	
	public double getCornerOccupacy(int currentPlayer)
	{
		int mine = 0, his = 0;
		
		for(int i = 0; i <= 1; ++i)
			for(int j = 0; j <= 1; ++j)
				if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == currentPlayer)
					++mine;
				else if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == -currentPlayer)
					++his;
		
		return (mine-his)/4.0;
	}
	
	public double getCornerCloseness(int currentPlayer)
	{
		int mine = 0, his = 0;
		
		for(int i = 0; i <= 1; ++i)
			for(int j = 0; j <= 1; ++j)
				if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == Empty)
				{
					/*
					 * X C
					 * A B
					 */
					
					int ii = (-2)*i+1;
					if(ii == -1)
						ii += BOARD_SIZE-1;
					
					int jj = (-2)*j+1;
					if(jj == -1)
						jj += BOARD_SIZE-1;
					
					// A Part for the 4 corners
					if(board[ii][0] == currentPlayer)
						++mine;
					else if(board[ii][0] == -currentPlayer)
						++his;
					
					// C Part for the 4 corners
					if(board[0][jj] == currentPlayer)
						++mine;
					else if(board[0][jj] == -currentPlayer)
						++his;
					
					// B Part for the 4 corners
					if(board[ii][jj] == currentPlayer)
						++mine;
					else if(board[ii][jj] == -currentPlayer)
						++his;
				}
		return (mine-his)/12.0;
	}
	
	public double getBoardPiece(int currentPlayer)
	{
		int mine=0;
		int his = 0;
		
		for(int j = 2; j <= 5; ++j)
			if(board[0][j] == currentPlayer)
				++mine;
			else if(board[0][j] == -currentPlayer);
				++his;
		
		for(int j = 2; j <= 5; ++j)
			if(board[BOARD_SIZE-1][j] == currentPlayer)
				++mine;
			else if(board[BOARD_SIZE-1][j] == -currentPlayer);
				++his;
				
		for(int i = 2; i <= 5; ++i)
			if(board[i][0] == currentPlayer)
				++mine;
			else if(board[i][0] == -currentPlayer);
				++his;
		
		for(int i = 2; i <= 5; ++i)
			if(board[i][BOARD_SIZE-1] == currentPlayer)
				++mine;
			else if(board[i][BOARD_SIZE-1] == -currentPlayer);
				++his;
				
		return (mine-his)/16.0;
	}
	public double getMobilityScore(int currentPlayer)
	{
		int[] possibleMove = new int[121];
		getAllPossibleMove(possibleMove, Blue);
		
		int i;
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		int mine = i/2;//(i-1+1)/2
		
		getAllPossibleMove(possibleMove, Red);
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		int his = i/2;//(i-1+1)/2
		
		return (mine-his)/20.0;
	}
	
	public double getFrontierDiscs(int currentPlayer)
	{
		return 0;
	}
	
	public double getDiscSquares(int currentPlayer)
	{		
		double out = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] != Empty)
					out += POSITION_SCORE[i][j]*board[i][j];
		
		return currentPlayer*out/MAX_SCORE;
	}
	
	public double getNbIrreversiblePiece(int currentPlayer)
	{
		int out = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer && isIrreversiblePiece(i, j, currentPlayer))
					++out;
		return out/64.0;
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
