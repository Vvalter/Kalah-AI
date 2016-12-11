package info.kwarc.teaching.AI.Kalah.WS1617.agents;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Game;
import info.kwarc.teaching.AI.Kalah.HumanPlayer;
import info.kwarc.teaching.AI.Kalah.RandomPlayer;
import scala.Tuple2;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Simon Rainer on 12/8/16.
 */
public class SuperAgentTest {
    public static void main(String[] args) {
//        TrollBot a = new TrollBot();
//        SuperAgent b = new VariableDepthAgent(50, 3, "player2");
        SuperAgent a = new SmartAgent(50, 1, "No sorting");
        SuperAgent b = new SmartAgent(50, 1, "Choosing max");
//        a.sortAllNextMoves = true;
        a.onlyChooseMax = true;
        b.sortAllNextMoves = true;
//        b.onlyChooseMax = true;

//        Agent a = new HumanPlayer("first");
//        Agent b = new HumanPlayer("second");
        playGame(a, b, 6, 6);
    }

    public static void evaluate(Agent a, Agent b) {
        evaluate(a, b, 1);
    }

    private static void squelchStdout() {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
        }) {
            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }

            @Override
            public void write(int b) {
            }

            @Override
            public void write(byte[] b) {
            }

            @Override
            public void write(byte[] buf, int off, int len) {
            }

            @Override
            public void print(boolean b) {
            }

            @Override
            public void print(char c) {
            }

            @Override
            public void print(int i) {
            }

            @Override
            public void print(long l) {
            }

            @Override
            public void print(float f) {
            }

            @Override
            public void print(double d) {
            }

            @Override
            public void print(char[] s) {
            }

            @Override
            public void print(String s) {
            }

            @Override
            public void print(Object obj) {
            }

            @Override
            public void println() {
            }

            @Override
            public void println(boolean x) {
            }

            @Override
            public void println(char x) {
            }

            @Override
            public void println(int x) {
            }

            @Override
            public void println(long x) {
            }

            @Override
            public void println(float x) {
            }

            @Override
            public void println(double x) {
            }

            @Override
            public void println(char[] x) {
            }

            @Override
            public void println(String x) {
            }

            @Override
            public void println(Object x) {
            }

            @Override
            public PrintStream printf(String format, Object... args) {
                return this;
            }

            @Override
            public PrintStream printf(Locale l, String format, Object... args) {
                return this;
            }

            @Override
            public PrintStream format(String format, Object... args) {
                return this;
            }

            @Override
            public PrintStream format(Locale l, String format, Object... args) {
                return this;
            }

            @Override
            public PrintStream append(CharSequence csq) {
                return this;
            }

            @Override
            public PrintStream append(CharSequence csq, int start, int end) {
                return this;
            }

            @Override
            public PrintStream append(char c) {
                return this;
            }
        });

    }
    public static void evaluate(Agent firstAgent, Agent secondAgent, int n) {
        squelchStdout();
        System.err.println("------------------------------------------------------------------------------");
        System.err.println("Evaluation agents: " + firstAgent + " and " + secondAgent);
        List<Result> allResults = new ArrayList<>();
        for (int houses = 3; houses <= 8; houses++) {
            for (int seeds = 1; seeds <= houses; seeds++) {
                System.err.println("Doing " + n * 2 + " runs with (" + houses + ", " + seeds + ")");

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
        Result res = new Result(g.play(true));
        return res;
    }

    private static ArrayList<Integer> scoresA = new ArrayList<>(), scoresB = new ArrayList<>();

    @Deprecated
    private static void battle(Agent a, Agent b) {
        Game g = new Game(a, b, 5, 5);
        Tuple2<Object, Object> scores = g.play(true);
        System.out.println(scores._1() + " : " + scores._2());

        scoresA.add((Integer) scores._1());
        scoresB.add((Integer) scores._2());
    }

    public static boolean isOptimal(Agent a, int trials) {
        squelchStdout();

        class Configuration {
            private int houses, seeds;

            public Configuration(int houses, int seeds) {
                this.houses = houses;
                this.seeds = seeds;
            }

            public int getSeeds() {
                return seeds;
            }

            public void setSeeds(int seeds) {
                this.seeds = seeds;
            }

            public int getHouses() {
                return houses;
            }

            public void setHouses(int houses) {
                this.houses = houses;
            }
        }

        Configuration draws[] = {
                new Configuration(1, 1), new Configuration(1, 6),
                new Configuration(3, 1),
                new Configuration(5, 1),
        };

        Configuration wins[] = {
                new Configuration(1, 3), new Configuration(1, 5),
                new Configuration(2, 1), new Configuration(2, 5), new Configuration(2, 6),
                new Configuration(3, 2), new Configuration(3, 3), new Configuration(3, 5),
                new Configuration(3, 5),
                new Configuration(4, 1), new Configuration(4, 2), new Configuration(4, 3),
        };
        Configuration looses[] = {
                new Configuration(1, 2), new Configuration(1, 4),
                new Configuration(2, 2), new Configuration(2, 3), new Configuration(2, 4),
                new Configuration(3, 6)
        };

        Agent b = new RandomPlayer("Random");
        Stream<Result> drawResults = Arrays.stream(draws).map(
                config -> IntStream.rangeClosed(0, trials).mapToObj(
                        i -> playGame(a, b, config.getHouses(), config.getSeeds())
                ).collect(Collectors.toList())
                ).flatMap(l -> l.stream());

        Stream<Result> winResults = Arrays.stream(wins).map(
                config -> IntStream.rangeClosed(0, trials).mapToObj(
                        i -> playGame(a, b, config.getHouses(), config.getSeeds())
                ).collect(Collectors.toList())
        ).flatMap(l -> l.stream());

        Stream<Result> loosesResults = Arrays.stream(looses).map(
                config -> IntStream.rangeClosed(0, trials).mapToObj(
                        i -> playGame(b, a, config.getHouses(), config.getSeeds())
                ).collect(Collectors.toList())
        ).flatMap(l -> l.stream());

        boolean alwaysDraw = drawResults.allMatch(result -> result.getScoreA() >= result.getScoreB());
        boolean alwaysWin = winResults.allMatch(result -> result.getScoreA() > result.getScoreB());
        boolean alwaysLoose = loosesResults.allMatch(result -> result.getScoreA() < result.getScoreB());

        boolean everything = alwaysDraw && alwaysWin && alwaysLoose;

        if (!everything) {
            System.err.println(alwaysDraw + " " + alwaysWin + " " + alwaysLoose);
        }
        return everything;
    }

    private static void testAlphaBeta() {
        for (int n = 1; n < 4; n++) {
            for (int k = 1; k < 4; k++) {
                int len = 2 * n + 2;
                for (int num = 0; num < Math.pow(k, len); num++) {
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
