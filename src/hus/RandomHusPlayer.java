package hus;

import hus.HusBoardState;
import hus.HusPlayer;
import student_player.StudentPlayer;
import student_player.StudentPlayer2;
import student_player.mytools.MyTools;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Random;

/** A random Hus player. */
public class RandomHusPlayer extends HusPlayer {
    Random rand = new Random();

    public RandomHusPlayer() { super("RandomHusPlayer"); }

    /** Choose moves randomly. */
    public HusMove chooseMove(HusBoardState board_state)
    {
        // Pick a random move from the set of legal moves.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        HusMove move = moves.get(rand.nextInt(moves.size()));
        return move;
    }
    
    public static void main(String args[]) {
    	RandomHusPlayer rand = new RandomHusPlayer();
    	HusBoardState hus = new HusBoardState();
    	HusMove movv = rand.chooseMove(hus);
    	System.out.println(movv.toPrettyString());
    }
	
	
}


