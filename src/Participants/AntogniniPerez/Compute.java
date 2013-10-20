package Participants.AntogniniPerez;

public class Compute {

	public static final double INF = Integer.MAX_VALUE;
	
	//Maximize -> minOrMax = 1, otherwhise -1 (minimize)
	//Object[] -> [0] -> Score, [1] -> Move.j, [2] -> Move.i
	public static Object[] alphaBeta(Board root, int depth, int minOrMax, double parentValue, int currentPlayer)
	{
		if(depth == 0 || root.isTheGameEnded())
			return new Object[] { eval(root, currentPlayer, depth), Board.DUMMY_VALUE, Board.DUMMY_VALUE };
		
		double optVal = minOrMax * -INF;
		int optOpi = Board.DUMMY_VALUE, optOpj = Board.DUMMY_VALUE;
		
		int[] allPossibleMoves = new int[121];
		root.getAllPossibleMove(allPossibleMoves, currentPlayer);

		if(allPossibleMoves[0] == Board.DUMMY_VALUE)
			optVal = (double) alphaBeta(root, depth-1, -minOrMax, optVal, -currentPlayer)[0];
		else
		{
			for(int i = 0; allPossibleMoves[i] > Board.DUMMY_VALUE; i += 2)
			{
				Board newNode = new Board(root);
				newNode.addPiece(allPossibleMoves[i], allPossibleMoves[i+1], currentPlayer);
				
				double val = (double) alphaBeta(newNode, depth-1, -minOrMax, optVal, -currentPlayer)[0];
				
				if(val*minOrMax > optVal*minOrMax)
				{
					optVal = val;
					optOpi = allPossibleMoves[i];
					optOpj = allPossibleMoves[i+1];
					if(optVal*minOrMax > parentValue*minOrMax)
						break;
				}
			}
		}
		
		return new Object[] {optVal, optOpi, optOpj};
	}
	
	private static double eval(Board root, int currentPlayer,int depth) 
	{
		//[-1;1] f(t) -> 1 -> Good
		double scorePieceDifference = root.getPieceDifference(currentPlayer);
		double scoreCornerOccupacy = root.getCornerOccupacy(currentPlayer);
		double scoreCornerCloseness = root.getCornerCloseness(currentPlayer);
		double scoreMobilityScore = root.getMobilityScore(currentPlayer);
		double scoreFrontierDiscs = root.getFrontierDiscs(currentPlayer);
		double scoreDiscSquare = root.getDiscSquares(currentPlayer);
		double scoreIrreversiblePiece = root.getIrreversiblePiece(currentPlayer);//€N
		double scoreParity = (root.getParity(currentPlayer) ? 1 : 0);//Bool
		double score = 0;
		switch(depth)
		{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				score =   1.0 * scorePieceDifference
						+ 1.0 * scoreCornerOccupacy
						+ 1.0 * scoreCornerCloseness
						+ 1.0 * scoreMobilityScore
						+ 1.0 * scoreFrontierDiscs
						+ 1.0 * scoreDiscSquare
						+ 1.0 * scoreIrreversiblePiece
						+ 1.0 * scoreParity;
			break;
		}
		
		/*System.out.println("Player " + currentPlayer);
		System.out.println("PD " + scorePieceDifference);
		System.out.println("CO " + scoreCornerOccupacy);
		System.out.println("CC " + scoreCornerCloseness);
		System.out.println("MS " + scoreMobilityScore);
		System.out.println("FD " + scoreFrontierDiscs);
		System.out.println("DS " + scoreDiscSquare);
		System.out.println("IP " + scoreIrreversiblePiece);
		System.out.println("P " + scoreParity);*/
		
		return score;
		}
}
