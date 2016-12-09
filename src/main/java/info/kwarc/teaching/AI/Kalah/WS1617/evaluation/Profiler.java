package info.kwarc.teaching.AI.Kalah.WS1617.evaluation;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.RandomPlayer;
import info.kwarc.teaching.AI.Kalah.WS1617.agents.StandardAgent;
import info.kwarc.teaching.AI.Kalah.WS1617.agents.SuperAgent;
import scala.Tuple4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Rainer on 12/9/16.
 */
public class Profiler {
    public static void main(String[] args) {
        int[] board = {
                6,6,6,6,6,6,0,
                6,6,6,6,6,6,0
        };
        SuperAgent a = new StandardAgent();
        doTestRun(a, board);
        a.timeoutMove();
    }

    private static void doTestRun(Agent a, int board[]) {
        Board stub = new BoardStub((board.length-2)/2, board[0], board, a, new RandomPlayer("RandomPlayer"));
        a.init(stub, true);
        a.move();
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
            Object storePlayer2 = internalBoard[2*n+1];
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
