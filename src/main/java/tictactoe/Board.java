package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Rules and state for one Tic-Tac-Toe round. */
public final class Board {
    public static final int SIZE = 3;

    private static final List<List<Position>> WINNING_LINES =
            List.of(
                    line(0, 0, 0, 1, 0, 2),
                    line(1, 0, 1, 1, 1, 2),
                    line(2, 0, 2, 1, 2, 2),
                    line(0, 0, 1, 0, 2, 0),
                    line(0, 1, 1, 1, 2, 1),
                    line(0, 2, 1, 2, 2, 2),
                    line(0, 0, 1, 1, 2, 2),
                    line(0, 2, 1, 1, 2, 0));

    private final Mark[][] cells = new Mark[SIZE][SIZE];
    private Mark currentPlayer = Mark.X;
    private GameOutcome outcome = GameOutcome.IN_PROGRESS;
    private List<Position> winningLine = List.of();
    private int moveCount;

    /** Creates an empty board with X to move. */
    public Board() {
        reset();
    }

    private Board(final Board source) {
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(source.cells[row], 0, cells[row], 0, SIZE);
        }
        currentPlayer = source.currentPlayer;
        outcome = source.outcome;
        winningLine = source.winningLine;
        moveCount = source.moveCount;
    }

    /** Returns a deep copy suitable for simulation. */
    public Board copy() {
        return new Board(this);
    }

    /** Clears the round and gives the first move to X. */
    public void reset() {
        for (Mark[] row : cells) {
            Arrays.fill(row, Mark.EMPTY);
        }
        currentPlayer = Mark.X;
        outcome = GameOutcome.IN_PROGRESS;
        winningLine = List.of();
        moveCount = 0;
    }

    /** Returns the mark at a position. */
    public Mark markAt(final Position position) {
        Objects.requireNonNull(position, "position");
        return cells[position.row()][position.column()];
    }

    /** Returns the mark at a zero-based row and column. */
    public Mark markAt(final int row, final int column) {
        return markAt(new Position(row, column));
    }

    /** Returns the player whose turn it is. */
    public Mark currentPlayer() {
        return currentPlayer;
    }

    /** Returns the current round outcome. */
    public GameOutcome outcome() {
        return outcome;
    }

    /** Returns the winning cells, or an empty list. */
    public List<Position> winningLine() {
        return winningLine;
    }

    /** Returns the number of moves played. */
    public int moveCount() {
        return moveCount;
    }

    /** Returns whether a position can be played now. */
    public boolean isPlayable(final Position position) {
        return !outcome.isFinished() && markAt(position) == Mark.EMPTY;
    }

    /** Places the current player's mark. */
    public void play(final Position position) {
        Objects.requireNonNull(position, "position");
        if (!isPlayable(position)) {
            throw new IllegalArgumentException("Position is not playable: " + position);
        }

        cells[position.row()][position.column()] = currentPlayer;
        moveCount++;
        updateOutcome();
        if (!outcome.isFinished()) {
            currentPlayer = currentPlayer.opponent();
        }
    }

    /** Convenience overload using zero-based row and column. */
    public void play(final int row, final int column) {
        play(new Position(row, column));
    }

    /** Returns all currently playable positions. */
    public List<Position> availablePositions() {
        if (outcome.isFinished()) {
            return List.of();
        }
        List<Position> positions = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Position position = new Position(row, column);
                if (markAt(position) == Mark.EMPTY) {
                    positions.add(position);
                }
            }
        }
        return Collections.unmodifiableList(positions);
    }

    /** Returns true if placing the supplied mark at a position would win. */
    public boolean wouldWin(final Position position, final Mark mark) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(mark, "mark");
        if (mark == Mark.EMPTY || markAt(position) != Mark.EMPTY) {
            return false;
        }

        cells[position.row()][position.column()] = mark;
        boolean wins = findWinningLine(mark).isPresent();
        cells[position.row()][position.column()] = Mark.EMPTY;
        return wins;
    }

    private void updateOutcome() {
        var line = findWinningLine(currentPlayer);
        if (line.isPresent()) {
            winningLine = line.orElseThrow();
            outcome = currentPlayer == Mark.X ? GameOutcome.X_WON : GameOutcome.O_WON;
        } else if (moveCount == SIZE * SIZE) {
            outcome = GameOutcome.DRAW;
        }
    }

    private java.util.Optional<List<Position>> findWinningLine(final Mark mark) {
        return WINNING_LINES.stream()
                .filter(line -> line.stream().allMatch(position -> markAt(position) == mark))
                .findFirst();
    }

    private static List<Position> line(
            final int row1,
            final int column1,
            final int row2,
            final int column2,
            final int row3,
            final int column3) {
        return List.of(
                new Position(row1, column1),
                new Position(row2, column2),
                new Position(row3, column3));
    }
}
