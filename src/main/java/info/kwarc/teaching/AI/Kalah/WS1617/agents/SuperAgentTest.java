package info.kwarc.teaching.AI.Kalah.WS1617.agents;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Game;
import scala.Tuple2;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Simon Rainer on 12/8/16.
 */
public class SuperAgentTest {
    public static void main(String[] args) {
//        testAlphaBeta();
        Agent a = new StandardAgent();
        Agent b = new DepthOneAgent();

        Agent tmp = a;a = b; b = tmp;

        for (int i = 0; i < 1; i++) {
            battle(a, b);
        }

        System.out.println(scoresA);
        System.out.println(scoresB);

        int aWon = 0, bWon = 0, tie = 0;
        for (int i = 0; i < scoresA.size(); i++) {
            if (scoresA.get(i) == scoresB.get(i)) {
                tie ++;
            } else if (scoresA.get(i) > scoresB.get(i)) {
                aWon ++;
            } else {
                bWon ++;
            }
        }
        System.out.println(aWon + " + " + bWon + " ties: " + tie);
    }

    public static void evaluate(Agent a, Agent b) {
        evaluate(a, b, 1);
    }
    public static void evaluate(Agent firstAgent, Agent secondAgent, int n) {
        System.setOut(new PrintStream(new OutputStream() {
            @Override public void write(int b) {}
        }) {
            @Override public void flush() {}
            @Override public void close() {}
            @Override public void write(int b) {}
            @Override public void write(byte[] b) {}
            @Override public void write(byte[] buf, int off, int len) {}
            @Override public void print(boolean b) {}
            @Override public void print(char c) {}
            @Override public void print(int i) {}
            @Override public void print(long l) {}
            @Override public void print(float f) {}
            @Override public void print(double d) {}
            @Override public void print(char[] s) {}
            @Override public void print(String s) {}
            @Override public void print(Object obj) {}
            @Override public void println() {}
            @Override public void println(boolean x) {}
            @Override public void println(char x) {}
            @Override public void println(int x) {}
            @Override public void println(long x) {}
            @Override public void println(float x) {}
            @Override public void println(double x) {}
            @Override public void println(char[] x) {}
            @Override public void println(String x) {}
            @Override public void println(Object x) {}
            @Override public PrintStream printf(String format, Object... args) { return this; }
            @Override public PrintStream printf(Locale l, String format, Object... args) { return this; }
            @Override public PrintStream format(String format, Object... args) { return this; }
            @Override public PrintStream format(Locale l, String format, Object... args) { return this; }
            @Override public PrintStream append(CharSequence csq) { return this; }
            @Override public PrintStream append(CharSequence csq, int start, int end) { return this; }
            @Override public PrintStream append(char c) { return this; }
        });
        System.err.println("------------------------------------------------------------------------------");
        System.err.println("Evaluation agents: " + firstAgent + " and " + secondAgent);
        List<Result> allResults = new ArrayList<>();
        for (int houses = 3; houses <= 8; houses++) {
            for (int seeds = 1; seeds <= houses; seeds++) {
                System.err.println("Doing " + n*2 + " runs with (" + houses + ", " + seeds + ")");

                List<Result> results = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    results.add(playGame(firstAgent, secondAgent, houses, seeds));
                }
                allResults.addAll(results);

                long numVictoriesA = results.stream().filter(r -> r.getScoreA() > r.getScoreB()).count();
                long numVictoriesB = results.stream().filter(r -> r.getScoreA() < r.getScoreB()).count();
                long numDraws = results.stream().filter(r -> r.getScoreA() == r.getScoreB()).count();

                results = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    results.add(playGame(secondAgent, firstAgent, houses, seeds));
                }
                allResults.addAll(results);

                numVictoriesA += results.stream().filter(r -> r.getScoreA() < r.getScoreB()).count();
                numVictoriesB += results.stream().filter(r -> r.getScoreA() > r.getScoreB()).count();
                numDraws += results.stream().filter(r -> r.getScoreA() == r.getScoreB()).count();

                System.err.println("Results: " + numVictoriesA + ":" + numVictoriesB + ":" + numDraws);
            }
        }
        long numVictoriesA = allResults.stream().filter(r -> r.getScoreA() > r.getScoreB()).count();
        long numVictoriesB = allResults.stream().filter(r -> r.getScoreA() < r.getScoreB()).count();
        long numDraws = allResults.stream().filter(r -> r.getScoreA() == r.getScoreB()).count();
        System.err.println("Results: " + numVictoriesA + ":" + numVictoriesB + ":" + numDraws);
        System.err.println("------------------------------------------------------------------------------");
    }

    public static class Result {
        private int scoreA;
        private int scoreB;

        public int getScoreA() {
            return scoreA;
        }

        public int getScoreB() {
            return scoreB;
        }


        public Result(Tuple2<Object, Object> scores) {
            scoreA = (int) scores._1();
            scoreB = (int) scores._2();
        }

        public String toString() {
            return scoreA + ":" + scoreB;
        }
    }

    public static Result playGame(Agent a, Agent b, int houses, int seeds) {
        Game g = new Game(a, b, houses, seeds);
        Result res = new Result(g.play(false));
        return res;
    }
    private static ArrayList<Integer> scoresA = new ArrayList<>(), scoresB = new ArrayList<>();
    @Deprecated
    private static void battle(Agent a, Agent b) {
        Game g = new Game(a, b, 6, 6);
        Tuple2<Object, Object> scores = g.play(true);
        System.out.println(scores._1() + " : " + scores._2());

        scoresA.add((Integer) scores._1());
        scoresB.add((Integer) scores._2());
    }

    private static void testAlphaBeta() {
        for (int n = 1; n < 4; n++) {
            for (int k = 1; k < 4; k++) {
                int len = 2*n+2;
                for (int num = 0; num < Math.pow(k, len); num ++) {
                    int board[] = new int[len];
                    int numCopy = num;
                    for (int i = 0; i < len; i++) {
                        board[i] = numCopy % k;
                        numCopy /= k;
                    }
//                    for (int i = len-1; i >= 0; i--) {
//                        System.out.print(board[i] + " ");
//                    }
//                    System.out.println();
//                    assert
//                            SuperAgent.getMaxUtility(board, 1000, Integer.MIN_VALUE, Integer.MAX_VALUE)
//                                    ==
//                            SuperAgentWithoutAlphaBeta.getMaxUtility(board, 1000);
                }
            }
        }
    }
}
