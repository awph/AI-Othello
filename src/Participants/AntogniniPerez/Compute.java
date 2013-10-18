package Participants.AntogniniPerez;

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
		if (depth == 0 || root.isFinished())
			return new Object[] { eval(root), null };
		int optimalValue = (minOrMax == 1) ? _INF : INF;
		Board nextNode = null;
		int[] moves = new int[121];
		Piece currentPlayer = (minOrMax == 1) ? root.getPlayer() : (root.getPlayer() == Piece.Blue) ? Piece.Red : Piece.Blue;
		root.getAllPossibleMove(moves, root.getPlayer());
		for (int i = 0; moves[i] > -1; i += 2) {
			Board newNode = new Board(root);
			newNode.addPiece(moves[i], moves[i + 1], currentPlayer);
			Object[] result = alphabeta(newNode, depth - 1, -minOrMax, optimalValue);
			if ((int) result[0] * minOrMax > optimalValue * minOrMax) {
				optimalValue = (int) result[0];
				nextNode = (Board) result[1];
				if (optimalValue * minOrMax > parentValue * minOrMax)
					break;
			}

		}
		return new Object[] { new Integer(optimalValue), nextNode };
	}

	private static Integer eval(Board root) {
		Integer value = 0;
		return value;
	}

	private static final int INF = Integer.MAX_VALUE;
	private static final int _INF = Integer.MIN_VALUE;
}
