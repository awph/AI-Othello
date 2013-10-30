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
	public static final int END_BEGIN_GAME = 13;
	
	private int[][] board;//Col major
	private int[][] positionMatrixScore;
	
	private boolean hasAPlayerPassed;
	private int oldPlayer;	
	private int ithMove;
	
	//Temp variable to avoid a creation of a pair object
	private int tempMine;
	private int tempHis;
	
	public Board()
	{
		board = new int[BOARD_SIZE][BOARD_SIZE];
		
		/*It couldn't be static because it's modified during the game when a player has a corner,
		we modifiy the corner closeness sign*/
		positionMatrixScore = new int[][]
				{
				{500, -150, 30, 10, 10, 30, -150, 500},
				{-150, -250, 0, 0, 0, 0, -250, -150},
				{30, 0, 1, 2, 2, 1, 0, 30},
				{10, 0, 2, 16, 16, 2, 0, 10},
				{10, 0, 2, 16, 16, 2, 0, 10},
				{30, 0, 1, 2, 2, 1, 0, 30},
				{-150, -250, 0, 0, 0, 0, -250, -150},
				{500, -150, 30, 10, 10, 30, -150, 500}
				};
		
		init();
	}
	
	private void init()
	{
		oldPlayer = Blue;
		hasAPlayerPassed = false;
		ithMove = 4;
		tempMine = 0;
		tempHis = 0;
		
		for(int i = 0; i < 8; ++i)
			Arrays.fill(board[i], Empty);
		
		board[BOARD_SIZE/2-1][BOARD_SIZE/2-1] = Blue;
		board[BOARD_SIZE/2][BOARD_SIZE/2] = Blue;
		board[BOARD_SIZE/2-1][BOARD_SIZE/2] = Red;
		board[BOARD_SIZE/2][BOARD_SIZE/2-1] = Red;
	}
	
	/**
	 * 
	 * @param board : Board to copy
	 */
	public Board(Board board)
	{
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		positionMatrixScore = new int[BOARD_SIZE][BOARD_SIZE];
		ithMove = board.ithMove;
		
		for(int i = 0; i < BOARD_SIZE; ++i)
		{
			this.board[i] = Arrays.copyOf(board.board[i], BOARD_SIZE);
			this.positionMatrixScore[i] = Arrays.copyOf(board.positionMatrixScore[i], BOARD_SIZE);
		}
	}

	/**
	 * Add a piece in the current game. We suppose that we always receive a valid move via the method getAllPossibleMove
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	public void addPiece(int col, int row, int currentPlayer)
	{
		++ithMove;
		//Update the parity
		if(oldPlayer == currentPlayer)
			hasAPlayerPassed = !hasAPlayerPassed;
		
		oldPlayer = currentPlayer;	
		board[col][row] = currentPlayer;
		updateCornerCloseness(col, row);//Change the cornercloseness if the piece is played in a corner
		
		
		//Return all the pieces linked with the new move
		if(checkHorizontallyLeft2Right(col, row, currentPlayer))
			actionHorizontallyLeft2Right(col, row, currentPlayer);
		
		if(checkHorizontallyRight2Left(col, row, currentPlayer))
			actionHorizontallyRight2Left(col, row, currentPlayer);
		
		if(checkVerticallyTop2Bottom(col, row, currentPlayer))
			actionVerticallyTop2Bottom(col, row, currentPlayer);
		
		if(checkVerticallyBottom2Top(col, row, currentPlayer))
			actionVerticallyBottom2Top(col, row, currentPlayer);
		
		if(checkDiagonallyBottomLeft2TopRight(col, row, currentPlayer))
			actionDiagonallyBottomLeft2TopRight(col, row, currentPlayer);
		
		if(checkDiagonallyBottomRight2TopLeft(col, row, currentPlayer))
			actionDiagonallyBottomRight2TopLeft(col, row, currentPlayer);
		
		if(checkDiagonallyTopRight2BottomLeft(col, row, currentPlayer))
			actionDiagonallyTopRight2BottomLeft(col, row, currentPlayer);
		
		if(checkDiagonallyTopLeft2BottomRight(col, row, currentPlayer))
			actionDiagonallyTopLeft2BottomRight(col, row, currentPlayer);
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
	
	/**
	 * Return all the possible move for a board
	 * @param outputArray : Array of size 121, it's unnecessary to have a bigger one. Store all the possible move inside. Even number : col Odd number : row. To know where to stop searching the move, check if the case contains DUMMY_VALUE
	 * @param currentPlayer The current player via RED or BLUE
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
	
	/**
	 * Return if the game is finished
	 * @return Game is finished
	 */
	public boolean isTheGameEnded()
	{
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == Empty && (isLegit(i, j, Blue) || isLegit(i, j, Red)))
					return false;
		return true;
	}
	
	/**
	 * Check if a move is legit
	 * @param col
	 * @param row
	 * @param currentPlayer : Current player with BLUE or RED
	 * @return if the move is legit
	 */
	private boolean isLegit(int col, int row, int currentPlayer)
	{
		return  checkHorizontallyLeft2Right(col, row, currentPlayer) || 
				checkHorizontallyRight2Left(col, row, currentPlayer) || 
				
				checkVerticallyTop2Bottom(col, row, currentPlayer) || 
				checkVerticallyBottom2Top(col, row, currentPlayer) ||
				
				checkDiagonallyBottomLeft2TopRight(col, row, currentPlayer) ||
				checkDiagonallyBottomRight2TopLeft(col, row, currentPlayer) ||
				
				checkDiagonallyTopRight2BottomLeft(col, row, currentPlayer) ||
				checkDiagonallyTopLeft2BottomRight(col, row, currentPlayer);
	}
	
	/**
	 * Update the corner closeness if the move is in a corner, because it becomes more interesting to have those zone with the corner
	 * @param col
	 * @param row
	 */
	private void updateCornerCloseness(int col, int row)
	{
		if(col == 0 && row == 0)
		{
			positionMatrixScore[0][1] *= -1;
			positionMatrixScore[1][0] *= -1;
			positionMatrixScore[1][1] *= -1;
		}
		else if(col == 0 && row == BOARD_SIZE-1)
		{
			positionMatrixScore[0][BOARD_SIZE-2] *= -1;
			positionMatrixScore[1][BOARD_SIZE-1] *= -1;
			positionMatrixScore[1][BOARD_SIZE-2] *= -1;
		}
		else if(col == BOARD_SIZE-1 && row == 0)
		{
			positionMatrixScore[BOARD_SIZE-1][1] *= -1;
			positionMatrixScore[BOARD_SIZE-2][0] *= -1;
			positionMatrixScore[BOARD_SIZE-2][1] *= -1;
		}
		else if(col == BOARD_SIZE-1 && row == BOARD_SIZE-1)
		{
			positionMatrixScore[BOARD_SIZE-1][BOARD_SIZE-2] *= -1;
			positionMatrixScore[BOARD_SIZE-2][BOARD_SIZE-1] *= -1;
			positionMatrixScore[BOARD_SIZE-2][BOARD_SIZE-2] *= -1;
		}
	}
	
	/**
     * A Piece is considered as irreversible if and only if theses 3 sates are
     * correct: 
     * 1) The left or right side are filled by the same piece color 
     * 2) The top or bottom side are filled by the same piece color 
     * 3) 2 diagonally neighbouring size are filled by the same piece color
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
	
	/**
	 * Score paritiy
	 * @param currentPlayer
	 * @return 1.0 if the currentPlayer is the last one, otherwise 0.0
	 */
	public double getParityScore(int currentPlayer)
	{
		return (hasAPlayerPassed && currentPlayer == Blue || !hasAPlayerPassed && currentPlayer == Red) ? 1.0 : 0.0;
	}
	
	/**
	 * Score stability
	 * @param currentPlayer
	 * @return a stability score for the currentPlayer
	 */
	public double getStabilityScore(int currentPlayer)
	{
		getCornerOccupacy(currentPlayer);
		double mineCorner = tempMine;
		double hisCorner = tempHis;
		
		getCornerCloseness(currentPlayer);
		double mineCornerCloseness = tempMine;
		double hisCornerCloseness = tempHis;
		
		getBordPiece(currentPlayer);
		double mineBord = tempMine;
		double hisBord = tempHis;
		
		getNbIrreversiblePiece(currentPlayer);
		double mineIrreversiblePiece = tempMine - mineCorner;
		double hisIrreversiblePiece = tempHis - hisCorner;
		
		getPieceDifference(currentPlayer);
		double minePiece = tempMine;
		double hisPiece = tempHis;
		
		//Avoid division by 0
		if(hisPiece == 0)
			++hisPiece;
		if(hisCorner == 0)
			++hisCorner;
		if(hisBord == 0)
			++hisBord;
		if(hisCornerCloseness == 0)
			++hisCornerCloseness;
		if(hisIrreversiblePiece == 0)
			++hisIrreversiblePiece;

		return  3.0 * minePiece/hisPiece +
				10.0 * mineCorner/hisCorner +
				10.0 * mineIrreversiblePiece/hisIrreversiblePiece +
				8.0 * mineBord/hisBord +
				5.0 * mineCornerCloseness/hisCornerCloseness;
	}
	
	/**
	 * Score Mobility
	 * @param currentPlayer
	 * @return a mobility score for current player
	 */
	public double getMobilityScore(int currentPlayer)
	{
		getMobility(currentPlayer);
		double mineMobility = tempMine;
		double hisMobility = tempHis;
		
		getFrontierDiscs(currentPlayer);
		double mineFrontierDiscs = tempMine;
		double hisFrontierDiscs = tempHis;
		
		//Avoid division by 0 and change one factor
		if(mineFrontierDiscs == 0)
			++mineFrontierDiscs;
		if(hisMobility == 0)
		{
			++hisMobility;
			mineMobility *= 3.0;
		}
		
		return 3.0*hisFrontierDiscs/mineFrontierDiscs + 8.0*mineMobility/hisMobility;
	}
	
	/**
	 * Score position
	 * @param currentPlayer
	 * @return a position score for current player
	 */
	public double getPlaceScore(int currentPlayer)
	{
		int out = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] != Empty)
				{
					if(board[i][j] == currentPlayer)
						out += positionMatrixScore[i][j];
				}
		
		return out;
	}
	
	/**
	 * Piece difference
	 * @param currentPlayer
	 * @return the difference of pieces for the current player. > 0 the current player has more
	 */
	public double getPieceDifference(int currentPlayer)
	{
		int his = 0, mine = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer)
					++mine;
				else if(board[i][j] == -currentPlayer)
					++his;
		
		tempMine = mine;
		tempHis = his;
		
		return mine - his;
	}
	
	/**
	 * Count the number of corner for both players
	 * @param currentPlayer
	 */
	private void getCornerOccupacy(int currentPlayer)
	{
		int mine = 0, his = 0;
		
		for(int i = 0; i <= 1; ++i)
			for(int j = 0; j <= 1; ++j)
				if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == currentPlayer)
					++mine;
				else if(board[i*(BOARD_SIZE-1)][j*(BOARD_SIZE-1)] == -currentPlayer)
					++his;
		
		tempMine = mine;
		tempHis = his;
	}
	
	/**
	 * Count the corner closeness for both players
	 * @param currentPlayer
	 */
	private void getCornerCloseness(int currentPlayer)
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
					
					int i1 = i*(BOARD_SIZE-1);
					int j1 = j*(BOARD_SIZE-1);
					
					int i2 = (-2)*i+1;
					int j2 = (-2)*j+1;

					// C Part for the 4 corners
					if(board[i1+i2][j1] == currentPlayer)
						++mine;
					else if(board[i1+i2][j1] == -currentPlayer)
						++his;
					
					// A Part for the 4 corners
					if(board[i1][j1+j2] == currentPlayer)
						++mine;
					else if(board[i1][j1+j2] == -currentPlayer)
						++his;
					
					// B Part for the 4 corners
					if(board[i1+i2][j1+j2] == currentPlayer)
						++mine;
					else if(board[i1+i2][j1+j2] == -currentPlayer)
						++his;
				}
		
		tempMine = mine;
		tempHis = his;
	}
	
	/**
	 * Count the pieces in the bord for both player
	 * @param currentPlayer
	 */
	private void getBordPiece(int currentPlayer)
	{
		int mine=0;
		int his = 0;
		
		for(int i = 0; i <= 1; i++)
			for(int j = 2; j <= 5; ++j)	
			{
				//Vertical
				if(board[i*(BOARD_SIZE-1)][j] == currentPlayer)
					++mine;
				else if(board[i*(BOARD_SIZE-1)][j] == -currentPlayer);
					++his;
					
				//Horizontal
				if(board[j][i*(BOARD_SIZE-1)] == currentPlayer)
					++mine;
				else if(board[j][i*(BOARD_SIZE-1)]  == -currentPlayer);
					++his;
			}
			
		tempMine = mine;
		tempHis = his;
	}
	
	/**
	 * Count the mobility for both player
	 * @param currentPlayer
	 */
	private void getMobility(int currentPlayer)
	{
		int[] possibleMove = new int[121];
		getAllPossibleMove(possibleMove, currentPlayer);
		
		int i;
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		int mine = i/2;
		
		getAllPossibleMove(possibleMove, -currentPlayer);
		for(i = 0; possibleMove[i] != DUMMY_VALUE;  i += 2);
		int his = i/2;
		
		tempMine = mine;
		tempHis = his;
	}
	
	/**
	 * Count the frontier pieces for both player. A piece is considered as frontier if and only if there is minimum one empty case in the neighborhood 
	 * @param currentPlayer
	 */
	private void getFrontierDiscs(int currentPlayer)
	{
		int mine = 0;
		int his = 0;
		
		for(int i = 1; i < BOARD_SIZE-1; ++i)
			for(int j = 1; j < BOARD_SIZE-1; ++j)
				if(board[i][j] != Empty && isFrontierDisc(i, j))
					if(board[i][j] == currentPlayer)
						++mine;
					else if(board[i][j] == -currentPlayer)
						++his;
		
		tempMine = mine;
		tempHis = his;
	}
	
	/**
	 * A piece is considered as frontier if and only if there is minimum one empty case in the neighborhood 
	 */
	private boolean isFrontierDisc(int i, int j)
	{
		/*
		 * X X X
		 * X O X
	 	 * X X X
		 */
		boolean out = false;
		
		for(int ii = i-1; !out && ii <= i+1; ++ii)
			for(int jj = j-1; !out && jj <= j+1; ++jj)
				out |= board[ii][jj] == Empty;
		
		return out;
	}
	
	/**
	 * Count the number of irreversible piece for both player
	 * @param currentPlayer
	 */
	private void getNbIrreversiblePiece(int currentPlayer)
	{
		int mine = 0, his = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] != Empty)
				{
					if(board[i][j] == currentPlayer && isIrreversiblePiece(i, j, currentPlayer))
						++mine;
					else if(board[i][j] == -currentPlayer && isIrreversiblePiece(i, j, -currentPlayer))
						++his;
				}
		tempMine = mine;
		tempHis = his;
	}

	/*
	 *========================================================*
	 *						CHECK PART
	 *========================================================*
	 */

	/**
	 * Check if the move is legit from left to right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkHorizontallyLeft2Right(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = col + 2; i < BOARD_SIZE; ++i)
			if(board[i-1][row] == oppositePlayer && board[i][row] == currentPlayer)
				return true;
			else if(board[i-1][row] != oppositePlayer || board[i][row] != oppositePlayer)
				return false;
		return false;
	}
	
	/**
	 * Check if the move is legit from right to left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkHorizontallyRight2Left(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int i = col - 2; i >= 0; --i)
			if(board[i+1][row] == oppositePlayer && board[i][row] == currentPlayer)
				return true;
			else if(board[i+1][row] != oppositePlayer || board[i][row] != oppositePlayer)
				return false;
		return false;		
	}
	
	/**
	 * Check if the move is legit from top to bottom
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkVerticallyTop2Bottom(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = row + 2; j < BOARD_SIZE; ++j)
			if(board[col][j-1] == oppositePlayer && board[col][j] == currentPlayer)
				return true;
			else if(board[col][j-1] != oppositePlayer || board[col][j] != oppositePlayer)
				return false;
		return false;
	}
	
	/**
	 * Check if the move is legit from bottom to top
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkVerticallyBottom2Top(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = row - 2; j >= 0; --j)
			if(board[col][j+1] == oppositePlayer && board[col][j] == currentPlayer)
				return true;
			else if(board[col][j+1] != oppositePlayer || board[col][j] != oppositePlayer)
				return false;
		return false;	
	}
	
	/**
	 * Check if the move is legit from top left to bottom right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkDiagonallyTopLeft2BottomRight(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = row + 2, i = col + 2; j < BOARD_SIZE && i < BOARD_SIZE; ++j, ++i)
			if(board[i-1][j-1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i-1][j-1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	/**
	 * Check if the move is legit from bottom right to top left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkDiagonallyBottomRight2TopLeft(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = row - 2, i = col - 2; j >= 0 && i >= 0; --j, --i)
			if(board[i+1][j+1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i+1][j+1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	/**
	 * Check if the move is legit from top right to bottom left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkDiagonallyTopRight2BottomLeft(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = row + 2, i = col - 2; j < BOARD_SIZE && i >= 0; ++j, --i)
			if(board[i+1][j-1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i+1][j-1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	/**
	 * Check if the move is legit from bottom left to top right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkDiagonallyBottomLeft2TopRight(int col, int row, int currentPlayer)
	{
		int oppositePlayer = -currentPlayer;
		for(int j = row - 2, i = col + 2; j >= 0 && i < BOARD_SIZE; --j, ++i)
			if(board[i-1][j+1] == oppositePlayer && board[i][j] == currentPlayer)
				return true;
			else if(board[i-1][j+1] != oppositePlayer || board[i][j] != oppositePlayer)
				return false;
		return false;
	}
	
	/**
	 * Check if the piece is irreversible from left to right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceHorizontallyRight(int col, int row, int currentPlayer)
	{
		for(int i = col + 1; i < BOARD_SIZE; ++i)
			if(board[i][row] != currentPlayer)
				return false;
		return true;
	}
	
	/**
	 * Check if the piece is irreversible from right to left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceHorizontallyLeft(int col, int row, int currentPlayer)
	{
		for(int i = col - 1; i >= 0; --i)
			if(board[i][row] != currentPlayer)
				return false;
		return true;
	}
	
	/**
	 * Check if the piece is irreversible from top to bottom
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceVerticallyBottom(int col, int row, int currentPlayer)
	{
		for(int j = row + 1; j < BOARD_SIZE; ++j)
			if(board[col][j] != currentPlayer)
				return false;
		return true;
	}
	
	/**
	 * Check if the piece is irreversible from bottom to top
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversibleVerticallyTop(int col, int row, int currentPlayer)
	{
		for(int j = row - 1; j >= 0; --j)
			if(board[col][j] != currentPlayer)
				return false;
		return true;
	}
	
	/**
	 * Check if the piece is irreversible from top left to bottom right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceDiagonallyBottomRight(int col, int row, int currentPlayer) 
	{
		for(int j = row + 1, i = col + 1; j < BOARD_SIZE && i < BOARD_SIZE; ++j, ++i)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	/**
	 * Check if the piece is irreversible from bottom right to top left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceDiagonallyTopLeft(int col, int row, int currentPlayer) 
	{
        for(int j = row - 1, i = col - 1; j >= 0 && i >= 0; --j, --i)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	/**
	 * Check if the piece is irreversible from top right to bottom left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceDiagonallyBottomLeft(int col, int row, int currentPlayer) 
	{
		for(int j = row + 1, i = col - 1; j < BOARD_SIZE && i >= 0; ++j, --i)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	/**
	 * Check if the piece is irreversible from bottom left to top right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 * @return
	 */
	private boolean checkIrreversiblePieceDiagonallyTopRight(int col, int row, int currentPlayer) 
	{
        for(int j = row - 1, i = col + 1; j >= 0 && i < BOARD_SIZE; --j, ++i)
        	if(board[i][j] != currentPlayer)
        		return false;
        return true;
	}
	
	/*
	 *========================================================*
	 *						ACTION PART
	 *========================================================*
	 */
	
	/**
	 * Return the necessary pieces from left to right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionHorizontallyLeft2Right(int col, int row, int currentPlayer)
	{
		for(int i = col + 1; i < BOARD_SIZE && board[i][row] == -currentPlayer; ++i)
			board[i][row] = currentPlayer;
	}
	
	/**
	 * Return the necessary pieces from right to left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionHorizontallyRight2Left(int col, int row, int currentPlayer)
	{
		for(int i = col - 1; i >= 0 && board[i][row] == -currentPlayer; --i)
			board[i][row] = currentPlayer;
	}
	
	/**
	 * Return the necessary pieces from top to bottom
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionVerticallyTop2Bottom(int col, int row, int currentPlayer)
	{
		for(int j = row + 1; j < BOARD_SIZE && board[col][j] == -currentPlayer; ++j)
			board[col][j] = currentPlayer;
	}
	
	/**
	 * Return the necessary pieces from bottom to topt
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionVerticallyBottom2Top(int col, int row, int currentPlayer)
	{
		for(int j = row - 1; j >= 0 && board[col][j] == -currentPlayer; --j)
			board[col][j] = currentPlayer;
	}
	
	/**
	 * Return the necessary pieces from top left to bottom right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionDiagonallyTopLeft2BottomRight(int col, int row, int currentPlayer)
	{
		for(int j = row + 1, i = col + 1; j < BOARD_SIZE && i < BOARD_SIZE && board[i][j] == -currentPlayer; ++j, ++i)
			board[i][j] = currentPlayer;	
	}
	
	/**
	 * Return the necessary pieces from bottom right to top left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionDiagonallyBottomRight2TopLeft(int col, int row, int currentPlayer)
	{
		for(int j = row - 1, i = col - 1; j >= 0 && i >= 0 && board[i][j] == -currentPlayer; --j, --i)
			board[i][j] = currentPlayer;
	}
	
	/**
	 * Return the necessary pieces from top right to bottom left
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionDiagonallyTopRight2BottomLeft(int col, int row, int currentPlayer)
	{
		for(int j = row + 1, i = col - 1; j < BOARD_SIZE && i >= 0 && board[i][j] == -currentPlayer; ++j, --i)
			board[i][j] = currentPlayer;
	}
	
	/**
	 * Return the necessary pieces from bottom left to top right
	 * @param col
	 * @param row
	 * @param currentPlayer
	 */
	private void actionDiagonallyBottomLeft2TopRight(int col, int row, int currentPlayer)
	{
		for(int j = row - 1, i = col + 1; j >= 0 && i < BOARD_SIZE && board[i][j] == -currentPlayer; --j, ++i)
			board[i][j] = currentPlayer;
	}
}
