package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;

class ComputerPlayerTest {
    private final ComputerPlayer player = new ComputerPlayer(new Random(7));

    @Test
    void perfectPlayerTakesImmediateWin() {
        Board board = boardWithMoves(0, 0, 1, 0, 2, 2, 1, 1, 0, 2);
        assertEquals(new Position(1, 2), player.chooseMove(board, Difficulty.PERFECT));
    }

    @Test
    void perfectPlayerBlocksImmediateLoss() {
        Board board = boardWithMoves(0, 0, 1, 1, 0, 1);
        assertEquals(new Position(0, 2), player.chooseMove(board, Difficulty.PERFECT));
    }

    @Test
    void perfectPlayerPrefersCenterOnEmptyBoard() {
        assertEquals(new Position(1, 1), player.chooseMove(new Board(), Difficulty.PERFECT));
    }

    @Test
    void relaxedPlayerAlwaysReturnsLegalMove() {
        Board board = boardWithMoves(1, 1, 0, 0, 2, 2);
        Position move = player.chooseMove(board, Difficulty.RELAXED);
        assertNotNull(move);
        assertEquals(Mark.EMPTY, board.markAt(move));
    }

    @Test
    void cannotChooseMoveForFinishedRound() {
        Board board = boardWithMoves(0, 0, 1, 0, 0, 1, 1, 1, 0, 2);
        assertThrows(
                IllegalStateException.class,
                () -> player.chooseMove(board, Difficulty.PERFECT));
    }

    private Board boardWithMoves(final int... coordinates) {
        Board board = new Board();
        for (int index = 0; index < coordinates.length; index += 2) {
            board.play(coordinates[index], coordinates[index + 1]);
        }
        return board;
    }
}
