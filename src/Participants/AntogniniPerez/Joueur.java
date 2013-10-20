package Participants.AntogniniPerez;

import java.util.Scanner;

import Othello.Move;

public class Joueur extends Othello.Joueur {

	private Board board;
	private int player;
	private int oppositePlayer;
	
	// playerID: 0 = red, 1 = blue
	public Joueur(int depth, int playerID) 
	{
		super(depth, playerID);
		player = playerID == 1 ? Board.Blue : Board.Red;
		oppositePlayer = -player;
		board = new Board();
	}

	Scanner stdin = new Scanner(System.in);
	
	public Move nextPlay(Move move) 
	{
		//DEBUG
		//Board board = new Board(true);
		//Move result = null;
		
		if (move != null) 
		{
			//System.out.println("Coup adverse : (" + move.j + ", " + move.i + ") Player : " + oppositePlayer);
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BEFORE");
			//board.debug__Board();
			board./*debug__*/addPiece(move.j, move.i, oppositePlayer);
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~AFTER");
			//board.debug__Board();
			//System.out.println("--------------------------------------------------------------");
		}
		
		Object[] result = Compute.alphaBeta(board, depth, 1, Compute.INF, player);
		int i = (int)result[1];
		int j = (int)result[2];
		if(i != Board.DUMMY_VALUE)
		{
			//System.out.println("Coup nous : (" + result.j + ", " + result.i + ") Player : " + player);
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BEFORE");
			//board.debug__Board();
			board./*debug__*/addPiece(i, j, player);
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~AFTER");
			//board.debug__Board();
			//System.out.println("--------------------------------------------------------------");
			System.out.println(player + " " + result[0]);
		}
		
		//stdin.nextInt();
		return (i != Board.DUMMY_VALUE) ? new Move(j, i) : null;
	}

}