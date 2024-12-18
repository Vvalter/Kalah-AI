package info.kwarc.teaching.AI.Kalah.WS1617.agents;

import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Board;
import scala.Int;
import scala.Tuple4;

import java.util.ArrayList;
import java.util.Arrays;
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
    protected int cutoffDepth = 16;
    protected int stepSize = 1;
    private boolean playerOne;
    private int bestMove;
    private boolean finished;

    public boolean futility = true;
    public boolean both = true;
    public boolean sortAllNextMoves = false;
    public boolean onlyChooseMax = true;
    private int globalMaxVal;


    @Override
    public final void init(Board board, boolean playerOne) {
        this.board = board;
        this.playerOne = playerOne;
        this.n = board.houses();
        this.numSeedsDividedByTwo = (Integer) board.getState()._3() + (Integer) board.getState()._4();
        for (Object o : board.getState()._1()) {
            this.numSeedsDividedByTwo += (Integer) o;
        }
        for (Object o : board.getState()._2()) {
            this.numSeedsDividedByTwo += (Integer) o;
        }
        this.numSeedsDividedByTwo /= 2;

        // TODO precompute openings or end games
        // TODO should probably just call move() (and prevent the next move call from overwriting the result)
    }

    @Override
    public final int move() {
        allExploredNodes = 0;
        bestMove = 0;
        maxDepth = 0;
        finished = false;
        for (int depth = 0; !finished && depth <= cutoffDepth; depth += stepSize) {
            short[] state = boardToArray(board, playerOne);
            exploredNodes = 0;
            bestMove = getMiniMaxMove(state, depth) + 1;
            maxDepth = depth;
            allExploredNodes += exploredNodes;
        }
        if (finished) {
            if (globalMaxVal > 0) {
                System.err.println(this.name() + ": I will win!:)");
            } else if (globalMaxVal < 0) {
                System.err.println(this.name() + ": I will loose!:(");
            } else {
                System.err.println(this.name() + "Nobody will win:/");
            }
            System.err.println("Exhausted full search tree at depth: " + maxDepth);
        } else {
            System.err.println("Cut off after: " + maxDepth);
        }
        System.err.format("Evaluated: %,d nodes\n", allExploredNodes);
        System.err.println("Move: " + bestMove);
        return bestMove;
    }

    private static List<Object> iterableToList(Iterable<Object> iter) {
        List<Object> res = new ArrayList<>();
        iter.forEach(o -> res.add(o));
        return res;
    }

    public final short[] boardToArray(Board b, boolean playerOne) {
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
            theirStore = ((Integer) state._3()).shortValue();
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
    public final static boolean makeMove(int n, short[] board, int house, short[] new_board) {
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
//                if (new_board[2 * n - currentPosition] != 0) {
                new_board[ourStore] += 1 + new_board[2 * n - currentPosition];
                new_board[2 * n - currentPosition] = 0;
                new_board[currentPosition] = 0;
//                }
            }
        }

        // You are allowed to take another turn if you end your turn by placing into your store
        return currentPosition == ourStore;
    }

    public final boolean isFinished(short[] board) {
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

    public final int getUtility(short[] board) {
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
        if (maxVal == Integer.MAX_VALUE - 1) {
            finished = true;
        }

        globalMaxVal = maxVal;
        return maxMove;
    }

    public int getMaxUtility(short[] board, int depth, int alpha, int beta) {
        exploredNodes++;
        if (isFinished(board)) return getUtility(board);
        if (depth <= 0) return getHeuristic(board);

        if (futility) {
            if (board[n] > numSeedsDividedByTwo) {
                return Integer.MAX_VALUE - 1;
            }
            int lower = board[n] - numSeedsDividedByTwo;
            int upper = numSeedsDividedByTwo - board[2 * n + 1];

            if (lower > beta) {
                return beta;
            } else if (both && upper < alpha) {
                return alpha;
            }
        }

        int maxVal = Integer.MIN_VALUE;
        short allBoard[][] = new short[n][2 * n + 2];
        int start = 0, end = n - 1;
        for (int i = 0; i < n; i++) {
            if (board[i] > 0) {
                short[] tmp = new short[2 * n + 2];
                boolean again = makeMove(n, board, i, tmp);
                short[] newBoard;
                if (!again) {
                    newBoard = allBoard[end--];
                } else {
                    newBoard = allBoard[start++];
                }
                for (int j = 0; j < 2 * n + 2; j++) {
                    newBoard[j] = tmp[j];
                }
            }
        }

        if (sortAllNextMoves) {
            Arrays.sort(allBoard, 0, start, (a, b) -> getHeuristic(b) - getHeuristic(a));
            Arrays.sort(allBoard, end + 1, n, (a, b) -> getHeuristic(b) - getHeuristic(a));
        }
        if (onlyChooseMax) {
            int maxHeuristicAgain = Integer.MIN_VALUE, maxHeuristic = Integer.MIN_VALUE;
            int maxAgainIndex = -1, maxIndex = -1;
            for (int i = 0; i < start; i++) {
                int heuristic = getHeuristic(allBoard[i]);
                if (heuristic >= maxHeuristicAgain) {
                    maxHeuristicAgain = heuristic;
                    maxAgainIndex = i;
                }
            }
            for (int i = end + 1; i < n; i++) {
                int heuristic = getHeuristic(allBoard[i]);
                if (heuristic >= maxHeuristic) {
                    maxHeuristic = heuristic;
                    maxIndex = i;
                }
            }
            if (maxAgainIndex != -1) {
                short[] tmp = allBoard[0];
                allBoard[0] = allBoard[maxAgainIndex];
                allBoard[maxAgainIndex] = tmp;
            }
            if (maxIndex != -1) {
                short[] tmp = allBoard[end+1];
                allBoard[end+1] = allBoard[maxIndex];
                allBoard[maxIndex] = tmp;
            }
        }


        for (int i = 0; i < start; i++) {
            short[] newBoard = allBoard[i];
            int utility;
            utility = getMaxUtility(newBoard, depth - 1, alpha, beta);
            if (utility > maxVal) {
                maxVal = utility;
            }
            if (utility > alpha) {
                alpha = utility;
            }
            if (utility >= beta) {
                return maxVal;
            }
        }
        for (int i = end + 1; i < n; i++) {
            short[] newBoard = allBoard[i];
            int utility;
            utility = getMinUtility(newBoard, depth - 1, alpha, beta);
            if (utility > maxVal) {
                maxVal = utility;
            }
            if (utility > alpha) {
                alpha = utility;
            }
            if (utility >= beta) {
                return maxVal;
            }
        }

        if (maxVal == Integer.MIN_VALUE) {
            return getUtility(board);
        } else {
            return maxVal;
        }
    }

    public int getMinUtility(short[] board, int depth, int alpha, int beta) {
        exploredNodes++;
        if (isFinished(board)) return getUtility(board);
        if (depth <= 0) return getHeuristic(board);

        if (futility) {
            if (board[2 * n + 1] > numSeedsDividedByTwo) {
                return Integer.MIN_VALUE + 1;
            }
            int lower = board[n] - numSeedsDividedByTwo;
            int upper = numSeedsDividedByTwo - board[2 * n + 1];

            if (both && lower > beta) {
                return beta;
            } else if (upper < alpha) {
                return alpha;
            }
        }

        int minValue = Integer.MAX_VALUE;
        short allBoard[][] = new short[n][2 * n + 2];
        int start = 0, end = n - 1;
        for (int i = n + 1; i < 2 * n + 1; i++) {
            if (board[i] > 0) {
                short[] tmp = new short[2 * n + 2];
                boolean again = makeMove(n, board, i, tmp);
                short[] newBoard;
                if (!again) {
                    newBoard = allBoard[end--];
                } else {
                    newBoard = allBoard[start++];
                }
                for (int j = 0; j < 2 * n + 2; j++) {
                    newBoard[j] = tmp[j];
                }
            }
        }

        if (sortAllNextMoves) {
            Arrays.sort(allBoard, 0, start, (a, b) -> getHeuristic(a) - getHeuristic(b));
            Arrays.sort(allBoard, end + 1, n, (a, b) -> getHeuristic(a) - getHeuristic(b));
        }

        if (onlyChooseMax) {
            int minHeuristicAgain = Integer.MAX_VALUE, minHeuristic = Integer.MAX_VALUE;
            int minAgainIndex = -1, minIndex = -1;
            for (int i = 0; i < start; i++) {
                int heuristic = getHeuristic(allBoard[i]);
                if (heuristic <= minHeuristicAgain) {
                    minHeuristicAgain = heuristic;
                    minAgainIndex = i;
                }
            }
            for (int i = end + 1; i < n; i++) {
                int heuristic = getHeuristic(allBoard[i]);
                if (heuristic <= minHeuristic) {
                    minHeuristic = heuristic;
                    minIndex = i;
                }
            }
            if (minAgainIndex != -1) {
                short[] tmp = allBoard[0];
                allBoard[0] = allBoard[minAgainIndex];
                allBoard[minAgainIndex] = tmp;
            }
            if (minIndex != -1) {
                short[] tmp = allBoard[end+1];
                allBoard[end+1] = allBoard[minIndex];
                allBoard[minIndex] = tmp;
            }
        }



        for (int i = 0; i < start; i++) {
            short[] newBoard = allBoard[i];
            int utility;
            utility = getMinUtility(newBoard, depth - 1, alpha, beta);
            if (utility < minValue) {
                minValue = utility;
            }
            if (utility < beta) {
                beta = utility;
            }
            if (utility <= alpha) {
                return minValue;
            }
        }
        for (int i = end + 1; i < n; i++) {
            short[] newBoard = allBoard[i];
            int utility;
            utility = getMaxUtility(newBoard, depth - 1, alpha, beta);
            if (utility < minValue) {
                minValue = utility;
            }
            if (utility < beta) {
                beta = utility;
            }
            if (utility <= alpha) {
                return minValue;
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
        System.err.format(this.name() + " timeoutMove: %d after depth: %d explored nodes: %,d/%,d\n", bestMove, maxDepth, exploredNodes, allExploredNodes + exploredNodes);
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
