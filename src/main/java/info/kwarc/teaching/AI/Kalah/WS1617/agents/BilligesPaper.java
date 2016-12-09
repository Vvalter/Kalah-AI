package info.kwarc.teaching.AI.Kalah.WS1617.agents;

import info.kwarc.teaching.AI.Kalah.Agent;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class BilligesPaper extends SuperAgent {
    @Override
    public int getHeuristic(short[] board) {
        int res = (board[n] - board[2*n+1]) * 4 ;
        for (int i = 0; i < n; i++) {
            if (board[i] == 2 * n + 1) {
                res += 4;
            }
            if (board[i] == n - i) {
                res += 2;
            }
            if (board[i] == 0) {
                res ++;
            }
        }
        return res;
    }

    @Override
    public String name() {
        return "BilligesPaper";
    }

    public static void main(String[] args) {
        Agent a = new StandardAgent();
        Agent b = new BilligesPaper();

        SuperAgentTest.evaluate(a, b);
    }
    /*
- Counter in players' store: 4
- Hole with 13 counters: 4
- Hole with an exact number of counters to receive another move: 2
- Empty hole on current players side: 1
     */

}
