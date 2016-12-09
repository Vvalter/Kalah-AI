package info.kwarc.teaching.AI.Kalah.WS1617.agents;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class DepthOneAgent extends StandardAgent {
    public DepthOneAgent() {
        cutoffDepth = 1;
    }
    @Override
    public String name() {
        return "DepthOneAgent";
    }
}
