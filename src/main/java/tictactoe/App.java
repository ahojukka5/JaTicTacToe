package tictactoe;

import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Modern JavaFX Tic-Tac-Toe application. */
public final class App extends Application {
    private static final Duration COMPUTER_DELAY = Duration.millis(420);
    private static final Duration MOVE_DURATION = Duration.millis(190);

    private final Board board = new Board();
    private final ComputerPlayer computer = new ComputerPlayer();
    private final Button[][] cells = new Button[Board.SIZE][Board.SIZE];
    private final Label status = new Label();
    private final Label detail = new Label();
    private final Label roundLabel = new Label();
    private final Label xScoreLabel = new Label("0");
    private final Label drawScoreLabel = new Label("0");
    private final Label oScoreLabel = new Label("0");
    private final ComboBox<Difficulty> difficultyBox = new ComboBox<>();
    private final Button themeButton = new Button("Light mode");

    private StackPane root;
    private GameMode mode = GameMode.COMPUTER;
    private Difficulty difficulty = Difficulty.PERFECT;
    private PauseTransition pendingComputerMove;
    private boolean darkTheme = true;
    private boolean computerThinking;
    private int xScore;
    private int draws;
    private int oScore;
    private int round = 1;

    @Override
    public void start(final Stage stage) {
        root = createRoot();
        Scene scene = new Scene(root, 1040, 720);
        scene.getStylesheets().add(resource("app.css"));
        stage.setTitle("JaTicTacToe");
        stage.setMinWidth(900);
        stage.setMinHeight(640);
        stage.setScene(scene);
        stage.show();
        refresh(false);
    }

    private StackPane createRoot() {
        StackPane pane = new StackPane();
        pane.getStyleClass().addAll("app-root", "dark-theme");

        Circle firstOrb = backgroundOrb(280, "orb-one");
        Circle secondOrb = backgroundOrb(220, "orb-two");
        StackPane.setAlignment(firstOrb, Pos.TOP_LEFT);
        StackPane.setAlignment(secondOrb, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(firstOrb, new Insets(-150, 0, 0, -120));
        StackPane.setMargin(secondOrb, new Insets(0, -80, -110, 0));

        BorderPane shell = new BorderPane();
        shell.getStyleClass().add("shell");
        shell.setPadding(new Insets(28, 34, 32, 34));
        shell.setTop(createHeader());

        Node sidebar = createSidebar();
        Node game = createGameCard();
        HBox content = new HBox(24, sidebar, game);
        content.setAlignment(Pos.CENTER);
        HBox.setHgrow(game, Priority.ALWAYS);
        shell.setCenter(content);

        pane.getChildren().addAll(firstOrb, secondOrb, shell);
        return pane;
    }

    private Circle backgroundOrb(final double radius, final String styleClass) {
        Circle orb = new Circle(radius);
        orb.getStyleClass().addAll("background-orb", styleClass);
        orb.setMouseTransparent(true);
        return orb;
    }

    private Node createHeader() {
        Label logo = new Label("XO");
        logo.getStyleClass().add("logo-mark");
        Label title = new Label("JaTicTacToe");
        title.getStyleClass().add("brand-name");
        Label subtitle = new Label("A small classic, rebuilt with modern Java");
        subtitle.getStyleClass().add("brand-tagline");
        HBox brand = new HBox(13, logo, new VBox(2, title, subtitle));
        brand.setAlignment(Pos.CENTER_LEFT);

        themeButton.getStyleClass().add("secondary-button");
        themeButton.setOnAction(event -> toggleTheme());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(16, brand, spacer, themeButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 24, 0));
        return header;
    }

    private Node createSidebar() {
        Label eyebrow = label("MATCH SETUP", "eyebrow");
        Label heading = label("Choose your game", "card-title");

        ToggleButton computerMode = modeButton("Vs computer");
        ToggleButton localMode = modeButton("Two players");
        ToggleGroup group = new ToggleGroup();
        computerMode.setToggleGroup(group);
        localMode.setToggleGroup(group);
        computerMode.setSelected(true);
        group.selectedToggleProperty().addListener((value, oldToggle, selected) -> {
            if (selected == null) {
                oldToggle.setSelected(true);
                return;
            }
            mode = selected == computerMode ? GameMode.COMPUTER : GameMode.LOCAL;
            difficultyBox.setDisable(mode == GameMode.LOCAL);
            resetMatch();
        });

        Label difficultyLabel = label("Computer strength", "field-label");
        difficultyBox.getItems().setAll(Difficulty.values());
        difficultyBox.setValue(difficulty);
        difficultyBox.setMaxWidth(Double.MAX_VALUE);
        difficultyBox.valueProperty().addListener((value, oldDifficulty, selected) -> {
            if (selected != null) {
                difficulty = selected;
                resetMatch();
            }
        });

        HBox scores = new HBox(
                8,
                scoreTile("X", xScoreLabel, "x-score"),
                scoreTile("Draw", drawScoreLabel, "draw-score"),
                scoreTile("O", oScoreLabel, "o-score"));
        scores.setAlignment(Pos.CENTER);

        Button resetButton = new Button("Reset match score");
        resetButton.getStyleClass().add("text-button");
        resetButton.setOnAction(event -> resetMatch());
        Label hint = label(
                "Tip: the unbeatable opponent evaluates every possible continuation.",
                "hint-text");
        hint.setWrapText(true);

        VBox card = new VBox(
                13,
                eyebrow,
                heading,
                new VBox(9, computerMode, localMode),
                difficultyLabel,
                difficultyBox,
                new Separator(),
                label("SESSION SCORE", "eyebrow"),
                scores,
                resetButton,
                hint);
        card.getStyleClass().addAll("glass-card", "settings-card");
        card.setPrefWidth(276);
        card.setMinWidth(250);
        card.setPadding(new Insets(25));
        return card;
    }

    private ToggleButton modeButton(final String text) {
        ToggleButton button = new ToggleButton(text);
        button.getStyleClass().add("mode-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Node scoreTile(final String caption, final Label value, final String styleClass) {
        value.getStyleClass().addAll("score-value", styleClass);
        Label title = label(caption, "score-caption");
        VBox tile = new VBox(2, value, title);
        tile.getStyleClass().add("score-tile");
        tile.setAlignment(Pos.CENTER);
        tile.setPrefWidth(70);
        return tile;
    }

    private Node createGameCard() {
        roundLabel.getStyleClass().add("round-label");
        status.getStyleClass().add("status-title");
        detail.getStyleClass().add("status-detail");
        VBox statusBox = new VBox(2, roundLabel, status, detail);

        Region statusSpacer = new Region();
        HBox.setHgrow(statusSpacer, Priority.ALWAYS);
        HBox statusRow = new HBox(16, statusBox, statusSpacer, label("●  LOCAL GAME", "live-chip"));
        statusRow.setAlignment(Pos.CENTER_LEFT);

        GridPane grid = createBoard();
        StackPane boardFrame = new StackPane(grid);
        boardFrame.getStyleClass().add("board-frame");
        boardFrame.setMaxSize(470, 470);
        VBox.setVgrow(boardFrame, Priority.ALWAYS);

        Label keyboardHint = label("Tab to move · Enter or Space to place", "keyboard-hint");
        Button newRoundButton = new Button("New round");
        newRoundButton.getStyleClass().add("primary-button");
        newRoundButton.setOnAction(event -> startNewRound());
        Region actionSpacer = new Region();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);
        HBox actions = new HBox(16, keyboardHint, actionSpacer, newRoundButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(20, statusRow, boardFrame, actions);
        card.getStyleClass().addAll("glass-card", "game-card");
        card.setPadding(new Insets(25));
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private GridPane createBoard() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("board-grid");
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        for (int index = 0; index < Board.SIZE; index++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / Board.SIZE);
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / Board.SIZE);
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                Position position = new Position(row, column);
                Button button = new Button();
                button.getStyleClass().add("cell-button");
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setOnAction(event -> play(position));
                button.setOnMouseEntered(event -> animateHover(button, 1.025));
                button.setOnMouseExited(event -> animateHover(button, 1.0));
                cells[row][column] = button;
                grid.add(button, column, row);
            }
        }
        return grid;
    }

    private void play(final Position position) {
        if (computerThinking || !board.isPlayable(position)) {
            return;
        }
        board.play(position);
        refresh(true);
        recordResult();
        if (mode == GameMode.COMPUTER && !board.outcome().isFinished()) {
            scheduleComputerMove();
        }
    }

    private void scheduleComputerMove() {
        computerThinking = true;
        refresh(false);
        pendingComputerMove = new PauseTransition(COMPUTER_DELAY);
        pendingComputerMove.setOnFinished(event -> {
            pendingComputerMove = null;
            board.play(computer.chooseMove(board, difficulty));
            computerThinking = false;
            refresh(true);
            recordResult();
        });
        pendingComputerMove.play();
    }

    private void recordResult() {
        if (!board.outcome().isFinished()) {
            return;
        }
        switch (board.outcome()) {
            case X_WON -> xScore++;
            case O_WON -> oScore++;
            case DRAW -> draws++;
            case IN_PROGRESS -> throw new IllegalStateException("Round is not finished");
        }
        refreshScores();
    }

    private void startNewRound() {
        cancelComputerMove();
        board.reset();
        round++;
        refresh(false);
        FadeTransition fade = new FadeTransition(Duration.millis(260), cells[0][0].getParent());
        fade.setFromValue(0.45);
        fade.setToValue(1.0);
        fade.play();
    }

    private void resetMatch() {
        cancelComputerMove();
        board.reset();
        xScore = 0;
        draws = 0;
        oScore = 0;
        round = 1;
        refreshScores();
        refresh(false);
    }

    private void cancelComputerMove() {
        if (pendingComputerMove != null) {
            pendingComputerMove.stop();
            pendingComputerMove = null;
        }
        computerThinking = false;
    }

    private void refresh(final boolean animate) {
        roundLabel.setText("Round " + round);
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                Position position = new Position(row, column);
                Button button = cells[row][column];
                Mark mark = board.markAt(position);
                boolean changed = !button.getText().equals(mark.symbol());
                button.setText(mark.symbol());
                button.getStyleClass().removeAll("x-mark", "o-mark", "winning-cell");
                if (mark == Mark.X) {
                    button.getStyleClass().add("x-mark");
                } else if (mark == Mark.O) {
                    button.getStyleClass().add("o-mark");
                }
                if (board.winningLine().contains(position)) {
                    button.getStyleClass().add("winning-cell");
                }
                button.setAccessibleText(
                        "Row " + (row + 1) + ", column " + (column + 1) + ", "
                                + (mark == Mark.EMPTY ? "empty" : mark.symbol()));
                boolean humanCanPlay = !computerThinking
                        && board.isPlayable(position)
                        && (mode == GameMode.LOCAL || board.currentPlayer() == Mark.X);
                button.setDisable(!humanCanPlay);
                if (animate && changed && mark != Mark.EMPTY) {
                    animatePlacement(button);
                }
            }
        }
        refreshStatus();
    }

    private void refreshStatus() {
        if (computerThinking) {
            status.setText("Computer is thinking…");
            detail.setText("Searching the strongest continuation");
            return;
        }
        switch (board.outcome()) {
            case IN_PROGRESS -> {
                String player = board.currentPlayer().symbol();
                status.setText(player + " to move");
                detail.setText(mode == GameMode.COMPUTER
                        ? "Your turn — find the winning line"
                        : "Pass the board to player " + player);
            }
            case X_WON -> {
                status.setText("X takes the round");
                detail.setText("Three in a row — nicely played");
            }
            case O_WON -> {
                status.setText(mode == GameMode.COMPUTER
                        ? "Computer takes the round"
                        : "O takes the round");
                detail.setText("Three in a row decides it");
            }
            case DRAW -> {
                status.setText("Perfectly balanced");
                detail.setText("No empty square and no winner");
            }
        }
    }

    private void refreshScores() {
        xScoreLabel.setText(Integer.toString(xScore));
        drawScoreLabel.setText(Integer.toString(draws));
        oScoreLabel.setText(Integer.toString(oScore));
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;
        root.getStyleClass().removeAll("dark-theme", "light-theme");
        root.getStyleClass().add(darkTheme ? "dark-theme" : "light-theme");
        themeButton.setText(darkTheme ? "Light mode" : "Dark mode");
    }

    private void animatePlacement(final Node node) {
        node.setScaleX(0.72);
        node.setScaleY(0.72);
        ScaleTransition transition = new ScaleTransition(MOVE_DURATION, node);
        transition.setToX(1.0);
        transition.setToY(1.0);
        transition.play();
    }

    private void animateHover(final Node node, final double scale) {
        if (!node.isDisabled()) {
            ScaleTransition transition = new ScaleTransition(Duration.millis(110), node);
            transition.setToX(scale);
            transition.setToY(scale);
            transition.play();
        }
    }

    private Label label(final String text, final String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private String resource(final String name) {
        return Objects.requireNonNull(App.class.getResource(name), "Missing resource: " + name)
                .toExternalForm();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
