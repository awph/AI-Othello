package Participants.AntogniniPerez;

import Othello.Move;

public class Compute {

	public static final double INF = Integer.MAX_VALUE;
	
	//Maximize -> minOrMax = 1, otherwhise -1 (minimize)
	public static Object[] alphaBeta(Board root, int depth, int minOrMax, double parentValue, int currentPlayer)
	{
		if(depth == 0 || root.isTheGameEnded())
			return new Object[] { eval(root, currentPlayer), null };
		
		double optVal = minOrMax * -INF;
		Move optOp = null;
		int[] allPossibleMoves = new int[121];
		
		root.getAllPossibleMove(allPossibleMoves, currentPlayer);
		
		for(int i = 0; allPossibleMoves[i] > Board.DUMMY_VALUE; i += 2)
		{
			Board newNode = new Board(root);
			newNode.addPiece(allPossibleMoves[i], allPossibleMoves[i+1], currentPlayer);
			
			double val = (double) alphaBeta(newNode, depth-1, -minOrMax, optVal, -currentPlayer)[0];
			
			if(val*minOrMax > optVal*minOrMax)
			{
				optVal = val;
				optOp = new Move(allPossibleMoves[i+1], allPossibleMoves[i]);//col then row
				if(optVal*minOrMax > parentValue*minOrMax)
					break;
			}
		}
		//System.out.println(optOp.j + ", " + optOp.i);
		return new Object[] {optVal, optOp};
	}
	
	private static double eval(Board root, int currentPlayer) 
	{
        return (Math.random()*(10)*root.getFinalPoints(currentPlayer));
	}
}
