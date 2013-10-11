package Participants.AntogniniPerez;

import java.util.Arrays;

import Othello.Move;

public class Board {

	private int[][] board;
	private static final int BOARD_SIZE = 8;
	private Piece player;
	
	public Board(Piece player)
	{
		board = new int[BOARD_SIZE][BOARD_SIZE];
		this.player = player;
		init();
	}
	
	private void init()
	{
		for(int i = 0; i < board.length; ++i)
			Arrays.fill(board[i], Piece.Empty.getValue());
		
		board[BOARD_SIZE/2][BOARD_SIZE/2] = Piece.Blue.getValue();
		board[BOARD_SIZE/2+1][BOARD_SIZE/2+1] = Piece.Blue.getValue();
		board[BOARD_SIZE/2+1][BOARD_SIZE/2] = Piece.Red.getValue();
		board[BOARD_SIZE/2][BOARD_SIZE/2+1] = Piece.Red.getValue();
	}
	
	public boolean addPiece(Move move, Piece currentPlayer)
	{
		if(player == currentPlayer && !isLegit(move))
			return false;
		
		update(move, currentPlayer);
		return true;
	}
	
	private boolean isLegit(Move move)
	{
		return true;
	}
	
	private void update(Move move, Piece currentPlayer)
	{
		
	}
}
