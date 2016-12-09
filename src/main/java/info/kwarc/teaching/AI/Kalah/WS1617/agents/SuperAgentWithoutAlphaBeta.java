package info.kwarc.teaching.AI.Kalah.WS1617.agents;

/**
 * Created by Simon Rainer on 12/8/16.
 */
public abstract class SuperAgentWithoutAlphaBeta extends SuperAgent {
    public static void main(String[] args) {
        int board[] = {0, 0, 1, 7,
                       0, 0, 0, 10
        };
        int board2[] = {2, 0, 1, 7, 0, 1, 0, 7};
        int board3[] = {0, 1, 0, 7, 0, 1, 2, 7};

//        System.out.println(SuperAgent.getUtility(board));
//        System.out.println(SuperAgent.getMiniMaxMove(board2, 10000));
//        System.out.println(SuperAgent.getMiniMaxMove(board3, 10000));
    }

    @Override
    public int getMiniMaxMove(int[] board, int depth) {
        if (isFinished(board)) return getUtility(board);
        int n = (board.length - 2) / 2;
        int maxVal = Integer.MIN_VALUE;
        int maxMove = 0;

        for (int i = 0; i < n; i++) {
            if (board[i] > 0) {
                int[] newBoard = new int[board.length];
                boolean again = makeMove(n, board, i, newBoard);
                int utility;
                if (again) {
                    utility = getMaxUtility(newBoard, depth - 1);
                } else {
                    utility = getMinUtility(newBoard, depth - 1);
                }
                if (utility > maxVal) {
                    maxVal = utility;
                    maxMove = i;
                }
            }
        }
        return maxMove;
    }

    public int getMaxUtility(int[] board, int depth) {
        if (isFinished(board)) return getUtility(board);
        if (depth == 0) return getHeuristic(board);

        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (board[i] > 0) {
                int[] newBoard = new int[board.length];
                boolean again = makeMove(n, board, i, newBoard);
                int utility;
//                if (Math.random() < 0.5) {
                if (again) {
                    utility = getMaxUtility(newBoard, depth - 1);
                } else {
                    utility = getMinUtility(newBoard, depth - 1);
                }
                if (utility > maxVal) {
                    maxVal = utility;
                }
            }
        }
        if (maxVal == Integer.MIN_VALUE) {
            return getUtility(board);
        } else {
            return maxVal;
        }
    }

    public int getMinUtility(int[] board, int depth) {
        if (isFinished(board)) return getUtility(board);
        if (depth == 0) return getHeuristic(board);

        int minValue = Integer.MAX_VALUE;
        for (int i = n+1; i < 2*n+1; i++) {
            if (board[i] > 0) {
                int[] newBoard = new int[board.length];
                boolean again = makeMove(n, board, i, newBoard);
                int utility;
                if (again) {
                    utility = getMinUtility(newBoard, depth - 1);
                } else {
                    utility = getMaxUtility(newBoard, depth - 1);
                }
                if (utility < minValue) {
                    minValue = utility;
                }
            }
        }
        if (minValue == Integer.MAX_VALUE) {
            return getUtility(board);
        } else {
            return minValue;
        }
    }
}
