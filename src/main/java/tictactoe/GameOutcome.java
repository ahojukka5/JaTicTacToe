package tictactoe;

/** Current result of a round. */
public enum GameOutcome {
    IN_PROGRESS,
    X_WON,
    O_WON,
    DRAW;

    /** Returns true after the round has finished. */
    public boolean isFinished() {
        return this != IN_PROGRESS;
    }

    /** Returns the winner, or {@link Mark#EMPTY} when there is no winner. */
    public Mark winner() {
        return switch (this) {
            case X_WON -> Mark.X;
            case O_WON -> Mark.O;
            case IN_PROGRESS, DRAW -> Mark.EMPTY;
        };
    }
}
