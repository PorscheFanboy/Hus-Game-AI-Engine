package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class StudentPlayer extends HusPlayer {
    
    public StudentPlayer() { 
        super("260351181");
    }
    
    // My evaluation function, simply counts the seeds
    private int eval(HusBoardState board_state, int turn) {
        int[][] pits = board_state.getPits();

        int[] my_pits = pits[turn];
        int sum = 0;
        for (int i : my_pits) {
            sum = sum + i;
        }
        return sum;
    }


    // Comparator to sort states from highest heuristic value to lowest
    private Comparator<tupleStateMoveValue> CreateComparatorMax() {
        return new Comparator<tupleStateMoveValue>() {
            @Override
            public int compare(tupleStateMoveValue o1, tupleStateMoveValue o2) {
                return o2.val - o1.val;
            }
        };
    }
    
    // Comparator to sort states from lowest heuristic value to highest
    private Comparator<tupleStateMoveValue> CreateComparatorMin() {
        return new Comparator<tupleStateMoveValue>() {
            @Override
            public int compare(tupleStateMoveValue o1, tupleStateMoveValue o2) {
                return o1.val - o2.val;
            }
        };
    }
    
    // The move that is equal to pit, must be searched first, order the rest of the moves from 
    // highest to lowest
    private Comparator<tupleStateMoveValue> CreateComparatorMax1(final int pit) {
        return new Comparator<tupleStateMoveValue>() {
            @Override
            public int compare(tupleStateMoveValue o1, tupleStateMoveValue o2) {
                if (o2.aMove.getPit() == pit) return 1000;
                else if (o1.aMove.getPit() == pit) return -1000;
                return o2.val - o1.val;
            }
        };
    }
    
    
    // AlphaBeta Search
    private int AlphaBeta(HusBoardState bState, int depth, int alpha, int beta, HusMove[] resultStorage, int turn, int originalDepth) {
        if (Thread.currentThread().isInterrupted()) { // Thread interrupted, return immediately
            return 0;
        }
        if ((depth == 0) || (bState.gameOver() == true)) {
            return eval(bState, turn);
        }
        if (bState.getTurnPlayer() == turn) { // Max node
            if (depth > 2) {
                int best_value = -1000;
                ArrayList<HusMove> moves = bState.getLegalMoves();
                // Use a priority queue to sort all moves by their immediate heuristic values, 
                // For max player, sort from high to low
                PriorityQueue<tupleStateMoveValue> queue;
                if ((depth == originalDepth) && (resultStorage[1].getPlayerID() == 1)) {
                    // One shallower search has already been done, so use the result of that move
                    // as the first move to be searched
                    int pit = resultStorage[0].getPit();
                    Comparator<tupleStateMoveValue> comp1 = CreateComparatorMax1(pit);
                    queue = new PriorityQueue<tupleStateMoveValue>(32, comp1);
                }
                else {
                    Comparator<tupleStateMoveValue> comp = CreateComparatorMax();
                    queue = new PriorityQueue<tupleStateMoveValue>(32, comp);
                }
                for (HusMove hm: moves) {
                    HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                    cloned_board_state.move(hm);
                    int ev = eval(cloned_board_state, turn);
                    tupleStateMoveValue tsmv = new tupleStateMoveValue(cloned_board_state, hm, ev);
                    queue.add(tsmv);
                }
                while(!queue.isEmpty()) {
                    tupleStateMoveValue tsmv = queue.remove();
                    int result = AlphaBeta(tsmv.aState, depth - 1, alpha, beta, resultStorage, turn, originalDepth);
                    if (result > best_value) {
                        best_value = result;
                        if (depth == originalDepth)  {
                            resultStorage[0] = tsmv.aMove;
                        }
                    }
                    alpha = Math.max(alpha, best_value);
                    if (beta <= alpha) break;
                }
                return best_value;
            }
            else { // depth <= 2
                int best_value = -1000;
                ArrayList<HusMove> moves = bState.getLegalMoves();
                for (HusMove hm : moves) {
                    HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                    cloned_board_state.move(hm);
                    int result = AlphaBeta(cloned_board_state, depth - 1, alpha, beta, resultStorage, turn, originalDepth);
                    if (result > best_value) {
                        best_value = result;
                    }
                    alpha = Math.max(alpha, best_value);
                    if (beta <= alpha) break;                
                }
                return best_value;
            }
        }
        else { // Min node
            if (depth > 2) {
                int best_value = 1000;
                ArrayList<HusMove> moves = bState.getLegalMoves();
                Comparator<tupleStateMoveValue> comp1 = CreateComparatorMin();
                // Priority queue for min player, sort moves from low to high
                PriorityQueue<tupleStateMoveValue> queue = new PriorityQueue<tupleStateMoveValue>(32, comp1);
                for (HusMove hm: moves) {
                    HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                    cloned_board_state.move(hm);
                    int ev = eval(cloned_board_state, turn);
                    tupleStateMoveValue tsmv = new tupleStateMoveValue(cloned_board_state, hm, ev);
                    queue.add(tsmv);
                }
            
                while(!queue.isEmpty()) {
                    tupleStateMoveValue tsmv = queue.remove();
                    int result = AlphaBeta(tsmv.aState, depth - 1, alpha, beta, resultStorage, turn, originalDepth);
                    best_value = Math.min(best_value, result);
                    beta = Math.min(beta, best_value);
                    if (beta <= alpha) break;                
                }
                return best_value;
            }
            else { // depth <= 2
                int best_value = 1000;
                ArrayList<HusMove> moves = bState.getLegalMoves();
                for (HusMove hm : moves) {
                    HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                    cloned_board_state.move(hm);
                    int result = AlphaBeta(cloned_board_state, depth - 1, alpha, beta, resultStorage, turn, originalDepth);
                    best_value = Math.min(best_value, result);
                    beta = Math.min(beta, best_value);
                    if (beta <= alpha) break;                
                }
                return best_value;
            }
        }
    }
    

    
    public HusMove chooseMove(final HusBoardState board_state)
    {
        HusMove storedMove = board_state.getLegalMoves().get(0);
        
        // resultStore[0] will get updated with the best move found so far.
        // resultStore[1] will store a dummy move which serves to signal whether at least one
        // search has been completed, HusMove(0, 0) means no, HusMove(0, 1) means yes.
        final HusMove[] resultStore = {storedMove, new HusMove (0,0)};
        
        final int turn = board_state.getTurnPlayer();
        
        
        // Opening Book
        int[] openingBook1 = {20, 23, 23, 21, 23, 21, 20, 23, 23, 20, 23, 23,
              22, 23, 6, 22, 23, 6, 20, 23, 23, 22, 21, 20};
        int[] openingBook2 = {7, 19, 5, 16, 19, 2, 19, 19, 22, 20, 22, 22, 
                8, 22, 22, 5, 22, 2, 20, 22, 2, 14, 4, 20};
        
                
        // Use opening book to make decisions for the first and second move
        if (board_state.getTurnNumber() == 0 && turn == 0) {
            return new HusMove(23,0);
        }
        else if (board_state.getTurnNumber() == 0 && turn == 1) {
            HusBoardState startState = new HusBoardState();
            int[] board = board_state.getPits()[0];
            for (int i = 0; i < 24; i++) {
                HusMove aMove = new HusMove(i, 0);
                HusBoardState clonedState = (HusBoardState) startState.clone();
                clonedState.move(aMove);
                if (Arrays.equals(board, clonedState.getPits()[1-turn])) {
                    return new HusMove(openingBook1[i], 1);
                }
            }
        }
        else if ((board_state.getTurnNumber() == 1) && (turn == 0)) {
            HusBoardState startState = new HusBoardState();
            int[] board = board_state.getPits()[1];
            for (int i = 0; i < 24; i++) {
                HusMove aMove = new HusMove(23, 0);
                HusMove bMove = new HusMove(i, 1);
                HusBoardState clonedState = (HusBoardState) startState.clone();
                clonedState.move(aMove);
                clonedState.move(bMove);
                if (Arrays.equals(board, clonedState.getPits()[1-turn])) {
                    return new HusMove(openingBook2[i], 0);
                }
            }
        }
        
        // Create a thread so that we can have better time management.
        Callable<Object> Calculation = new Callable<Object>()
        {
            @Override
            public Object call() throws Exception
            {
                // Iterative Deepening with start depth of 8
                int depth = 8;
                while(!Thread.currentThread().isInterrupted()) {
                    AlphaBeta(board_state, depth, -1000, 1000, resultStore, turn, depth);
                    if (depth == 8) {
                        depth = 10;
                        resultStore[1].setPlayerID(1);
                    }
                    else {
                        depth++;
                    }
                }
                return 1;
            }
        };
        
        final ExecutorService service = Executors.newSingleThreadExecutor();

        try
        {
            final Future<Object> f = service.submit(Calculation);
            f.get(1949, TimeUnit.MILLISECONDS);
        }
        catch (final TimeoutException e)
        {
            // If timed out, return the best move found so far
            return resultStore[0];
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            service.shutdownNow();
        }
        
        // Return the best move found
        return resultStore[0];
    }
}

// A tuple that contains a state, a move, and its heuristic value
class tupleStateMoveValue {
    public HusBoardState aState;
    public HusMove aMove;
    public int val;
    public tupleStateMoveValue(HusBoardState bs, HusMove hm, int n) {
        aState = bs;
        aMove = hm;
        val = n;
    };
}
