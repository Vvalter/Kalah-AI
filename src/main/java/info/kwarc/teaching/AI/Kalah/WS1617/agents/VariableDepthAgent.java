package info.kwarc.teaching.AI.Kalah.WS1617.agents;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.RandomPlayer;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class VariableDepthAgent extends StandardAgent {
    private String name = null;
    public VariableDepthAgent(int cutoffDepth, int stepSize, String name) {
        this(cutoffDepth, stepSize);
        this.name = name;
    }
    public VariableDepthAgent(int cutoffDepth, int stepSize) {
        this.cutoffDepth = cutoffDepth;
        this.stepSize = stepSize;
    }
    @Override
    public String name() {
        if (name == null) {
            return "VariableDepthAgent";
        } else {
            return name;
        }
    }

    public static void main(String[] args) {
        Agent a = new VariableDepthAgent(10, 1);

        SuperAgentTest.playGame(a, new RandomPlayer("Random"), 6, 6);
    }
}
