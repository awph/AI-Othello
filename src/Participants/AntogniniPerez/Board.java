package Participants.AntogniniPerez;

import java.util.Arrays;

import Othello.Move;

public class Board {

	private static final int BOARD_SIZE = 8;

	private Piece[][] board;
	private Piece player;

	public Board(Piece player) {
		board = new Piece[BOARD_SIZE][BOARD_SIZE];
		this.player = player;
		init();
	}

	public boolean addPiece(Move move, Piece currentPlayer) {
		if (player == currentPlayer && !isLegit(move, currentPlayer))
			return false;
		
		System.out.println("\nBefore");
		debug__Board();
		System.out.println("\nAfter");
		update(move, currentPlayer);
		debug__Board();

		return true;
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

	private void update(Move move, Piece currentPlayer) {
		board[move.j][move.i] = currentPlayer;

		// Replace all the opposite pieces
		if (checkHorizontallyLeftToRight(move, currentPlayer))
			for (int i = move.i + 1; i < BOARD_SIZE && board[move.j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i)
				board[move.j][i] = currentPlayer;

		if (checkHorizontallyRightToLeft(move, currentPlayer))
			for (int i = move.i - 1; i > 0 && board[move.j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i)
				board[move.j][i] = currentPlayer;

		if (checkVerticallyTopToBottom(move, currentPlayer))
			for (int j = move.j + 1; j < BOARD_SIZE && board[j][move.i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++j)
				board[j][move.i] = currentPlayer;

		if (checkVerticallyBottomToTop(move, currentPlayer))
			for (int j = move.j - 1; j > 0 && board[j][move.i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --j)
				board[j][move.i] = currentPlayer;

		if (checkDiagonallyTopLeftToBottomRight(move, currentPlayer))
			for (int i = move.i + 1, j = move.j + 1; i < BOARD_SIZE && j < BOARD_SIZE && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i, ++j)
				board[j][i] = currentPlayer;

		if (checkDiagonallyBottomLeftToTopRight(move, currentPlayer))
			for (int i = move.i + 1, j = move.j - 1; i < BOARD_SIZE && j > 0 && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); ++i, --j)
				board[j][i] = currentPlayer;
		
		if(checkDiagonallyTopRightToBottomLeft(move, currentPlayer))
			for (int i = move.i - 1, j = move.j - 1; i > 0 && j > 0 && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i, --j)
				board[j][i] = currentPlayer;
		
		if(checkDiagonallyBottomRightToTopLeft(move, currentPlayer))
			for (int i = move.i - 1, j = move.j + 1; i > 0 && j < BOARD_SIZE && board[j][i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue); --i, ++j)
				board[j][i] = currentPlayer;
	}

	private boolean isLegit(Move move, Piece currentPlayer) {
		return (checkHorizontallyLeftToRight(move, currentPlayer) || checkHorizontallyRightToLeft(move, currentPlayer) || checkVerticallyTopToBottom(move, currentPlayer) || checkVerticallyBottomToTop(move, currentPlayer) || checkDiagonallyTopLeftToBottomRight(move, currentPlayer) || checkDiagonallyBottomLeftToTopRight(move, currentPlayer) || checkDiagonallyTopRightToBottomLeft(move, currentPlayer) || checkDiagonallyBottomRightToTopLeft(move, currentPlayer));
	}

	private boolean checkHorizontallyLeftToRight(Move move, Piece currentPlayer) {
		for (int i = move.i + 2; i < BOARD_SIZE; ++i) {
			if (board[move.j][i - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[move.j][i] == currentPlayer)
				return true;
			else if (board[move.j][i] == Piece.Empty)
				return false;
		}
		return false;
	}

	private boolean checkHorizontallyRightToLeft(Move move, Piece currentPlayer) {
		for (int i = move.i - 2; i > 0; --i) 
			if (board[move.j][i + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[move.j][i] == currentPlayer)
				return true;
			else if (board[move.j][i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkVerticallyTopToBottom(Move move, Piece currentPlayer) {
		for (int j = move.j + 2; j < BOARD_SIZE; ++j)
			if (board[j - 1][move.i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][move.i] == currentPlayer)
				return true;
			else if (board[j][move.i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkVerticallyBottomToTop(Move move, Piece currentPlayer) {
		for (int j = move.j - 2; j > 0; --j)
			if (board[j + 1][move.i] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][move.i] == currentPlayer)
				return true;
			else if (board[j][move.i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkDiagonallyTopLeftToBottomRight(Move move, Piece currentPlayer) {
		for (int i = move.i + 2, j = move.j + 2; i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
			if (board[j - 1][i - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkDiagonallyBottomLeftToTopRight(Move move, Piece currentPlayer) {
		for (int i = move.i + 2, j = move.j - 2; i < BOARD_SIZE && j > 0; ++i, --j)
			if (board[j + 1][i - 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}
	
	private boolean checkDiagonallyTopRightToBottomLeft(Move move, Piece currentPlayer) {
		for (int i = move.i - 2, j = move.j - 2; i > 0 && j > 0; --i, --j)
			if (board[j + 1][i + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}

	private boolean checkDiagonallyBottomRightToTopLeft(Move move, Piece currentPlayer) {
		for (int i = move.i - 2, j = move.j + 2; i > 0 && j < BOARD_SIZE; --i, ++j)
			if (board[j - 1][i + 1] == (currentPlayer == Piece.Blue ? Piece.Red : Piece.Blue) && board[j][i] == currentPlayer)
				return true;
			else if (board[j][i] == Piece.Empty)
				return false;
		return false;
	}
}
