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
                if (depth == original_depth) System.out.println(i.toPrettyString() + " result " + result);
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
    
    public static void main(String args[]) {
        StudentPlayer2 Jianhua = new StudentPlayer2();
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
////		for (int i = 0; i < 32; i++) {
////			System.out.println(hus.getPits()[0][i]);
////		}
//    	HusMove mov3 = Jianhua.chooseMove(hus);
//    	System.out.println("movv " + mov3.toPrettyString());
//    	hus.move(mov3);
//    	HusMove mov4 = Jianhua.chooseMove(hus);
//    	System.out.println("movv " + mov4.toPrettyString());
//    	hus.move(mov4);
//    	HusMove mov5 = Jianhua.chooseMove(hus);
//    	hus.move(mov5);
//    	System.out.println("movv " + mov5.toPrettyString());
//    	HusMove mov6 = Jianhua.chooseMove(hus);
//    	hus.move(mov6);
//    	System.out.println("movv " + mov6.toPrettyString());
//    	HusMove mov7 = Jianhua.chooseMove(hus);
//    	hus.move(mov7);
//    	System.out.println("movv " + mov7.toPrettyString());
    }
    

}


