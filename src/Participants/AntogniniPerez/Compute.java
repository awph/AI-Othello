package Participants.AntogniniPerez;

import java.util.Arrays;

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
		
		return new Object[] {optVal, optOp};
	}
	
	private static double eval(Board root, int currentPlayer) 
	{
		double scoreAtEndOfGameCurrentPlayer = root.getScoreAtEndOfGame(currentPlayer);
		//double scoreAtEndOfGameOppositePlayer = 64 - scoreAtEndOfGameCurrentPlayer;
		
		double positionScoreCurrentPlayer = root.getPositionScore(currentPlayer);
		double positionScoreOppositePlayer = root.getPositionScore(-currentPlayer);
		
		double mobilityScoreCurrentPlayer = root.getMobilityScore(currentPlayer);
		double mobilityScoreOppositePlayer = root.getMobilityScore(-currentPlayer);
		
		double irreverisblePiecesCurrentPlayer = root.getNbIrreversiblePiece(currentPlayer);
		double irreverisblePiecesOppositePlayer = root.getNbIrreversiblePiece(-currentPlayer);
		
		//Find a better way, this sucks
        return 1.5*scoreAtEndOfGameCurrentPlayer + 2.5*(positionScoreCurrentPlayer-positionScoreOppositePlayer) + 4.5*mobilityScoreCurrentPlayer*10/mobilityScoreOppositePlayer + 5*(irreverisblePiecesCurrentPlayer-irreverisblePiecesOppositePlayer);
	}
}
