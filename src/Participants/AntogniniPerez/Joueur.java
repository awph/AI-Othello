package Participants.AntogniniPerez;

import Othello.Move;

// Utile seulement dans cet exemple, pour lire l'entrée de l'utilisateur à la console
import java.util.Scanner;

public class Joueur extends Othello.Joueur {

	// depth: profondeur alpha-beta
	// playerID: 0 = rouge, 1 = bleu
	public Joueur(int depth, int playerID) {
		super();
	}

	Scanner stdin = new Scanner(System.in);

	public Move nextPlay(Move move) {
		Move result = null;
		if (move != null)
			System.out.println("Coup adverse: " + move.i + ", " + move.j);

		return result;
	}

}