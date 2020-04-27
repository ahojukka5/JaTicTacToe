package tictactoe;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {
    @Test public void testBoardInitialState() {
        Board board = new Board();
        assertTrue(!board.endOfGame());
        assertEquals(1, board.getCurrentPlayer());
    }
}
