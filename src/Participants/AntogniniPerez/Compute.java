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
		/*double scoreAtEndOfGameCurrentPlayer = root.getScoreAtEndOfGame(currentPlayer);
		//double scoreAtEndOfGameOppositePlayer = 64 - scoreAtEndOfGameCurrentPlayer;
		
		double positionScoreCurrentPlayer = root.getPositionScore(currentPlayer);
		double positionScoreOppositePlayer = root.getPositionScore(-currentPlayer);
		
		double mobilityScoreCurrentPlayer = root.getMobilityScore(currentPlayer);
		double mobilityScoreOppositePlayer = root.getMobilityScore(-currentPlayer);
		
		double irreverisblePiecesCurrentPlayer = root.getNbIrreversiblePiece(currentPlayer);
		double irreverisblePiecesOppositePlayer = root.getNbIrreversiblePiece(-currentPlayer);
*/
		//Find a better way, this sucks
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
				score = 10.0 * root.getPieceDifference() + 801.724*root.getCornerOccupacy() + 382.026*root.getCornerCloseness() + 78.922*root.getMobilityScore() + 74.396*root.getFrontierDiscs() + 10.0*root.getDiscSquares();
			break;
		}
		
		return score;
		}
}
