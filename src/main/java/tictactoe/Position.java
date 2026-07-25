package tictactoe;

/** Zero-based board position. */
public record Position(int row, int column) {
    public Position {
        if (row < 0 || row >= Board.SIZE || column < 0 || column >= Board.SIZE) {
            throw new IllegalArgumentException("Position must be inside the 3 x 3 board");
        }
    }
}
