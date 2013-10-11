package Participants.AntogniniPerez;

import Othello.Move;

// Utile seulement dans cet exemple, pour lire l'entr�e de l'utilisateur � la console
import java.util.Scanner;

public class Joueur extends Othello.Joueur {

	private Board board;
	// depth: profondeur alpha-beta
	// playerID: 0 = rouge, 1 = bleu
	public Joueur(int depth, int playerID) {
		super();
		board = new Board(playerID == 1 ? Piece.Blue : Piece.Red);
	}

	public Move nextPlay(Move move) {
		Move result = null;
		if (move != null)
			System.out.println("Coup adverse: " + move.i + ", " + move.j);

		return result;
	}

}