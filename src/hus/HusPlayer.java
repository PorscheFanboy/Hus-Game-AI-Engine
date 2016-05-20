package hus;

import boardgame.Board;
import boardgame.BoardState;
import boardgame.Move;
import boardgame.Player;


/** An abstract class for Hus players. */
public abstract class HusPlayer extends Player {
    protected int opponent_id;

    public HusPlayer() { super("HusPlayer"); }
    public HusPlayer(String s) { super(s); }

    final public Board createBoard() { return new HusBoard(); }

    final public void setColor( int c ) {
        player_id = c;
        opponent_id = (c + 1) % 2;
    }

    // Casts the board to the correct type.
    final public Move chooseMove(BoardState bs)
    {
        // Cast the arguments to the objects we want to work with.
        HusBoardState board_state = (HusBoardState) bs;
        return this.chooseMove(board_state);
    }

    // The method that students implement.
    public abstract Move chooseMove(HusBoardState board_state);
}
