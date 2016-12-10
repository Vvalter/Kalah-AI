package info.kwarc.teaching.AI.Kalah.WS1617.agents;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class StandardAgent extends SuperAgent{

    @Override
    public int getHeuristic(short[] board) {
        return board[n] - board[2 * n + 1];
    }

    public static void main(String[] args) {
        SuperAgentTest.isOptimal(new StandardAgent(), 80);
    }
}
