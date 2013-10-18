package Participants.AntogniniPerez;

import java.util.Scanner;

import Othello.Move;

public class Joueur extends Othello.Joueur {

	private Board board;
	private Piece player;
	private int[] allPossibleMove;
	
	// playerID: 0 = red, 1 = blue
	public Joueur(int depth, int playerID) 
	{
		super();
		player = playerID == 1 ? Piece.Blue : Piece.Red;
		allPossibleMove = new int[121];
		board = new Board(player);
	}

	Scanner stdin = new Scanner(System.in);
	
	public Move nextPlay(Move move) 
	{
		// Ici, vous devrez
		// - Mettre à jour votre représentation du jeu en fonction du coup joué par l'adversaire
		// - Décider quel coup jouer (alpha-beta!!)
		// - Remettre à jour votre représentation du jeu
		// - Retourner le coup choisi
		// Mais ici, on se contente de lire à la console:
		Move result = null;
		if (move != null) 
		{
			System.out.println("Coup adverse: " + move.i + ", " + move.j);
			board.addPiece(move.i, move.j, player == Piece.Red ? Piece.Blue : Piece.Red);
		}
		
		result = (Move) Compute.alphabeta(board, depth, 1, Compute._INF)[1];
		
		//System.out.println("Votre coup: ");
		//System.out.print("Colonne (-1 si aucun coup possible): ");
		//int i = stdin.nextInt();
		//if (i != -1) {
			//System.out.print("Ligne: ");
			//int j =  stdin.nextInt();
			//result = new Move(i,j);
			board.addPiece(result.i, result.j, player);
		//}
		return result;
	}

}