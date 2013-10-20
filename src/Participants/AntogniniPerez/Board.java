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
		{ 8,     6,    7,    4,    4,    7,    6,    8},    
		{ 6,    -3,    4,    0,    0,    4,   -3,    6},    
		{ 6,    -3,    4,    0,    0,    4,   -3,    6},    
		{ 8,     6,    7,    4,    4,    7,    6,    8},    
		{-8,   -24,   -4,  -3,   -3,   -4,  -24,    -8},    
		{99,   -8,    8,    6,    6,    8,   -8,    99}
		};
	
	private int[][] board;//Row major
	private int oldPlayer;	
	private int maxPositionScore;
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
		maxPositionScore = 0;

		for(int i = 0; i < BOARD_SIZE/2; ++i)
			for(int j = 0; j < BOARD_SIZE/2; ++j)
				maxPositionScore += POSITION_SCORE[i][j];
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
	
	public boolean getParity(int currentPlayer)
	{
		return (hasAPlayerPassed && currentPlayer == Blue || !hasAPlayerPassed && currentPlayer == Red);
	}
	
	public double getPieceDifference(int currentPlayer)
	{
		int red = 0, blue = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == Blue)
					++blue;
				else if(board[i][j] == Red)
					++red;
		
		if(red == blue)
			return 0;
		else if(blue > red)
			return currentPlayer*(double)(blue)/(blue+red);
		else
			return -currentPlayer*(double)(red)/(blue+red);
	}
	
	public double getCornerOccupacy(int currentPlayer)
	{
		int blue = 0, red = 0;
		
		for(int i = 0; i <= 1; ++i)
			for(int j = 0; j <= 1; ++j)
				if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == Blue)
					++blue;
				else if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == Red)
					++red;
		
		return currentPlayer*0.25*(blue-red);
	}
	
	public double getCornerCloseness(int currentPlayer)
	{
		int blue = 0, red = 0;
		
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
					if(board[ii][0] == Blue)
						++blue;
					else if(board[ii][0] == Red)
						++red;
					
					// C Part for the 4 corners
					if(board[0][jj] == Blue)
						++blue;
					else if(board[0][jj] == Red)
						++red;
					
					// B Part for the 4 corners
					if(board[ii][jj] == Blue)
						++blue;
					else if(board[ii][jj] == Red)
						++red;
				}
		return currentPlayer*0.125*(red-blue);
	}
	
	public double getMobilityScore(int currentPlayer)
	{
		int[] possibleMove = new int[121];
		getAllPossibleMove(possibleMove, Blue);
		
		int i;
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		int blue = i/2;//(i-1+1)/2
		
		getAllPossibleMove(possibleMove, Red);
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		int red = i/2;//(i-1+1)/2
		
		if(red == blue || blue == 0 || red == 0)
			return 0.0;
		else if(blue > red)
			return currentPlayer*(double)(blue)/(blue+red);
		else
			return -currentPlayer*(double)(red)/(blue+red);
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
		
		return currentPlayer*out/maxPositionScore;
	}
	
	public double getIrreversiblePiece(int currentPlayer)
	{
		int blue = getNbIrreversiblePiece(Blue);
		int red = getNbIrreversiblePiece(Red);
		
		if(blue > red)
			return currentPlayer*(double)(blue)/(blue+red);
		else if(red < blue)
			return -currentPlayer*(double)(red)/(blue+red);
		else 
			return 0.0;
	}
	
	private int getNbIrreversiblePiece(int currentPlayer)
	{
		int out = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer && isIrreversiblePiece(i, j, currentPlayer))
					++out;
		return out;
		
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
