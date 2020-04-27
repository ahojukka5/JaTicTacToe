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

public class App extends Application {

    private static final char[] playerMarks = new char[]{'_', 'X', 'O'};
    private final Label header = new Label("Turn: X");
    private final Board board = new Board();

    /**
     * Return the current player mark, either "X" or "O"
     * @return String "X" or "O"
     */
    private String getCurrentPlayerMark() {
        return String.valueOf(playerMarks[board.getCurrentPlayer()]);
    }

    /**
     * Updates header, showing turn 'X' / 'O' or "End of Game"
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

        private final Button btn;
        private boolean pressed = false;
        private final int i;
        private final int j;

        public ButtonClickHandler(Button button, int i, int j) {
            this.btn = button;
            this.i = i;
            this.j = j;
        }

        @Override
        public void handle(ActionEvent event) {
            if (pressed || board.endOfGame()) {
                return;
            }
            pressed = true;
            btn.setText(App.this.getCurrentPlayerMark());
            board.setState(i, j, board.getCurrentPlayer());
            board.nextPlayer();
            updateHeader();
        }
    }

    @Override
    public void start(Stage stage) {

        header.setFont(Font.font("Monospaced", 20));
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(gridPane);

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                Button btn = new Button(" ");
                btn.setFont(Font.font("Monospaced", 40));
                btn.setOnAction(new ButtonClickHandler(btn, i - 1, j - 1));
                gridPane.add(btn, i, j);
            }
        }

        Scene mainScene = new Scene(borderPane);
        stage.setScene(mainScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(App.class);
    }
}
