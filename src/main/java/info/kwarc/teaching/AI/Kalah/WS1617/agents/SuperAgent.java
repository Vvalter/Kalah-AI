package info.kwarc.teaching.AI.Kalah.WS1617.agents;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Board;
import scala.Tuple4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Rehgar on 12/8/16.
 */
public abstract class SuperAgent extends Agent {
    private int exploredNodes;
    private int allExploredNodes;
    private int maxDepth;
    private Board board;
    protected int n, numSeedsDividedByTwo;
    protected int cutoffDepth = 30;
    private boolean playerOne;
    private int bestMove;

    @Override
    public void init(Board board, boolean playerOne) {
        System.out.println("init");

        this.board = board;
        this.playerOne = playerOne;
        this.n = board.houses();
        this.numSeedsDividedByTwo = this.n * ((int)board.getHouses(this).iterator().next());

        // TODO precompute openings or end games
    }

    @Override
    public int move() {
        allExploredNodes = 0;
        exploredNodes = 0;
        bestMove = 0;
        maxDepth = 0;
        long start = System.currentTimeMillis();
        for (int depth = 0; depth < cutoffDepth; depth++) {
            short[] state = boardToArray(board, playerOne);
            bestMove = getMiniMaxMove(state, depth) + 1;
            maxDepth = depth;
            allExploredNodes += exploredNodes;
            exploredNodes = 0;
        }
        return bestMove;
    }

    private static List<Object> iterableToList(Iterable<Object> iter) {
        List<Object> res = new ArrayList<>();
        iter.forEach(o -> res.add(o));
        return res;
    }

    public short[] boardToArray(Board b, boolean playerOne) {
        Tuple4<Iterable<Object>, Iterable<Object>, Object, Object> state = b.getState();

        List<Object> ourHouses;
        Short ourStore;
        List<Object> theirHouses;
        Short theirStore;
        if (playerOne) {
            ourHouses = iterableToList(state._1());
            ourStore = ((Integer) state._3()).shortValue();
            theirHouses = iterableToList(state._2());
            theirStore = ((Integer) state._4()).shortValue();
        } else {
            ourHouses = iterableToList(state._2());
            ourStore = ((Integer) state._4()).shortValue();
            theirHouses = iterableToList(state._1());
            theirStore = ((Short) state._3()).shortValue();
        }

        // 0..n-1 our houses
        // n our store
        // n+1..2*n their houses
        // 2*n+1 their store
        short res[] = new short[2 * n + 2];
        for (int i = 0; i < n; i++) {
            res[i] = ((Integer) ourHouses.get(i)).shortValue();
            res[i + n + 1] = ((Integer) theirHouses.get(i)).shortValue();
        }
        res[n] = ourStore;
        res[2 * n + 1] = theirStore;

        return res;
    }

    /**
     * Simulates a move
     *
     * @param board an array representing a board state
     * @param house the house that is chosen in a move.
     *              can be an enemy move too.
     * @return indicating if one is allowed to move again
     */
    public static boolean makeMove(int n, short[] board, int house, short[] new_board) {
        assert house != n && house != 2 * n + 1 : "house is a store";
        assert house >= 0 && house <= 2 * n : "house is too big or too small";

        for (int i = 0; i < board.length; i++) {
            new_board[i] = board[i];
        }

        int numSeeds = board[house];
        assert numSeeds != 0 : "Requested an illegal move";

        int currentPosition = house;
        boolean playerOne;
        int enemyStore;
        int ourStore;
        if (house < n) {
            playerOne = true;
            enemyStore = 2 * n + 1;
            ourStore = n;
        } else {
            playerOne = false;
            enemyStore = n;
            ourStore = 2 * n + 1;
        }

        new_board[house] = 0;
        for (int i = 0; i < numSeeds; i++) {
            currentPosition++;
            if (currentPosition == enemyStore) {
                currentPosition++;
            }
            if (currentPosition > 2 * n + 1) {
                currentPosition = 0; // TODO optimize ?
            }
            new_board[currentPosition]++;
        }

        // If we have placed into an emtpy field at our side as the last action
        // (x-1)/n is 1 if x is on enemy side otherwise 0
        if (new_board[currentPosition] == 1) {
            if ((playerOne && currentPosition < n) ||
                    (!playerOne && currentPosition > n && currentPosition != 2 * n + 1)) {
                // We can take this field and the opposite in our store
                // The opposite side can be computed by 2*n-currentPosition
                // TODO simplify
//            if (new_board[2*n-currentPosition] != 0) {
                new_board[ourStore] += 1 + new_board[2 * n - currentPosition];
                new_board[2 * n - currentPosition] = 0;
                new_board[currentPosition] = 0;
//            }
            }
        }

        // You are allowed to take another turn if you end your turn by placing into your store
        return currentPosition == ourStore;
    }

    public boolean isFinished(short[] board) {
        boolean firstCase = true, secondCase = true;
        for (int i = 0; i < n; i++) {
            if (board[i] != 0) {
                firstCase = false;
            }
            if (board[i + n + 1] != 0) {
                secondCase = false;
            }
            if (!(firstCase || secondCase)) {
                return false;
            }
        }
        return true;
    }

    public int getUtility(short[] board) {
        assert isFinished(board);
        int ours = board[n];
        int theirs = board[2 * n + 1];

        for (int i = 0; i < n; i++) {
            ours += board[i];
            theirs += board[i + n + 1];
        }

        if (ours > theirs) {
            return Integer.MAX_VALUE - 1;
        } else if (ours < theirs) {
            return Integer.MIN_VALUE + 1;
        } else {
            return 0;
        }
    }

    public abstract int getHeuristic(short[] board);

    public int getMiniMaxMove(short[] board, int depth) {
        if (isFinished(board)) return getUtility(board);
        int maxVal = Integer.MIN_VALUE;
        int maxMove = 0;

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        short[] newBoard = new short[board.length];
        for (int i = 0; i < n; i++) {
            if (board[i] > 0) {
                boolean again = makeMove(n, board, i, newBoard);
                int utility;
                if (again) {
                    utility = getMaxUtility(newBoard, depth - 1, alpha, beta);
                } else {
                    utility = getMinUtility(newBoard, depth - 1, alpha, beta);
                }
                if (utility > alpha) {
                    alpha = utility;
                }
                if (utility > maxVal) {
                    maxVal = utility;
                    maxMove = i;
                }
                if (utility >= beta) {
                    break;
                }
            }
        }

        /*
        System.out.println("-------------------------------------------------");
        System.out.println("Board:");
        for (int i = 0; i < board.length; i++) {
            System.out.print(board[i] + " ");
        }
        System.out.println();
        System.out.println("Max utiltiy: " + maxVal);
        System.out.println("-------------------------------------------------");
        */
        return maxMove;
    }

    public int getMaxUtility(short[] board, int depth, int alpha, int beta) {
        exploredNodes ++;
        if (isFinished(board)) return getUtility(board);
        if (depth <= 0) return getHeuristic(board);

        int maxVal = Integer.MIN_VALUE;
        short[] newBoard = new short[board.length];
        for (int i = 0; i < n; i++) {
            if (board[i] > 0) {
                boolean again = makeMove(n, board, i, newBoard);
                int utility;
                if (again) {
                    utility = getMaxUtility(newBoard, depth - 1, alpha, beta);
                } else {
                    utility = getMinUtility(newBoard, depth - 1, alpha, beta);
                }
                if (utility > maxVal) {
                    maxVal = utility;
                }
                if (utility > alpha) {
                    alpha = utility;
                }
                if (utility >= beta) {
                    break;
                }
            }
        }
        if (maxVal == Integer.MIN_VALUE) {
            return getUtility(board);
        } else {
            return maxVal;
        }
    }

    public int getMinUtility(short[] board, int depth, int alpha, int beta) {
        exploredNodes ++;
        if (isFinished(board)) return getUtility(board);
        if (depth <= 0) return getHeuristic(board);

        int minValue = Integer.MAX_VALUE;
        short[] newBoard = new short[board.length];
        for (int i = n + 1; i < 2 * n + 1; i++) {
            if (board[i] > 0) {
                boolean again = makeMove(n, board, i, newBoard);
                int utility;
                if (again) {
                    utility = getMinUtility(newBoard, depth - 1, alpha, beta);
                } else {
                    utility = getMaxUtility(newBoard, depth - 1, alpha, beta);
                }
                if (utility < minValue) {
                    minValue = utility;
                }
                if (utility < beta) {
                    beta = utility;
                }
                if (utility <= alpha) {
                    break;
                }
            }
        }
        if (minValue == Integer.MAX_VALUE) {
            return getUtility(board);
        } else {
            return minValue;
        }
    }

    @Override
    public String name() {
        return "TinyRick";
    }

    @Override
    public Iterable<String> students() {
        return students;
    }

    @Override
    public int timeoutMove() {
        System.err.format("timeoutMove: %d after depth: %d explored nodes: %,d/%,d\n", bestMove, maxDepth, exploredNodes, allExploredNodes+exploredNodes);
        return bestMove;
    }

    public static void main(String[] args) {
        int board[] = {2, 0, 1, 7, 0, 1, 0, 7};

        int newBoard[] = new int[board.length];
        //System.out.println(SuperAgent.getMiniMaxMove(board, 10000));
    }

    private static List<String> students;

    static {
        students = new ArrayList<>();
        students.add("Joerg Planner");
        students.add("Simon Rainer");
        students.add("Paul Nickles");
    }

}
