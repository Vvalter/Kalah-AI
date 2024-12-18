package info.kwarc.teaching.AI.Kalah.WS1617.evaluation;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.RandomPlayer;
import info.kwarc.teaching.AI.Kalah.WS1617.agents.StandardAgent;
import info.kwarc.teaching.AI.Kalah.WS1617.agents.SuperAgent;
import info.kwarc.teaching.AI.Kalah.WS1617.agents.VariableDepthAgent;
import scala.Tuple4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class Profiler {
    public static void main(String[] args) {
        int[] board = {
//                6,6,6,6,6,6,0,
//                6,6,6,6,6,6,0
//                4,4,4,4,0,
//                4,4,4,4,0
//                4, 4, 4, 0,
//                4, 4, 4, 0
                3,3,3,3,3,3,0,
                3,3,3,3,3,3,0 // -> 6
//                4,4,4,3,3,0,1,
//                4,4,3,0,4,0,2 // -> 5
//                0,4,4,3,0,1,2,
//                0,5,4,1,5,0,7 // -> 4
//                0,4,4,0,1,2,3,
//                0,5,4,1,5,0,7 // -> 3
//                0,4,0,1,2,3,4,
//                0,5,4,1,5,0,7 // -> 3
//                0,4,0,1,0,4,5,
//                0,5,4,1,5,0,7 // -> 5
//                1,1,2,2,2,5,11,
//                0,0,0,0,0,1,11 // -> 2

//            1,1,1,1,1,0,
//                1,1,1,1,1,0
        };
        for (int i = 0; i < 10; i++) {
//            System.err.println("No sorting");
//            SuperAgent a = new VariableDepthAgent(22, 21);
//            a.futility = true;
//            a.sortAllNextMoves = false;
//            a.onlyChooseMax = false;
//            doTestRun(a, board);
            System.err.println("Only max");
            SuperAgent c = new StandardAgent(24, 1, "Only max");
            c.futility = true;
            c.sortAllNextMoves = false;
            c.onlyChooseMax = true;
            doTestRun(c, board);
            System.err.println("full sorting");
            SuperAgent b = new StandardAgent(24, 1, "full sorting");
            b.futility = true;
            b.sortAllNextMoves = true;
            b.onlyChooseMax = false;
            doTestRun(b, board);
        }

        /*
        for (int i = 0; i < 3; i++) {
            SuperAgent a = new VariableDepthAgent(17, 1);
            a.sortAllNextMoves = true;
            System.err.println("sortAllNextMoves");
            doTestRun(a, board);
            a.sortAllNextMoves = false;
            System.err.println("no sortAllNextMoves");
            doTestRun(a, board);
        }
        */
    }

    private static void doTestRun(Agent a, int board[]) {
        long start = System.currentTimeMillis();
        Board stub = new BoardStub((board.length - 2) / 2, board[0], board, a, new RandomPlayer("RandomPlayer"));
        a.init(stub, true);
        a.move();
        System.err.format("Finished after %,d milliseconds\n", (System.currentTimeMillis() - start));
    }

    static class BoardStub extends Board {

        private int n;
        private int[] internalBoard;

        private Agent a, b;

        public BoardStub(int houses, int initSeeds, int[] board, Agent a, Agent b) {
            super(houses, initSeeds);

            this.n = houses;
            this.internalBoard = board;
            this.a = a;
            this.b = b;
        }

        @Override
        public Tuple4<Iterable<Object>, Iterable<Object>, Object, Object> getState() {
            Object storePlayer1 = internalBoard[n];
            Object storePlayer2 = internalBoard[2 * n + 1];
            return new Tuple4<>(getHouses(a), getHouses(b), storePlayer1, storePlayer2);
        }

        @Override
        public Iterable<Object> getHouses(Agent ag) {
            List<Object> res = new ArrayList<>();
            if (ag == a) {
                for (int i = 0; i < n; i++) {
                    res.add(internalBoard[i]);
                }
            } else {
                for (int i = n + 1; i < 2 * n + 1; i++) {
                    res.add(internalBoard[i]);
                }
            }
            return res;
        }

        @Override
        public int getSeed(int player, int house) {
            return 0;
        }

        @Override
        public int getScore(int player) {
            return 0;
        }

        @Override
        public String asString(Agent pl) {
            return null;
        }
    }
}
