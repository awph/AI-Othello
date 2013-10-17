package Participants.AntogniniPerez;

import Othello.Move;

public class Joueur extends Othello.Joueur {

	private Board board;

	// playerID: 0 = red, 1 = blue
	public Joueur(int depth, int playerID) 
	{
		super();
		board = new Board(playerID == 1 ? Piece.Blue : Piece.Red);
	}

	public Move nextPlay(Move move) 
	{
		Move result = null;
		if (move != null)
			System.out.println("Coup adverse: " + move.i + ", " + move.j);

		return result;
	}

}