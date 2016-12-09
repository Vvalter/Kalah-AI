package info.kwarc.teaching.AI.Kalah.WS1617.agents;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class StandardAgent extends SuperAgent{

    @Override
    public int getHeuristic(short[] board) {
        if (board[n] - board[2*n+1] > numSeedsDividedByTwo) {
            return Integer.MAX_VALUE-1;
        }
        return board[n] - board[2 * n + 1];
    }
}
