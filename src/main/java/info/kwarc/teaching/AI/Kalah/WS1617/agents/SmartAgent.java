package info.kwarc.teaching.AI.Kalah.WS1617.agents;

/**
 * Created by vvalter on 11.12.16.
 */
public class SmartAgent extends VariableDepthAgent{
    public SmartAgent(int cutoffDepth, int stepSize, String name) {
        super(cutoffDepth, stepSize, name);
    }

    @Override
    public int getHeuristic(short[] board) {
        int res = 0;
        for (int i = 0; i <= n; i++) {
            res += board[i];
            res -= board[i+n+1];
        }
        return res + 2*board[n] - 2*board[2*n+1];
    }
}
