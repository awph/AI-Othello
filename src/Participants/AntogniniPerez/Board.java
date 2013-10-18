package Participants.AntogniniPerez;

import java.util.Arrays;

import Othello.Move;

public class Board {

	public static final int DUMMY_VALUE = -1;// To know where to stop in the
												// possible move array

	private static final int BOARD_SIZE = 8;
	private Piece[][] board; // [Cols][Rows]
	private Piece player;

	public Board(Piece player) {
		board = new Piece[BOARD_SIZE][BOARD_SIZE];
		this.player = player;
		init();
	}

	/**
	 * Board must be of size Board::BOARD_SIZExBoard::BOARD_SIZE
	 */
	public Board(Board board) {
		this(board.player);
		for (int i = 0; i < BOARD_SIZE; ++i)
			for (int j = 0; j < BOARD_SIZE; ++j)
				this.board[j][i] = board.board[j][i];
	}

	public boolean addPiece(int rowMove, int colMove, Piece currentPlayer) {
		if (player == currentPlayer && !isLegit(rowMove, colMove, currentPlayer))
			return false;

		update(rowMove, colMove, currentPlayer);

		return true;
	}

	public boolean isFinished() {
		for (int i = 0; i < BOARD_SIZE; ++i)
			for (int j = 0; j < BOARD_SIZE; ++j)
				if (board[j][i] == Piece.Empty && (isLegit(i, j, Piece.Blue) || isLegit(i, j, Piece.Red)))
					return false;
		return true;
	}

	/**
	 * A Piece is considered as irreversible if and only if theses 3 sates are
	 * correct: - The left or right side are filled by the same piece color -
	 * The top or bottom side are filled by the same piece color - 2 diagonally
	 * neighbouring size are filled by the same piece color
	 */
	public boolean isIrreversiblePiece(int rowMove, int colMove, Piece currentPlayer) {
		// State 1
		boolean isStateOneValid = true;
		{
			// Left side
			isStateOneValid = checkIrreversiblePieceHorizontalLeft(rowMove, colMove, currentPlayer);

			// Right side
			if (!isStateOneValid)
				isStateOneValid = checkIrreversiblePieceHorizontalRight(rowMove, colMove, currentPlayer);

			if (!isStateOneValid)
				return false;
		}

		// State 2
		boolean isStateTwoValid = true;
		{
			// Top side
			isStateTwoValid = checkIrreversiblePieceVerticallyTop(rowMove, colMove, currentPlayer);

			// Bottom side
			if (!isStateTwoValid)
				isStateTwoValid = checkIrreversiblePieceVerticallyBottom(rowMove, colMove, currentPlayer);

			if (!isStateTwoValid)
				return false;
		}

		// State 3
		boolean isStateThreeValid = true;
		{
			// Left diagonals
			if (checkIrreversiblePieceDiagonalTopLeft(rowMove, colMove, currentPlayer))
				isStateThreeValid = checkIrreversiblePieceDiagonalBottomLeft(rowMove, colMove, currentPlayer);

			if (isStateThreeValid)
				return true;

			// Top diagonals
			isStateThreeValid = true;
			if (checkIrreversiblePieceDiagonalTopLeft(rowMove, colMove, currentPlayer))
				isStateThreeValid = checkIrreversiblePieceDiagonalTopRight(rowMove, colMove, currentPlayer);

			if (isStateThreeValid)
				return true;

			// Right diagonals
			isStateThreeValid = true;
			if (checkIrreversiblePieceDiagonalTopRight(rowMove, colMove, currentPlayer))
				isStateThreeValid = checkIrreversiblePieceDiagonalBottomRight(rowMove, colMove, currentPlayer);

			if (isStateThreeValid)
				return true;

			// Bottom diagonals
			isStateThreeValid = true;
			if (checkIrreversiblePieceDiagonalBottomRight(rowMove, colMove, currentPlayer))
				isStateThreeValid = checkIrreversiblePieceDiagonalBottomLeft(rowMove, colMove, currentPlayer);

			return isStateThreeValid;// State1 AND State2 AND State3
		}

	}

	public void getAllPossibleMove(int[] outputArray, Piece currentPlayer) {
		int indexOutputArray = 0;
		for (int i = 0; i < BOARD_SIZE; ++i)
			for (int j = 0; j < BOARD_SIZE; ++j)
				if (board[j][i] == Piece.Empty && isLegit(i, j, currentPlayer)) {
					outputArray[indexOutputArray++] = i;
					outputArray[indexOutputArray++] = j;
				}
		outputArray[indexOutputArray] = DUMMY_VALUE;
	}

	public int getFinalPoints(Piece currentPlayer) {
		int mine = 0, his = 0, empty = 0;

		for (int i = 0; i < BOARD_SIZE; ++i)
			for (int j = 0; j < BOARD_SIZE; ++j)
				if (board[i][j] == currentPlayer)
					++mine;
				else if (board[i][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
					++his;
				else
					++empty;

		if (mine > his)
			return mine + empty;
		else
			return mine;
	}

	public void debug__Board() {
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j)
				System.out.print(board[i][j] + "\t");
			System.out.println();
		}
	}

	private boolean checkIrreversiblePieceVerticallyBottom(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int j = colMove + 1; out && j < BOARD_SIZE; ++j)
			if (board[j][rowMove] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceVerticallyTop(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int j = colMove - 1; out && j > 0; --j)
			if (board[j][rowMove] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceHorizontalLeft(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int i = rowMove - 1; out && i > 0; --i)
			if (board[colMove][i] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceHorizontalRight(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int i = rowMove + 1; out && i < BOARD_SIZE; ++i)
			if (board[colMove][i] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceDiagonalBottomRight(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int i = rowMove + 1, j = colMove + 1; out && i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
			if (board[j][i] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceDiagonalTopRight(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int i = rowMove - 1, j = colMove + 1; out && i > 0 && j < BOARD_SIZE; --i, ++j)
			if (board[j][i] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceDiagonalTopLeft(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int i = rowMove - 1, j = colMove - 1; out && i > 0 && j > 0; --i, --j)
			if (board[j][i] != currentPlayer)
				out = false;
		return out;
	}

	private boolean checkIrreversiblePieceDiagonalBottomLeft(int rowMove, int colMove, Piece currentPlayer) {
		boolean out = true;
		for (int i = rowMove + 1, j = colMove - 1; out && i < BOARD_SIZE && j > 0; ++i, --j)
			if (board[j][i] != currentPlayer)
				out = false;
		return out;
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
		if (checkHorizontallyLeftToRight(rowMove, colMove, currentPlayer))
			for (int i = colMove + 1; i < BOARD_SIZE && board[i][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i)
				board[i][rowMove] = currentPlayer;

		if (checkHorizontallyRightToLeft(rowMove, colMove, currentPlayer))
			for (int i = colMove - 1; i > 0 && board[i][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i)
				board[i][rowMove] = currentPlayer;

		if (checkVerticallyTopToBottom(rowMove, colMove, currentPlayer))
			for (int j = rowMove + 1; j < BOARD_SIZE && board[colMove][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++j)
				board[colMove][j] = currentPlayer;

		if (checkVerticallyBottomToTop(rowMove, colMove, currentPlayer))
			for (int j = rowMove - 1; j > 0 && board[colMove][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --j)
				board[colMove][j] = currentPlayer;

		if (checkDiagonallyTopLeftToBottomRight(rowMove, colMove, currentPlayer))
			for (int i = colMove + 1, j = rowMove + 1; i < BOARD_SIZE && j < BOARD_SIZE && board[i][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i, ++j)
				board[i][j] = currentPlayer;

		if (checkDiagonallyBottomLeftToTopRight(rowMove, colMove, currentPlayer))
			for (int i = colMove + 1, j = rowMove - 1; i < BOARD_SIZE && j > 0 && board[i][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i, --j)
				board[i][j] = currentPlayer;

		if (checkDiagonallyTopRightToBottomLeft(rowMove, colMove, currentPlayer))
			for (int i = colMove - 1, j = rowMove + 1; i > 0 && j < BOARD_SIZE && board[i][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i, ++j)
				board[i][j] = currentPlayer;

		if (checkDiagonallyBottomRightToTopLeft(rowMove, colMove, currentPlayer))
			for (int i = colMove - 1, j = rowMove - 1; i > 0 && j > 0 && board[i][j] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i, --j)
				board[i][j] = currentPlayer;
	}

	private boolean isLegit(int rowMove, int colMove, Piece currentPlayer) {

		System.out.println(checkHorizontallyLeftToRight(rowMove, colMove, currentPlayer));
		System.out.println(checkHorizontallyRightToLeft(rowMove, colMove, currentPlayer));
		System.out.println(checkVerticallyTopToBottom(rowMove, colMove, currentPlayer));
		System.out.println(checkVerticallyBottomToTop(rowMove, colMove, currentPlayer));
		System.out.println(checkDiagonallyTopLeftToBottomRight(rowMove, colMove, currentPlayer));
		System.out.println(checkDiagonallyBottomLeftToTopRight(rowMove, colMove, currentPlayer));
		System.out.println(checkDiagonallyTopRightToBottomLeft(rowMove, colMove, currentPlayer));
		System.out.println(checkDiagonallyBottomRightToTopLeft(rowMove, colMove, currentPlayer));

		return (checkHorizontallyLeftToRight(rowMove, colMove, currentPlayer) || checkHorizontallyRightToLeft(rowMove, colMove, currentPlayer) || checkVerticallyTopToBottom(rowMove, colMove, currentPlayer) || checkVerticallyBottomToTop(rowMove, colMove, currentPlayer) || checkDiagonallyTopLeftToBottomRight(rowMove, colMove, currentPlayer) || checkDiagonallyBottomLeftToTopRight(rowMove, colMove, currentPlayer) || checkDiagonallyTopRightToBottomLeft(rowMove, colMove, currentPlayer) || checkDiagonallyBottomRightToTopLeft(
				rowMove, colMove, currentPlayer));
	}

	private boolean checkHorizontallyLeftToRight(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = colMove + 2; i < BOARD_SIZE; ++i) {
			System.out.println(board[colMove][i - 1] + " " + board[colMove][i] + " " + currentPlayer + " " + player);
			if (board[i - 1][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][rowMove] == currentPlayer)
				return true;
			else if (board[i - 1][rowMove] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[i][colMove] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		}
		return false;
	}

	private boolean checkHorizontallyRightToLeft(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = colMove - 2; i > 0; --i)
			if (board[i+1][rowMove] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][rowMove] == currentPlayer)
				return true;
			else if (board[i+1][rowMove] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[i][rowMove] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	private boolean checkVerticallyTopToBottom(int rowMove, int colMove, Piece currentPlayer) {
		for (int j = rowMove + 2; j < BOARD_SIZE; ++j)
			if (board[colMove][j - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[colMove][j] == currentPlayer)
				return true;
			else if (board[colMove][j - 1] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[colMove][j] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	private boolean checkVerticallyBottomToTop(int rowMove, int colMove, Piece currentPlayer) {
		for (int j = rowMove - 2; j > 0; --j)
			if (board[colMove][j + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[colMove][j] == currentPlayer)
				return true;
			else if (board[colMove][j + 1] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[colMove][j] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	private boolean checkDiagonallyTopLeftToBottomRight(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = colMove + 2, j = rowMove + 2; i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
			if (board[i - 1][j - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][j] == currentPlayer)
				return true;
			else if (board[i - 1][j - 1] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[i][j] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	private boolean checkDiagonallyBottomLeftToTopRight(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = colMove + 2, j = rowMove - 2; i < BOARD_SIZE && j > 0; ++i, --j)
			if (board[i - 1][j + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][j] == currentPlayer)
				return true;
			else if (board[i - 1][j + 1] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[i][j] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	private boolean checkDiagonallyTopRightToBottomLeft(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = colMove - 2, j = rowMove + 2; i > 0 && j < BOARD_SIZE; --i, ++j)
			if (board[i + 1][j - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][j] == currentPlayer)
				return true;
			else if (board[i + 1][j - 1] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[i][j] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	private boolean checkDiagonallyBottomRightToTopLeft(int rowMove, int colMove, Piece currentPlayer) {
		for (int i = colMove - 2, j = rowMove - 2; i > 0 && j > 0; --i, --j)
			if (board[i - 1][j - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][j] == currentPlayer)
				return true;
			else if (board[i - 1][j - 1] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) || board[i][j] != (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue))
				return false;
		return false;
	}

	public Piece getPlayer() {
		return player;
	}
}
