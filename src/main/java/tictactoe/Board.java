package tictactoe;

/**
 * Tic Tac Toe board.
 */
public final class Board {

    /**
     * Number of rows this board has.
     */
    private static final int ROWS = 3;

    /**
     * Number of columns this board has.
     */
    private static final int COLUMNS = 3;

    /**
     * Game state.
     *
     * 0 = unplayed
     * 1 = player 1
     * 2 = player 2
     */
    private final int[][] state = new int[ROWS][COLUMNS];

    /**
     * Contains information which playes is in turn.
     */
    private int currentPlayer = 1;

    /**
     * Contains integer telling how many turns has been played so far.
     */
    private int numberOfTurnsPlayed = 0;

    /**
     * How many players is in game.
     */
    private final int numberOfPlayers = 2;

    /**
     * Get number of rows in this game.
     * @return integer
     */
    public int getNumberOfRows() {
        return ROWS;
    }

    /**
     * Get number of columns in this game.
     * @return integer
     */
    public int getNumberOfColumns() {
        return COLUMNS;
    }

    /**
     * Get number of turns played in this game.
     * @return integer
     */
    private int getNumberOfTurnsPlayed() {
        return numberOfTurnsPlayed;
    }

    /**
     * Get number of players in this game.
     * @return integer
     */
    private int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Get current player number.
     * @return integer
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set current player number.
     * @param player id number of player
     */
    private void setCurrentPlayer(final int player) {
        this.currentPlayer = player;
    }

    /**
     * Return the game state for cell i, j.
     * @param i row in game
     * @param j column in game
     * @return id number of player or 0 if not played yet
     */
    public int getState(final int i, final int j) {
        return state[i][j];
    }

    /**
     * Set the state of the game cell i, j.
     *
     * @param i row in game
     * @param j column in game
     * @param v player id
     */
    public void setState(final int i, final int j, final int v) {
        state[i][j] = v;
    }

    /**
     * Return true if game is already played.
     *
     * @return true if game is already played, false otherwise
     */
    public boolean endOfGame() {
        int maxNumberOfTurns = getNumberOfColumns() * getNumberOfColumns();
        return (getNumberOfTurnsPlayed() == maxNumberOfTurns || someoneWins());
    }

    /**
     * Returns true if cell is playable.
     *
     * @param row number of row, starting from 0
     * @param col number of column, starting from 0
     * @return true if can play cell
     */
    public boolean isPlayable(final int row, final int col) {
        if (getState(row, col) != 0) { // already played
            return false;
        }
        if (endOfGame()) { // game over
            return false;
        }
        return true;
    }

    /**
     * Play cell in certain row and column.
     *
     * @param row row of the board
     * @param col column of the board
     *
     * @throws IllegalArgumentException if cell is not playable
     */
    public void play(final int row, final int col) {
        if (!isPlayable(row, col)) {
            String msg = "Cannot play (%d, %d): not playable.";
            String formattedMsg = String.format(msg, row, col);
            throw new IllegalArgumentException(formattedMsg);
        }
        setState(row, col, getCurrentPlayer());
        nextPlayer();
    }

    /**
     * Change turn to the next player.
     */
    public void nextPlayer() {
        if (getCurrentPlayer() == getNumberOfPlayers()) {
            setCurrentPlayer(1);
        } else {
            setCurrentPlayer(getCurrentPlayer() + 1);
        }
        setNumberOfTurnsPlayed(getNumberOfTurnsPlayed() + 1);
    }

    /**
     * Set how many turns has been played so far.
     * @param i number of turns
     */
    private void setNumberOfTurnsPlayed(final int i) {
        numberOfTurnsPlayed = i;
    }

    /**
     * Return true if someone has been winning the game.
     * @return true value if someone already has won
     */
    private boolean someoneWins() {
        return getGameWinner() != 0;
    }

    /**
     * Check diagonal win for player.
     * @param pid player id
     * @return boolean, true means win
     */
    private boolean checkDiagonalWin(final int pid) {
        boolean diagonalWin1 = true;
        boolean diagonalWin2 = true;
        for (int i = 0; i < getNumberOfRows(); i++) {
            diagonalWin1 &= (getState(i, i) == pid);
            int j = getNumberOfRows() - i - 1;
            diagonalWin2 &= (getState(i, j) == pid);
        }
        return (diagonalWin1 || diagonalWin2);
    }

    /**
     * Check row win for player.
     * @param pid player id
     * @return boolean, true means win
     */
    private boolean checkRowWin(final int pid) {
        for (int row = 0; row < getNumberOfRows(); row++) {
            boolean rowWin = true;
            for (int col = 0; col < getNumberOfColumns(); col++) {
                rowWin &= (getState(row, col) == pid);
            }
            if (rowWin) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check column win for player.
     * @param pid player id
     * @return boolean, true means win
     */
    private boolean checkColumnWin(final int pid) {
        for (int col = 0; col < getNumberOfColumns(); col++) {
            boolean colWin = true;
            for (int row = 0; row < getNumberOfRows(); row++) {
                colWin &= (getState(row, col) == pid);
            }
            if (colWin) {
                return true;
            }
        }
        return false;
    }

     /**
     * Get the winner of the game.
     *
     * @return player id of the winner or 0 if no winner
     */
    private int getGameWinner() {
        for (int p = 1; p <= getNumberOfPlayers(); p++) {
            if (checkDiagonalWin(p)) {
                System.out.println("Diagonal win of player " + p);
                return p;
            }
            if (checkRowWin(p)) {
                System.out.println("Row win of player " + p);
                return p;
            }
            if (checkColumnWin(p)) {
                System.out.println("Column win of player " + p);
                return p;
            }
        }
        return 0;
    }
}
