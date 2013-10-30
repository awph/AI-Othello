package Participants.AntogniniPerez;

public class Compute {

	public static final double INF = Double.MAX_VALUE;
	
	private int I;
	private int J;
	
	public Compute()
	{
		initialize();
	}
	
	public void initialize()
	{
		I = J = Board.DUMMY_VALUE;
	}
	
	public int getI()
	{
		return I;
	}
	
	public int getJ()
	{
		return J;
	}
	
	//Maximize -> minOrMax = 1, otherwhise -1 (minimize)
	//Return score
	public double alphaBeta(Board root, int depth, int minOrMax, double parentValue, int currentPlayer)
	{
		boolean isEndOfGame = root.isTheGameEnded();
		if(depth == 0 || isEndOfGame)
			return eval(root, currentPlayer, depth, isEndOfGame);
		
		double optVal = minOrMax * -INF;
		int optOpi = Board.DUMMY_VALUE, optOpj = Board.DUMMY_VALUE;
		
		int[] allPossibleMoves = new int[121];
		root.getAllPossibleMove(allPossibleMoves, currentPlayer);

		if(allPossibleMoves[0] == Board.DUMMY_VALUE)
			optVal = alphaBeta(root, depth-1, -minOrMax, optVal, -currentPlayer);
		else
		{
			for(int i = 0; allPossibleMoves[i] > Board.DUMMY_VALUE; i += 2)
			{
				Board newNode = new Board(root); 
				newNode.addPiece(allPossibleMoves[i], allPossibleMoves[i+1], currentPlayer);
				
				double val = alphaBeta(newNode, depth-1, -minOrMax, optVal, -currentPlayer);
				
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
		I = optOpi;
		J = optOpj;
		return optVal;
	}
	
	private static double eval(Board root, int currentPlayer,int depth, boolean isEndOfGame) 
	{
		double score = 0.0;
		currentPlayer = -1;
		double scoreParity = root.getParityScore(currentPlayer);
		double scoreMobility = root.getMobilityScore(currentPlayer);
		double scorePlace = root.getPlaceScore(currentPlayer);
		double scoreStability = root.getStabilityScore(currentPlayer);
		int ithMove = root.getIthMove();
		
		if(root.isTheGameEnded())
		{
			root.getPieceDifference(currentPlayer);
			int scorePiece = root.getTempMine() - root.getTempHis();
		
			score = (scorePiece > 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		}
		else if(ithMove < 13)
		{
			score += 15 * scoreParity +
					 3 * scoreMobility +
					 3 * (scorePlace / 10) +
					 scoreStability;
		}
		else
		{
			double ratio = ithMove / 64;

			score += 15 * scoreParity * (1 - ratio) +
					 2 * scoreMobility *  (1 - ratio) +
					 3 * (scorePlace / 10) * (1 - ratio)+
					 scoreStability * ratio;
		}		

		int[] allPossibleMoves = new int[121];
		root.getAllPossibleMove(allPossibleMoves, currentPlayer);

		if(allPossibleMoves[0] == Board.DUMMY_VALUE)
			score -= 500;
		
		return score;
	}
}
