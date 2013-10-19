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
		
		Move result = (Move) Compute.alphaBeta(board, depth, 1, Compute.INF, player)[1];
		
		if(result != null)
		{
			//System.out.println("Coup nous : (" + result.j + ", " + result.i + ") Player : " + player);
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BEFORE");
			//board.debug__Board();
			board./*debug__*/addPiece(result.j, result.i, player);
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~AFTER");
			//board.debug__Board();
			//System.out.println("--------------------------------------------------------------");
		}
		
		//stdin.nextInt();
		return result;
	}

}