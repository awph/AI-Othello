package Participants.AntogniniPerez;

/* 
 Exemple d'implémentation d'un joueur d'Othello. Cette implémentation sert uniquement
 à démontrer le principe, mais n'implémente aucune intelligence: les coups à jouer sont 
 simplement lus à la console!
 */

// Pour l'interopérabilité: il faut une représentation commune des coups!
import Othello.Move;

// Utile seulement dans cet exemple, pour lire l'entrée de l'utilisateur à la console
import java.util.Scanner;

// Vous devrez étendre Othello.Joueur pour implémenter votre propre joueur...
public class Joueur extends Othello.Joueur {

	// depth: profondeur alpha-beta
	// playerID: 0 = rouge, 1 = bleu
	public Joueur(int depth, int playerID) {
		super();
	}

	Scanner stdin = new Scanner(System.in);

	// Méthode appelée à chaque fois que vous devez jouer un coup.
	// move est le coup joué par l'adversaire
	public Move nextPlay(Move move) {
		// Ici, vous devrez
		// - Mettre à jour votre représentation du jeu en fonction du coup joué
		// par l'adversaire
		// - Décider quel coup jouer (alpha-beta!!)
		// - Remettre à jour votre représentation du jeu
		// - Retourner le coup choisi
		// Mais ici, on se contente de lire à la console:
		Move result = null;
		if (move != null)
			System.out.println("Coup adverse: " + move.i + ", " + move.j);
		System.out.println("Votre coup: ");
		System.out.print("Colonne (-1 si aucun coup possible): ");
		int i = stdin.nextInt();
		if (i != -1) {
			System.out.print("Ligne: ");
			int j = stdin.nextInt();
			result = new Move(i, j);
		}
		return result;
	}

}