package info.kwarc.teaching.AI.Kalah.WS1617.agents;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class StandardAgent extends VariableDepthAgent {

    public StandardAgent(int cutoffDepth, int stepSize, String name) {
        super(cutoffDepth, stepSize, name);
    }

    @Override
    public final int getHeuristic(short[] board) {
        return board[n] - board[2 * n + 1];
    }

    public static void main(String[] args) {
        SuperAgentTest.isOptimal(new StandardAgent(10000, 1, "StandardAgent"), 80);
    }
}
