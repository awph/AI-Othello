package Participants.AntogniniPerez;

public class Compute {

	public static final double INF = Double.MAX_VALUE;
	private int player;
	private int depth;

	private int f1;
	private int f2;
	private int f3;
	private int f4;
	
	private static final int[][] factors = 
		{
		{3,1,8,3}, // P1, level 1
		{4,1,6,6}, // P1, level 2
		{3,1,3,8}, // P1, level 3
		{5,1,8,10}, // P1, level 4
		{9,1,3,6}, // P1, level 5
		{8,7,5,6}, // P1, level 6
		{8,3,6,9}, // P1, level 7
		{9,10,6,4}, // P1, level 8
		{9,10,7,3}, // P1, level 9
		{6,1,2,8}, // P2, level 1
		{3,2,2,9}, // P2, level 2
		{6,5,6,9}, // P2, level 3
		{3,9,9,3}, // P2, level 4
		{10,10,1,1}, // P2, level 5
		{2,7,4,5}, // P2, level 6
		{9,8,1,7}, // P2, level 7
		{9,10,6,4}, // P2, level 8
		{9,10,7,3}, // P2, level 9
		};
	
	private int I;
	private int J;
	
	public Compute(int player, int depth)
	{
		this.player = player;
		this.depth = depth;
		initialize();
		chooseFactors();
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
	
	/**
	 * Apply the algorithm alpha-beta
	 * @param root : Current board
	 * @param depth : Depth
	 * @param minOrMax : If we have to maximize (1) or minimize (-1)
	 * @param parentValue : the alpha or beta of the parent
	 * @param currentPlayer : currentPlayer
	 * @param hasToPass : If the player MUST pass
	 * @return score and you can get the move with getI & getJ
	 */
	public double alphaBeta(Board root, int depth, int minOrMax, double parentValue, int currentPlayer, boolean hasToPass)
	{
		boolean isEndOfGame = root.isTheGameEnded();
		if(depth == 0 || isEndOfGame)
			return eval(root, isEndOfGame, hasToPass);
		
		double optVal = minOrMax * -INF;
		int optOpi = Board.DUMMY_VALUE, optOpj = Board.DUMMY_VALUE;
		
		int[] allPossibleMoves = new int[121];
		root.getAllPossibleMove(allPossibleMoves, currentPlayer);

		if(allPossibleMoves[0] == Board.DUMMY_VALUE)
			optVal = alphaBeta(root, depth-1, -minOrMax, optVal, -currentPlayer, true);
		else
		{
			for(int i = 0; allPossibleMoves[i] > Board.DUMMY_VALUE; i += 2)
			{
				Board newNode = new Board(root); 
				newNode.addPiece(allPossibleMoves[i], allPossibleMoves[i+1], currentPlayer);
				
				double val = alphaBeta(newNode, depth-1, -minOrMax, optVal, -currentPlayer, false);
				
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
	
	/**
	 * Compute a score for the current game
	 * @param root : Current board
	 * @param isEndOfGame : If the game is finished
	 * @param hasToPass : If the played has to pass
	 * @return A score
	 */
	private double eval(Board root, boolean isEndOfGame, boolean hasToPass) 
	{
		if(isEndOfGame) return (root.getPieceDifference(player) > 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		double score = 0.0;
		double scoreParity = root.getParityScore(player);
		double scoreMobility = root.getMobilityScore(player);
		double scorePlace = root.getPlaceScore(player);
		double scoreStability = root.getStabilityScore(player);
		int ithMove = root.getIthMove();
		
		if(ithMove < Board.END_BEGIN_GAME)
		{
			score += f1 * 5 * scoreParity +
					 f2 * 3 * scoreMobility +
					 f3 * (scorePlace / 10) +
					 f4 *scoreStability;
		}
		else
		{
			double ratio = ithMove / 64;

			score += f1 * 5 * scoreParity * (1 - ratio) +
					 f2 * 2 * scoreMobility *  (1 - ratio) +
					 f3 * (scorePlace / 10) * (1 - ratio)+
					 f4 * scoreStability * ratio;
		}		
		
		if(hasToPass)
			score -= 500;
		
		return score;
	}
	
	private void chooseFactors()
	{
		int index = -1 + depth + ((player > 0 ) ? 9 : 0);
		f1 =  Compute.factors[index][0];
		f2 =  Compute.factors[index][1];
		f3 =  Compute.factors[index][2];
		f4 =  Compute.factors[index][3];
	}
}
