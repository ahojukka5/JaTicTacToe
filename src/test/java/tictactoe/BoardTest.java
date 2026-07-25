package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class BoardTest {
    @Test
    void startsEmptyWithXToMove() {
        Board board = new Board();
        assertEquals(Mark.X, board.currentPlayer());
        assertEquals(GameOutcome.IN_PROGRESS, board.outcome());
        assertEquals(9, board.availablePositions().size());
        assertEquals(0, board.moveCount());
    }

    @Test
    void alternatesPlayersAfterLegalMoves() {
        Board board = new Board();
        board.play(0, 0);
        assertEquals(Mark.O, board.currentPlayer());
        board.play(1, 1);
        assertEquals(Mark.X, board.currentPlayer());
    }

    @Test
    void detectsRowWinAndWinningCells() {
        Board board = boardWithMoves(0, 0, 1, 0, 0, 1, 1, 1, 0, 2);
        assertEquals(GameOutcome.X_WON, board.outcome());
        assertEquals(
                List.of(new Position(0, 0), new Position(0, 1), new Position(0, 2)),
                board.winningLine());
    }

    @Test
    void detectsColumnWin() {
        Board board = boardWithMoves(0, 0, 0, 1, 1, 0, 1, 1, 2, 2, 2, 1);
        assertEquals(GameOutcome.O_WON, board.outcome());
    }

    @Test
    void detectsDiagonalWin() {
        Board board = boardWithMoves(0, 0, 0, 1, 1, 1, 0, 2, 2, 2);
        assertEquals(GameOutcome.X_WON, board.outcome());
    }

    @Test
    void detectsDraw() {
        Board board = boardWithMoves(0, 0, 0, 1, 0, 2, 1, 1, 1, 0, 1, 2, 2, 1, 2, 0, 2, 2);
        assertEquals(GameOutcome.DRAW, board.outcome());
        assertTrue(board.availablePositions().isEmpty());
    }

    @Test
    void rejectsOccupiedCellAndMovesAfterGameEnds() {
        Board board = new Board();
        board.play(0, 0);
        assertThrows(IllegalArgumentException.class, () -> board.play(0, 0));

        Board finished = boardWithMoves(0, 0, 1, 0, 0, 1, 1, 1, 0, 2);
        assertThrows(IllegalArgumentException.class, () -> finished.play(2, 2));
    }

    @Test
    void simulationCopyDoesNotMutateOriginal() {
        Board board = new Board();
        Board copy = board.copy();
        copy.play(1, 1);
        assertEquals(Mark.EMPTY, board.markAt(1, 1));
        assertEquals(Mark.X, copy.markAt(1, 1));
        assertFalse(board.outcome().isFinished());
    }

    @Test
    void resetRestoresInitialState() {
        Board board = boardWithMoves(0, 0, 1, 0, 0, 1);
        board.reset();
        assertEquals(Mark.X, board.currentPlayer());
        assertEquals(GameOutcome.IN_PROGRESS, board.outcome());
        assertEquals(9, board.availablePositions().size());
    }

    private Board boardWithMoves(final int... coordinates) {
        Board board = new Board();
        for (int index = 0; index < coordinates.length; index += 2) {
            board.play(coordinates[index], coordinates[index + 1]);
        }
        return board;
    }
}
