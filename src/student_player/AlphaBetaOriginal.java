package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Collections;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class AlphaBetaOriginal extends HusPlayer {

    public AlphaBetaOriginal() { 
    	super("AlphaBetaOriginal");
    }
    
    
 
    private int eval(HusBoardState board_state, int turn) {
    	int[][] pits = board_state.getPits();

        int[] my_pits = pits[turn];
        int mySum = 0;
    	for (int i : my_pits) {
    		mySum = mySum + i;
    	}
    	return mySum;
    }
    
    
    private int original_depth = 6;
    
    // AlphaBeta Algorithm using Wikipedia pseudocode
    private int AlphaBeta(HusBoardState bState, int depth, int alpha, int beta, HusMove[] resultStorage, int turn) {
    	if ((depth == 0) || (bState.gameOver() == true)) {
    		return eval(bState, turn);
    	}
    	else if (bState.getTurnPlayer() == turn) {
    		int best_value = -1000;
    		ArrayList<HusMove> moves = bState.getLegalMoves();
    		// Collections.shuffle(moves);
    		for (HusMove i : moves) {
            	HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                cloned_board_state.move(i);
                int result = AlphaBeta(cloned_board_state, depth - 1, alpha, beta, resultStorage, turn);
                if (result > best_value) {
                	best_value = result;
                	if (depth == original_depth)  {
                		resultStorage[0] = i;
                	}
                }
                alpha = Math.max(alpha, best_value);
                if (beta <= alpha) break;                
            }
    		return best_value;
    	}
    	else {
    		int best_value = 1000;
    		ArrayList<HusMove> moves = bState.getLegalMoves();
    		// Collections.shuffle(moves);
    		for (HusMove i : moves) {
            	HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                cloned_board_state.move(i);
                int result = AlphaBeta(cloned_board_state, depth - 1, alpha, beta, resultStorage, turn);
                best_value = Math.min(best_value, result);
                beta = Math.min(beta, best_value);
                if (beta <= alpha) break;                
            }
    		return best_value;
    	}
    }
    
    
   

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
    public HusMove chooseMove(HusBoardState board_state)
    {
    	
        
        int turn = board_state.getTurnPlayer();
        
        HusMove storedMove = board_state.getLegalMoves().get(0);
        final HusMove[] resultStore = {storedMove};
        
        AlphaBeta(board_state, original_depth, -1000, 1000, resultStore, turn);
        
        return resultStore[0];
    }
    
}


