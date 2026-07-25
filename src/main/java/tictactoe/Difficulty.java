package tictactoe;

/** Computer player strength. */
public enum Difficulty {
    RELAXED("Relaxed"),
    PERFECT("Unbeatable");

    private final String label;

    Difficulty(final String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
