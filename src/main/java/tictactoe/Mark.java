package tictactoe;

/** A mark placed on the board. */
public enum Mark {
    EMPTY(""),
    X("X"),
    O("O");

    private final String symbol;

    Mark(final String symbol) {
        this.symbol = symbol;
    }

    /** Returns the display symbol for this mark. */
    public String symbol() {
        return symbol;
    }

    /** Returns the opposing player mark. */
    public Mark opponent() {
        return switch (this) {
            case X -> O;
            case O -> X;
            case EMPTY -> throw new IllegalStateException("The empty mark has no opponent");
        };
    }
}
