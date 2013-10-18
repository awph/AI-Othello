package Participants.AntogniniPerez;

import Othello.Move;

public class Compute {

	public static final int INF = Integer.MAX_VALUE;
	
	//Maximize -> minOrMax = 1, otherwhise -1 (minimize)
	public static Object[] alphaBeta(Board root, int depth, int minOrMax, int parentValue, int currentPlayer)
	{
		if(depth == 0 || root.isTheGameEnded())
			return new Object[] { eval(root, currentPlayer), null };
		
		int optVal = minOrMax * -INF;
		Move optOp = null;
		int[] allPossibleMoves = new int[121];
		
		root.getAllPossibleMove(allPossibleMoves, currentPlayer);
		
		for(int i = 0; allPossibleMoves[i] != Board.DUMMY_VALUE; i += 2)
		{
			Board newNode = new Board(root);
			newNode.addPiece(allPossibleMoves[i], allPossibleMoves[i+1], currentPlayer);
			
			Object[] res = alphaBeta(newNode, depth-1, -minOrMax, optVal, -currentPlayer);
			
			if((int)res[0]*minOrMax > optVal*minOrMax)
			{
				optVal = (int)res[0];
				optOp = new Move(allPossibleMoves[i+1], allPossibleMoves[i]);//col then row
				if(optVal*minOrMax > parentValue*minOrMax)
					break;
			}
		}
		return new Object[] {optVal, optOp};
	}
	
	private static int eval(Board root, int currentPlayer) 
	{
        return root.getFinalPoints(currentPlayer);
	}
}
