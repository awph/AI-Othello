package Participants.AntogniniPerez;

import java.util.Arrays;

import Othello.Move;

public class Board {

	private static final int BOARD_SIZE = 8;
	
	private Piece[][] board;
	private Piece player;
	
	public Board(Piece player)
	{
		board = new Piece[BOARD_SIZE][BOARD_SIZE];
		this.player = player;
		init();
	}
	
	public boolean addPiece(Move move, Piece currentPlayer)
	{
		if(player == currentPlayer && !isLegit(move))
			return false;
		
		update(move, currentPlayer);
		return true;
	}

	private void init()
	{
		for(int i = 0; i < board.length; ++i)
			Arrays.fill(board[i], Piece.Empty);
		
		board[BOARD_SIZE/2][BOARD_SIZE/2] = Piece.Blue;
		board[BOARD_SIZE/2+1][BOARD_SIZE/2+1] = Piece.Blue;
		board[BOARD_SIZE/2+1][BOARD_SIZE/2] = Piece.Red;
		board[BOARD_SIZE/2][BOARD_SIZE/2+1] = Piece.Red;
	}
	
	private void update(Move move, Piece currentPlayer)
	{
		board[move.i][move.j] = currentPlayer; 
		
		//Replace all the opposite pieces
		if(checkHorizontallyLeftToRight(move))
			for(int i = move.i+1; i < BOARD_SIZE && board[i][move.j] == (player == Piece.Blue ? Piece.Red : Piece.Blue); ++i)
				board[i][move.j] = player; 
		if(checkHorizontallyRightToLeft(move))
			for(int i = move.i-1; i > 0 && board[i][move.j] == (player == Piece.Blue ? Piece.Red : Piece.Blue); --i)
				board[i][move.j] = player; 
		if(checkVerticallyTopToBottom(move))
			for(int j = move.j+1; j < BOARD_SIZE && board[move.i][j] == (player == Piece.Blue ? Piece.Red : Piece.Blue); ++j)
				board[move.i][j] = player;
		if(checkVerticallyBottomToTop(move))
			for(int j = move.j-1; j > 0 && board[move.i][j] == (player == Piece.Blue ? Piece.Red : Piece.Blue); --j)
				board[move.i][j] = player;
		if(checkDiagonallyTopLeftToBottomRight(move))
			for(int i = move.i+1, j = move.j+1; i < BOARD_SIZE && j < BOARD_SIZE && board[i][j] == (player == Piece.Blue ? Piece.Red : Piece.Blue); ++i, ++j)
				board[i][j] = player;
		if(checkDiagonallyBottomLeftToTopRight(move))
			for(int i = move.i+2, j = move.j-2; i < BOARD_SIZE && j > 0 && board[i][j] == (player == Piece.Blue ? Piece.Red : Piece.Blue); ++i, --j)
				board[i][j] = player;		
	}
	
	private boolean isLegit(Move move)
	{
		return (checkHorizontallyLeftToRight(move) || checkHorizontallyRightToLeft(move)
				|| checkVerticallyTopToBottom(move) || checkVerticallyBottomToTop(move)
				|| checkDiagonallyTopLeftToBottomRight(move) || checkDiagonallyBottomLeftToTopRight(move));
	}
	
	private boolean checkHorizontallyLeftToRight(Move move)
	{
		for(int i = move.i+2; i < BOARD_SIZE; ++i)
		{
			if(board[i-1][move.j] == (player == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][move.j] == player)
				return true;
			else if(board[i][move.j] == Piece.Empty)
				return false;
		}
		return false;
	}
	
	private boolean checkHorizontallyRightToLeft(Move move)
	{
		for(int i = move.i-2; i > 0; --i)
		{
			if(board[i+1][move.j] == (player == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][move.j] == player)
				return true;
			else if(board[i][move.j] == Piece.Empty)
				return false;
		}
		return false;
	}
	
	private boolean checkVerticallyTopToBottom(Move move)
	{
		for(int j = move.j+2; j < BOARD_SIZE; ++j)
		{
			if(board[move.i][j-1] == (player == Piece.Blue ? Piece.Red : Piece.Blue) && board[move.i][j] == player)
				return true;
			else if(board[move.i][j] == Piece.Empty)
				return false;
		}
		return false;
	}
	
	private boolean checkVerticallyBottomToTop(Move move)
	{
		for(int j = move.j-2; j > 0; --j)
		{
			if(board[move.i][j+1] == (player == Piece.Blue ? Piece.Red : Piece.Blue) && board[move.i][j] == player)
				return true;
			else if(board[move.i][j] == Piece.Empty)
				return false;
		}
		return false;
	}
	
	private boolean checkDiagonallyTopLeftToBottomRight(Move move)
	{
		for(int i = move.i+2, j = move.j+2; i < BOARD_SIZE && j < BOARD_SIZE; ++i, ++j)
		{
			if(board[i-1][j-1] == (player == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][j] == player)
				return true;
			else if(board[i][j] == Piece.Empty)
				return false;
		}
		return false;
	}
	
	private boolean checkDiagonallyBottomLeftToTopRight(Move move)
	{
		for(int i = move.i+2, j = move.j-2; i < BOARD_SIZE && j > 0; ++i, --j)
		{
			if(board[i-1][j+1] == (player == Piece.Blue ? Piece.Red : Piece.Blue) && board[i][j] == player)
				return true;
			else if(board[i][j] == Piece.Empty)
				return false;
		}
		return false;
	}
}
