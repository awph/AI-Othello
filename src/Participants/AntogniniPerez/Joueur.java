package Participants.AntogniniPerez;

import Othello.Move;

public class Joueur extends Othello.Joueur {

	private Board board;
	private int player;
	private int oppositePlayer;
	private Compute compute;
	
	// playerID: 0 = red, 1 = blue
	public Joueur(int depth, int playerID) 
	{
		super(depth, playerID);
		player = playerID == 1 ? Board.Blue : Board.Red;
		oppositePlayer = -player;
		board = new Board();
		compute = new Compute();
	}

	public Move nextPlay(Move move) 
	{
		if (move != null) 
			board.addPiece(move.j, move.i, oppositePlayer);
		
		compute.initialize();
		compute.alphaBeta(board, depth, 1, Compute.INF, player);
		
		int i = compute.getI();
		int j = compute.getJ();
		
		if(i != Board.DUMMY_VALUE)
			board.addPiece(i, j, player);

		return (i != Board.DUMMY_VALUE) ? new Move(j, i) : null;
	}

}