package hus;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import java.lang.Math;

import boardgame.BoardPanel;

import hus.HusBoard;

/**
 * A board panel for display and input for Hus. */
public class HusBoardPanel extends BoardPanel
implements MouseListener, MouseMotionListener, ComponentListener {

    private static final long serialVersionUID = 2648134549469132906L;

    /** CVS version information */
    private static final String CVSID = "$Id: HusBoardPanel.java,v 1.0 2014/02/08 16:21:21 rvince3 Exp $";

    // Some constants affecting display
    static final Color BGCOLOR = new Color(227,26,28);
    static final Color PITCOLOR = new Color(253, 191, 111);
    static final Color AHusENTCOLOR = new Color(255, 255, 255);
    static final Color FONTCOLOR = new Color(0, 0, 0);

    static final Color[] TEAMCOLOR= {new Color(50,50,50), new Color(217, 93, 81), new Color(56, 93, 103), new Color(250, 250,250)};
    static final Color HIGHLIGHTCOLOR= new Color(100, 100, 100);

    static final int BORDERX = 16;
    static final int BORDERY = 30;

    static final int WIDTH_IN_PITS = HusBoard.BOARD_WIDTH;

    final int pit_offset = 2;
    int pit_radius = 0;
    int grid_size = 0;
    float barrier_height = 0.7f;
    int[][] pit_centres = new int[2][WIDTH_IN_PITS * 4];

    // Width and height of game board
    float w, h;
    int x_off, y_off;

    //    Point dragStart = null, dragEnd = null;
    BoardPanelListener list = null; // Who needs a move input ?

    public HusBoardPanel() {
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        this.addComponentListener( this );
    }

    protected void requestMove( BoardPanelListener l ) {
        list = l;
    }

    protected void cancelMoveRequest() {
        list = null;
    }

    /*
     * Converts from pit indices used by the GUI to a player id and
     * pit indices used by the board. First item is player id, second
     * item is pit index. */
    protected int[] guiPitToBoardPit(int gui_pit){
        int player_id = gui_pit < 2 * WIDTH_IN_PITS ? 0 : 1;
        int pit = gui_pit;

        if(pit / WIDTH_IN_PITS == 0){
            pit = WIDTH_IN_PITS - 1 - pit;
        }

        if(pit / WIDTH_IN_PITS == 2){
            pit = pit % WIDTH_IN_PITS;
            pit = 2 * WIDTH_IN_PITS - 1 - pit;
        }

        if(pit / WIDTH_IN_PITS == 3){
            pit = pit % WIDTH_IN_PITS;
        }

        return new int[] {player_id, pit};
    }

    private boolean clickInCircle(int centreX, int centreY, int clickX, int clickY, int radius){
       return (Math.pow((centreX - clickX), 2) +
               Math.pow((centreY - clickY), 2)) < Math.pow(radius, 2);
    }

    public void mousePressed(MouseEvent arg0) {
        // No move have been requested. E.g. if reviewing history.
        if(list == null)
            return;

        int clickX = arg0.getX();
        int clickY = arg0.getY();

        int clicked_pit = -1;

        for(int i = 0; i < WIDTH_IN_PITS * 4; i++){
            if(clickInCircle(pit_centres[0][i], pit_centres[1][i], clickX, clickY, pit_radius)){
                clicked_pit = i;
                break;
            }
        }

        HusBoard board = (HusBoard) getCurrentBoard();

        int player_id, pit;
        if(clicked_pit >= 0){
            int[] board_pit = guiPitToBoardPit(clicked_pit);

            player_id = board_pit[0];
            pit = board_pit[1];

            HusMove move = new HusMove(pit, player_id);
            if(board.isLegal(move)){
                list.moveEntered(move);
                cancelMoveRequest();
            }
        }

        repaint();
    }

    public void mouseDragged(MouseEvent arg0) {}

    public void mouseReleased(MouseEvent arg0) {}

    /** Paint the board to the offscreen buffer. This does the painting
     * of the actual board, but not the pieces being moved by the user.*/
    public void drawBoard( Graphics g ) {
        Rectangle clip = g.getClipBounds();

        g.setColor(BGCOLOR);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        float game_height = 4.0f + barrier_height;

        x_off = BORDERX;
        y_off = BORDERY;

        // Width of entire area within border
        w = clip.width - 2 * BORDERX;
        h = clip.height - 2 * BORDERY;

        if(w/h > WIDTH_IN_PITS / 4){
            float w_prime = h * WIDTH_IN_PITS / game_height;
            x_off += (w - w_prime) / 2.0f;
            w = w_prime;
        }else{
            float h_prime = w * (game_height / WIDTH_IN_PITS);
            y_off += (h - h_prime) / 2.0f;
            h = h_prime;
        }

        grid_size = (int)(h / game_height);
        pit_radius = (grid_size - (2 * pit_offset)) / 2;

        g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        FontMetrics fm = g.getFontMetrics();

        // Draw the pits, and store the centres while we're at it.
        for( int i = 0; i < 4; i++ ) {
            int y = y_off + i * grid_size;

            if(i > 1){
                y += barrier_height * grid_size;
            }

            for( int j = 0; j < WIDTH_IN_PITS; j++ ) {
                int x = x_off + j * grid_size;

                g.setColor(PITCOLOR);

                g.fillOval(
                    x + pit_offset, y + pit_offset, 2 * pit_radius, 2 * pit_radius);

                pit_centres[0][i * WIDTH_IN_PITS + j] = x + pit_offset + pit_radius;
                pit_centres[1][i * WIDTH_IN_PITS + j] = y + pit_offset + pit_radius;


                g.setColor(FONTCOLOR);
                int pl = (i == 0 || i == 2) ? WIDTH_IN_PITS - 1 - j : j;
                pl = (i == 1 || i == 2) ? pl + WIDTH_IN_PITS : pl;

                String pit_label = Integer.toString(pl);

                Rectangle2D r = fm.getStringBounds(pit_label, g).getBounds2D();

                if(i == 0 || i == 2){
                    g.drawString(
                        pit_label, x + grid_size / 2 - (int)r.getWidth() / 2, y);
                }else{
                    g.drawString(
                        pit_label, x + grid_size / 2 - (int)r.getWidth() / 2,
                        y + grid_size / 2 + pit_radius / (int)Math.sqrt(2) + (int)r.getHeight());
                }
            }
        }

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        String p0 = "P0";
        Rectangle2D r = fm.getStringBounds(p0, g).getBounds2D();
        g.drawString(
            p0, x_off + (int)((w - r.getWidth()) / 2.0), 20);//(int)(h + r.getHeight() * 0.4));

        String p1 = "P1";
        r = fm.getStringBounds(p1, g).getBounds2D();
        g.drawString(
            p1, x_off + (int)((w - r.getWidth()) / 2), clip.height - 5);// r.getHeight() * 0.4));
    }

    /** We use the double-buffering provided by the superclass, but draw
     *  the "transient" elements in the paint() method. */
    public void paint( Graphics g ) {
        // Paint the board as usual, this will use the offscreen buffer
        super.paint(g);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        FontMetrics fm = g.getFontMetrics();

        int n_seeds = 0;
        HusBoard board = (HusBoard) getCurrentBoard();

        if(board == null){
            return;
        }

        // Paint the number of seeds in each pit
        g.setColor(FONTCOLOR);
        for(int i = 0; i < 4 * WIDTH_IN_PITS; i++){
            int[] board_pit = guiPitToBoardPit(i);

            n_seeds = board.getNumSeeds(board_pit[0], board_pit[1]);

            if(n_seeds > 0){
                String s = Integer.toString(n_seeds);
                Rectangle2D r = fm.getStringBounds(s, g).getBounds2D();

                int pit_centre_x = pit_centres[0][i];
                int pit_centre_y = pit_centres[1][i];

                g.drawString(
                    s, (int)(pit_centre_x - r.getWidth()/2),
                    (int)(pit_centre_y + r.getHeight() * 0.4));
            }
        }
    }

    public void componentResized(ComponentEvent arg0) {
    }

    /* Don't use these interface methods */
    public void mouseClicked(MouseEvent arg0) {}
    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}
    public void mouseMoved(MouseEvent arg0) {}
    public void componentMoved(ComponentEvent arg0) {}
    public void componentShown(ComponentEvent arg0) {}
    public void componentHidden(ComponentEvent arg0) {}
}


