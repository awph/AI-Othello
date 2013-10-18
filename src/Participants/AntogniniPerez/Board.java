package Participants.AntogniniPerez;

import java.util.Arrays;

import Othello.Move;

public class Board {

	public static final int DUMMY_VALUE = -1;//To know where to stop in the possible move array
	
	private static final int BOARD_SIZE = 8;
	private Piece[][] board;
	private Piece player;

	public Board(Piece player) {
		board = new Piece[BOARD_SIZE][BOARD_SIZE];
		this.player = player;
		init();
	}

	/**
	 * Board must be of size Board::BOARD_SIZExBoard::BOARD_SIZE
	 */
	public Board(Piece player, Board board)
	{
		this(player);
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				this.board[j][i] = board.board[j][i];
	}
	
	public boolean addPiece(Move move, Piece currentPlayer) {
		if (player == currentPlayer && !isLegit(move.i, move.j, currentPlayer))
			return false;
		
		System.out.println("\nBefore");
		debug__Board();
		System.out.println("\nAfter");
		update(move.i, move.j, currentPlayer);
		debug__Board();

		return true;
	}

	public void getAllPossibleMove(int[] outputArray, Piece currentPlayer)
	{
		int indexOutputArray = 0;
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(isLegit(i, j, currentPlayer))
				{
					outputArray[indexOutputArray++] = i;
					outputArray[indexOutputArray++] = j;
				}
		outputArray[indexOutputArray] = DUMMY_VALUE;
	}
	
	public int getFinalPoints(Piece currentPlayer)
	{
		int mine = 0, his = 0, empty = 0;
		
		for(int i = 0; i < BOARD_SIZE; ++i)
			for(int j = 0; j < BOARD_SIZE; ++j)
				if(board[i][j] == currentPlayer)
					++mine;
				else if(board[i][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
					++his;
				else
					++empty;
		
		if(mine > his)
			return mine + empty;
		else
			return mine;
	}
	
	private void debug__Board() {
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j)
				System.out.print(board[i][j] + "\t");
			System.out.println();
		}
	}

	private void init() {
		for (int i = 0; i < board.length; ++i)
			Arrays.fill(board[i], Piece.Empty);

		board[BOARD_SIZE / 2 - 1][BOARD_SIZE / 2 - 1] = Piece.Blue;
		board[BOARD_SIZE / 2][BOARD_SIZE / 2] = Piece.Blue;
		board[BOARD_SIZE / 2][BOARD_SIZE / 2 - 1] = Piece.Red;
		board[BOARD_SIZE / 2 - 1][BOARD_SIZE / 2] = Piece.Red;
	}

	private void update(int rowMove, int colMove, Piece currentPlayer) {
		board[colMove][rowMove] = currentPlayer;

		// Replace all the opposite pieces
		if (checkHorizontallyLeftToRight(rowMove, colMove,  currentPlayer))
			for (int i = rowMove + 1; i < BOARD_SIZE && board[colMove][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i)
				board[colMove][i] = currentPlayer;

		if (checkHorizontallyRightToLeft(rowMove, colMove,  currentPlayer))
			for (int i = rowMove - 1; i > 0 && board[colMove][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i)
				board[colMove][i] = currentPlayer;

		if (checkVerticallyTopToBottom(rowMove, colMove,  currentPlayer))
			for (int j = colMove + 1; j < BOARD_SIZE && board[j][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++j)
				board[j][rowMove] = currentPlayer;

		if (checkVerticallyBottomToTop(rowMove, colMove,  currentPlayer))
			for (int j = colMove - 1; j > 0 && board[j][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --j)
				board[j][rowMove] = currentPlayer;

		if (checkDiagonallyTopLeftToBottomRight(rowMove, colMove,  currentPlayer))
			for (int i = rowMove + 1, j = colMove + 1; i < BOARD_SIZE && j < BOARD_SIZE && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i, ++j)
				board[j][i] = currentPlayer;

		if (checkDiagonallyBottomLeftToTopRight(rowMove, colMove,  currentPlayer))
			for (int i = rowMove + 1, j = colMove - 1; i < BOARD_SIZE && j > 0 && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i, --j)
				board[j][i] = currentPlayer;
		
		if(checkDiagonallyTopRightToBottomLeft(rowMove, colMove,  currentPlayer))
			for (int i = rowMove - 1, j = colMove - 1; i > 0 && j > 0 && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i, --j)
				board[j][i] = currentPlayer;
		
		if(checkDiagonallyBottomRightToTopLeft(rowMove, colMove,  currentPlayer))
			for (int i = rowMove - 1, j = colMove + 1; i > 0 && j < BOARD_SIZE && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i, ++j)
				board[j][i] = currentPlayer;
	}

	private boolean isLegit(int rowMove, int colMove, Piece currentPlayer) {
		return (checkHorizontallyLeftToRight(rowMove, colMove,  currentPlayer) || checkHorizontallyRightToLeft(rowMove, colMove,  currentPlayer) || checkVerticallyTopToBottom(rowMove, colMove,  currentPlayer) || checkVerticallyBottomToTop(rowMove, colMove,  currentPlayer) || checkDiagonallyTopLeftToBottomRight(rowMove, colMove,  currentPlayer) || checkDiagonallyBottomLeftToTopRight(rowMove, colMove,  currentPlayer) || checkDiagonallyTopRightToBottomLeft(rowMove, colMove,  currentPlayer) || checkDiagonallyBottomRightToTopLeft(rowMove, colMove,  currentPlayer));
	}

	private boolean checkHorizontallyLeftToRight(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = rowMove + 2; i < BOARD_SIZE; ++i) {
			if (board[colMove][i - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[colMove][i] == currentPlayer)
				return true;
			else if (board[colMove][i] == Piece.Empty)
				return false;
		}
		return false;
	}

	private boolean checkHorizontallyRightToLeft(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = rowMove - 2; i > 0; --i) 
			if (board[colMove][i + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[colMove][i] == currentPlayer)
				return true;
			else if (board[colMove][i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkVerticallyTopToBottom(int rowMove, int colMove, Piece currentPlayer) {
		for (int j = colMove + 2; j < BOARD_SIZE; ++j)
			if (board[j - 1][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][rowMove] == currentPlayer)
				return true;
			else if (board[j][rowMove] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkVerticallyBottomToTop(int rowMove, int colMove, Piece currentPlayer) {
		for (int j = colMove - 2; j > 0; --j)
			if (board[j + 1][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][rowMove] == currentPlayer)
				return true;
			else if (board[j][rowMove] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkDiagonallyTopLeftToBottomRight(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = rowMove + 2, j = colMove + 2; i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
			if (board[j - 1][i - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkDiagonallyBottomLeftToTopRight(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = rowMove + 2, j = colMove - 2; i < BOARD_SIZE && j > 0; ++i, --j)
			if (board[j + 1][i - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}
	
	private boolean checkDiagonallyTopRightToBottomLeft(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = rowMove - 2, j = colMove - 2; i > 0 && j > 0; --i, --j)
			if (board[j + 1][i + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkDiagonallyBottomRightToTopLeft(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = rowMove - 2, j = colMove + 2; i > 0 && j < BOARD_SIZE; --i, ++j)
			if (board[j - 1][i + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}
}
