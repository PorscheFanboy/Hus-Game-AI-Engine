package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class MiniMax extends HusPlayer {

    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public MiniMax() { 
    	super("MiniMax");
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
    
    private int original_depth = 4;
    
    
    private int MiniMax(HusBoardState bState, int depth, HusMove[] resultStorage, int turn) {
    	if ((depth == 0) || bState.gameOver()) {
    		return eval(bState, turn);
    		
    	}
    	if (bState.getTurnPlayer() == turn) {
    		int best_value = -1000;
    		ArrayList<HusMove> moves = bState.getLegalMoves();
    		for (HusMove i : moves) {
            	HusBoardState cloned_board_state = (HusBoardState) bState.clone();
            	
                cloned_board_state.move(i);
            
                int result = MiniMax(cloned_board_state, depth - 1, resultStorage, turn);
                if (result > best_value) {
                	best_value = result;
                	if (depth == original_depth) resultStorage[0] = i;
                }
            }
    		return best_value;
    	}
    	else {
    		int best_value = 1000;
    		ArrayList<HusMove> moves = bState.getLegalMoves();
    		for (HusMove i : moves) {
    			
            	HusBoardState cloned_board_state = (HusBoardState) bState.clone();
                cloned_board_state.move(i);
                int result = MiniMax(cloned_board_state, depth - 1, resultStorage, turn);
                if (result < best_value) {
                	best_value = result;
                }
            }
    		return best_value;
    	}
    }

    public HusMove chooseMove(HusBoardState board_state)
    {

        
        HusMove storedMove = board_state.getLegalMoves().get(0);
        final HusMove[] resultStore = {storedMove};
        
        int turn = board_state.getTurnPlayer();
        
        MiniMax(board_state, original_depth, resultStore, turn);
        return resultStore[0];
    }
    
}


