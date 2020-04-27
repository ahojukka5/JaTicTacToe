package tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        assertTrue(!board.endOfGame());
        assertEquals(1, board.getCurrentPlayer());
    }
}
