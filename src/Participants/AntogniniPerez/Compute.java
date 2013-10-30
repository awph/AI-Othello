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
		{7,1,9,3}, // P1, level 1
		{3,5,5,6}, // P1, level 2
		{3,1,3,8}, // P1, level 3
		{10,1,8,10}, // P1, level 4
		{3,1,3,1}, // P1, level 5
		{6,4,3,6}, // P1, level 6
		{8,3,6,9}, // P1, level 7
		{9,10,6,4}, // P1, level 8
		{9,10,6,4}, // P1, level 9
		{1,1,1,1}, // P2, level 1
		{1,1,1,1}, // P2, level 2
		{1,1,1,1}, // P2, level 3
		{1,1,1,1}, // P2, level 4
		{1,1,1,1}, // P2, level 5
		{1,1,1,1}, // P2, level 6
		{1,1,1,1}, // P2, level 7
		{1,1,1,1}, // P2, level 8
		{1,1,1,1}, // P2, level 9
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
	
	//Maximize -> minOrMax = 1, otherwhise -1 (minimize)
	//Return score
	public double alphaBeta(Board root, int depth, int minOrMax, double parentValue, int currentPlayer, boolean hasToPass)
	{
		boolean isEndOfGame = root.isTheGameEnded();
		if(depth == 0 || isEndOfGame)
			return eval(root, this.player, this.depth, isEndOfGame, hasToPass);
		
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
	
	private double eval(Board root, int currentPlayer,int depth, boolean isEndOfGame, boolean hasToPass) 
	{
		double score = 0.0;
		double scoreParity = root.getParityScore(currentPlayer);
		double scoreMobility = root.getMobilityScore(currentPlayer);
		double scorePlace = root.getPlaceScore(currentPlayer);
		double scoreStability = root.getStabilityScore(currentPlayer);
		int ithMove = root.getIthMove();
		
		if(isEndOfGame)
			score = (root.getPieceDifference(currentPlayer) > 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		else if(ithMove < 13)
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
		f1 =  this.factors[index][0];
		f2 =  this.factors[index][1];
		f3 =  this.factors[index][2];
		f4 =  this.factors[index][3];
	}
}
