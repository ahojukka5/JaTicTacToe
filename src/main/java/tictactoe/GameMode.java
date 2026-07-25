package tictactoe;

/** Available game modes. */
public enum GameMode {
    COMPUTER("Vs computer"),
    LOCAL("Two players");

    private final String label;

    GameMode(final String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
