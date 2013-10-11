package  Othello ;

// *************************************************
//  Classe implementant l'algorithme de Jeu OTHELLO
//  ************************************************

public class Joueur
{

	// 0 : joue les rouges, 1 : joue les bleus
	protected int playerID;
	protected int depth ;		// Profondeur alpha-beta
	protected int size = 8; // taille du plateau de jeu
	
	// ******************************************************
	//   Constructeur appele par l'Applet Othello
	// ******************************************************
    public Joueur() { }
	
	public Joueur(int depth, int playerID) 
	{  
	  this.depth = depth ; 
	  this.playerID = playerID ;
	}
	
	// ******************************************************
	//   M\'ethode appel\'ee \`a chaque coup du Joueur Adverse
	//     PARAMETRE : move est le coup jou\'e par l'adversaire
	//           move == null si aucun coup jou\'e.
	//     RETOURNE : le coup jou\'e en retour
	//			retourne null si aucun coup n'est possible.
	// ******************************************************
	public Move nextPlay(Move move)
	{
	    return null ;
	}			
}
		
