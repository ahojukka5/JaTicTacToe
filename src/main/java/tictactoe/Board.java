package tictactoe;

public class Board {

    // Game state, 0 = unplayed, 1 = player 1, 2 = player 2.
    private final int[][] state = new int[3][3];
    private int currentPlayer = 1;
    private int numberOfTurnsPlayed = 0;
    private static final int numberOfPlayers = 2;

    private int getNumberOfTurnsPlayed() {
        return numberOfTurnsPlayed;
    }

    private int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    private void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Return the game state for cell i, j.
     *
     * @return integer corresponding the player id in that cell or 0 if cell has not been played yet
     */
    public int getState(int i, int j) {
        return state[i][j];
    }

    public void setState(int i, int j, int v) {
        state[i][j] = v;
    }

    /**
     * Return true if game is already played.
     */
    public boolean endOfGame() {
        return (getNumberOfTurnsPlayed() == 9 || someoneWins());
    }

    /**
     * Change turn to the next player.
     */
    public void nextPlayer() {
        setCurrentPlayer(getCurrentPlayer() == getNumberOfPlayers() ? 1 : getCurrentPlayer() + 1);
        setNumberOfTurnsPlayed(getNumberOfTurnsPlayed() + 1);
    }

    private void setNumberOfTurnsPlayed(int i) {
        numberOfTurnsPlayed = i;
    }

    /**
     * Return true if someone has been winning the game.
     */
    private boolean someoneWins() {
        return getGameWinner() != 0;
    }

    /**
     * Get the winner of the game.
     * 
     * @return player id of the winner or 0 if no winner
     */
    private int getGameWinner() {
        for (int p = 1; p <= getNumberOfPlayers(); p++) {

            // check diagonals
            if (getState(0, 0) == p && getState(1, 1) == p && getState(2, 2) == p) {
                return p;
            }
            if (getState(0, 2) == p && getState(1, 1) == p && getState(2, 0) == p) {
                return p;
            }
            // check rows
            for (int r = 0; r < 3; r++) {
                if (getState(r, 0) == p && getState(r, 1) == p && getState(r, 2) == p) {
                    return p;
                }
            }
            // check columns
            for (int c = 0; c < 3; c++) {
                if (getState(0, c) == p && getState(1, c) == p && getState(2, c) == p)
                    return p;
            }
        }
        return 0;
    }
}