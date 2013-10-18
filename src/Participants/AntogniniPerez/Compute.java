package Participants.AntogniniPerez;

import java.util.Arrays;

import Othello.Move;

public class Compute {

	/**
	 * 
	 * @param root
	 * @param depth
	 * @param minOrMax
	 *            : 1 -> maximize, -1 -> minimize
	 * @param parentValue
	 * @return
	 */
	public static Object[] alphabeta(Board root, int depth, int minOrMax, int parentValue) {
		Piece currentPlayer = (minOrMax == 1) ? root.getPlayer() : (root.getPlayer() == Piece.Blue) ? Piece.Red : Piece.Blue;
		if (depth == 0 || root.isFinished())
			return new Object[] { eval(root, currentPlayer), null };
		int optimalValue = (minOrMax == 1) ? _INF : INF;
		Move optMove = null;
		int[] moves = new int[121];
		root.getAllPossibleMove(moves, root.getPlayer());
		for (int i = 0; moves[i] > Board.DUMMY_VALUE; i += 2) {
			Board newNode = new Board(root);
			newNode.addPiece(moves[i], moves[i + 1], currentPlayer);
			Object[] result = alphabeta(newNode, depth - 1, -minOrMax, optimalValue);
			if ((int) result[0] * minOrMax > optimalValue * minOrMax) {
				optimalValue = (int) result[0];
				optMove = new Move(moves[i], moves[i + 1]);
				if (optimalValue * minOrMax > parentValue * minOrMax)
					break;
			}

		}
		return new Object[] { new Integer(optimalValue), optMove};
	}

	private static Integer eval(Board root, Piece currentPlayer) {
		Integer value = 0;
		value += root.getFinalPoints(currentPlayer);
		return value;
	}

	public static final int INF = Integer.MAX_VALUE;
	public static final int _INF = Integer.MIN_VALUE;
}
