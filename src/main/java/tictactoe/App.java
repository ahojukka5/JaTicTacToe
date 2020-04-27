package tictactoe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * TicTacToe main application.
 */
public final class App extends Application {

    /**
     * Player marks, by default first player is X and second is O.
     */
    private static final char[] MARKS = new char[] {'_', 'X', 'O'};

    /**
     * Text shown in the top of the game.
     */
    private final transient Label header = new Label("Turn: X");

    /**
     * Font used in header.
     */
    private final transient Font headerFont = Font.font("Monospaced", 20);

    /**
     * Game board.
     */
    private final transient Board board = new Board();

    /**
     * Font used in buttons.
     */
    private final transient Font buttonFont = Font.font("Monospaced", 40);

    /**
     * Return the current player mark, either "X" or "O".
     *
     * @return String "X" or "O"
     */
    private String getCurrentPlayerMark() {
        return String.valueOf(MARKS[board.getCurrentPlayer()]);
    }

    /**
     * Updates header, showing turn 'X' / 'O' or "End of Game".
     */
    private void updateHeader() {
        if (board.endOfGame()) {
            header.setText("End of Game!");
        } else {
            header.setText("Turn: " + App.this.getCurrentPlayerMark());
        }
    }

    /**
     * Handles the button click.
     */
    private class ButtonClickHandler implements EventHandler<ActionEvent> {

        /**
         * Button which this handler is registered to.
         */
        private final transient Button btn;

        /**
         * Board row location for this button, 0 is first.
         */
        private final transient int i;

        /**
         * Board column location for this button, 0 is first.
         */
        private final transient int j;

        /**
         * Initialized new handler for button.
         * @param button button
         * @param row row location of button in game board
         * @param col column location of button in game board
         */
        ButtonClickHandler(final Button button, final int row, final int col) {
            this.btn = button;
            this.i = row;
            this.j = col;
        }

        @Override
        public void handle(final ActionEvent event) {
            if (!board.isPlayable(i, j)) {
                return;
            }
            btn.setText(App.this.getCurrentPlayerMark());
            board.setState(i, j, board.getCurrentPlayer());
            board.nextPlayer();
            updateHeader();
        }
    }

    @Override
    public void start(final Stage stage) {

        header.setFont(headerFont);
        final GridPane gridPane = new GridPane();
        final BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(gridPane);

        for (int i = 1; i <= board.getNumberOfRows(); i++) {
            for (int j = 1; j <= board.getNumberOfColumns(); j++) {
                final Button btn = new Button(" ");
                btn.setFont(buttonFont);
                btn.setOnAction(new ButtonClickHandler(btn, i - 1, j - 1));
                gridPane.add(btn, i, j);
            }
        }

        final Scene mainScene = new Scene(borderPane);
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * Run the main application.
     * @param args command line arguments to game
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
