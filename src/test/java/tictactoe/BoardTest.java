package tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Game board tests.
 */
public class BoardTest {

    /**
     * Test that initial state of the board is ok.
     */
    @Test public void testBoardInitialState() {
        Board board = new Board();
        assertFalse(board.endOfGame());
        assertEquals(1, board.getCurrentPlayer());
    }

    /**
     * Test that gameover is not given.
     * + ----- +
     * | O   O |
     * |   O X |
     * | X     |
     * +-------+
     */
    @Test public void testGameOver() {
        Board board = new Board();
        board.play(1, 1);
        board.play(1, 2);
        board.play(0, 2);
        board.play(2, 0);
        board.play(0, 0);
        assertFalse(board.endOfGame());
    }
}
