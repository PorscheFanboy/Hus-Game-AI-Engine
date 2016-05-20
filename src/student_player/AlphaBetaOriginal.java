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
    	
        // Get the contents of the pits so we can use it to make decisions.
        int[][] pits = board_state.getPits();

        // Use ``player_id`` and ``opponent_id`` to get my pits and opponent pits.
        int[] my_pits = pits[player_id];
        int[] op_pits = pits[opponent_id];
        

        // Use code stored in ``mytools`` package.
        MyTools.getSomething();
        
        int turn = board_state.getTurnPlayer();
        
        
        
        HusMove storedMove = board_state.getLegalMoves().get(0);
        final HusMove[] resultStore = {storedMove};
        
        
        
        
        AlphaBeta(board_state, original_depth, -1000, 1000, resultStore, turn);
        
        return resultStore[0];
    }
    

    
    
    public static void main(String args[]) {
    	AlphaBetaOriginal Jianhua = new AlphaBetaOriginal();
    	HusBoardState hus = new HusBoardState();
    	HusMove amov = new HusMove(6, 0);
    	hus.move(amov);
    	HusMove mov1 = new HusMove(22, 1);
		hus.move(mov1);
		HusMove mov2 = new HusMove(05, 0);
		hus.move(mov2);
    	HusMove mov = Jianhua.chooseMove(hus);
    	System.out.println(mov.toPrettyString());
    	
    	
    	
    	
    	
//		HusMove mov = new HusMove(9, 0);
//		hus.move(mov);
//		HusMove mov1 = new HusMove(21, 1);
//		hus.move(mov1);
//		HusMove mov2 = new HusMove(19, 0);
//		hus.move(mov2);
//		System.out.println(hus.toString());
//    	HusMove mov3 = Jianhua.chooseMove(hus);
//    	System.out.println("movv " + mov3.toPrettyString());
//    	hus.move(mov3);
//    	HusMove mov4 = Jianhua.chooseMove(hus);
//    	System.out.println("movv " + mov4.toPrettyString());
//    	hus.move(mov4);
//    	HusMove mov5 = Jianhua.chooseMove(hus);
//    	System.out.println("ASDF");
//    	ArrayList<HusMove> moves = hus.getLegalMoves();
//    	for(HusMove i : moves) {
//    		System.out.println(i.toPrettyString());
//    	}
//    	System.out.println("qwer");
//    	System.out.println("movv " + mov5.toPrettyString());
//    	hus.move(mov5);
//    	
//    	HusMove mov6 = Jianhua.chooseMove(hus);
//    	System.out.println("ASDF");
//    	ArrayList<HusMove> movs = hus.getLegalMoves();
//    	for(HusMove i : movs) {
//    		System.out.println(i.toPrettyString());
//    	}
//    	System.out.println("qwer");
//    	hus.move(mov6);
//    	System.out.println("movv " + mov6.toPrettyString());
//    	HusMove mov7 = Jianhua.chooseMove(hus);
//    	hus.move(mov7);
//    	System.out.println("movv " + mov7.toPrettyString());
    	
    	
    }
}


