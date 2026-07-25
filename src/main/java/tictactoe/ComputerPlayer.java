package tictactoe;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/** Computer opponent supporting a friendly and a perfect strategy. */
public final class ComputerPlayer {
    private static final List<Position> PREFERRED_ORDER =
            List.of(
                    new Position(1, 1),
                    new Position(0, 0),
                    new Position(0, 2),
                    new Position(2, 0),
                    new Position(2, 2),
                    new Position(0, 1),
                    new Position(1, 0),
                    new Position(1, 2),
                    new Position(2, 1));

    private final Random random;

    public ComputerPlayer() {
        this(new Random());
    }

    ComputerPlayer(final Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    /** Chooses a legal move for the current player. */
    public Position chooseMove(final Board board, final Difficulty difficulty) {
        Objects.requireNonNull(board, "board");
        Objects.requireNonNull(difficulty, "difficulty");
        if (board.outcome().isFinished() || board.availablePositions().isEmpty()) {
            throw new IllegalStateException("Cannot choose a move for a finished round");
        }

        return switch (difficulty) {
            case RELAXED -> chooseRelaxedMove(board);
            case PERFECT -> choosePerfectMove(board);
        };
    }

    private Position chooseRelaxedMove(final Board board) {
        Mark me = board.currentPlayer();
        for (Position position : orderedAvailablePositions(board)) {
            if (board.wouldWin(position, me)) {
                return position;
            }
        }
        for (Position position : orderedAvailablePositions(board)) {
            if (board.wouldWin(position, me.opponent())) {
                return position;
            }
        }

        List<Position> positions = board.availablePositions();
        return positions.get(random.nextInt(positions.size()));
    }

    private Position choosePerfectMove(final Board board) {
        Mark computerMark = board.currentPlayer();
        Position bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        for (Position position : orderedAvailablePositions(board)) {
            int score = scoreMove(board, position, computerMark);
            if (score > bestScore) {
                bestScore = score;
                bestMove = position;
            }
        }
        return Objects.requireNonNull(bestMove, "No legal move available");
    }

    private int scoreMove(final Board board, final Position position, final Mark computerMark) {
        Board simulation = board.copy();
        simulation.play(position);
        return minimax(simulation, computerMark, 0);
    }

    private int minimax(final Board board, final Mark computerMark, final int depth) {
        if (board.outcome().isFinished()) {
            if (board.outcome() == GameOutcome.DRAW) {
                return 0;
            }
            return board.outcome().winner() == computerMark ? 10 - depth : depth - 10;
        }

        boolean maximizing = board.currentPlayer() == computerMark;
        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Position position : orderedAvailablePositions(board)) {
            Board simulation = board.copy();
            simulation.play(position);
            int score = minimax(simulation, computerMark, depth + 1);
            bestScore = maximizing ? Math.max(bestScore, score) : Math.min(bestScore, score);
        }
        return bestScore;
    }

    private List<Position> orderedAvailablePositions(final Board board) {
        return PREFERRED_ORDER.stream().filter(position -> board.markAt(position) == Mark.EMPTY).toList();
    }
}
