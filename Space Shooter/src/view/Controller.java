package view;

import javafx.fxml.FXML;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class Controller implements EventHandler<KeyEvent> {


    private ShipModel shipModel;
<<<<<<< HEAD
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};
    final private static double FPS = 5.0;

    private Timer timer;
    private boolean paused;

    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private GameView gameView;

    public Controller() {
        this.paused = false;
    }

    public void initialize() {
        String file = this.getCurrentLevel(0);
=======
    private static final String[] levelFiles = {"Space Shooter/src/levels/level1.txt", "Space Shooter/src/levels/level2.txt", "Space Shooter/src/levels/level3.txt"};
    final private static double FPS = 5.0;
    
    private Timer timer;
    private boolean paused;
    
    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private GameView gameView;

    public Controller() {
    	this.paused = false;
    }

    public void initialize() {
    	String file = this.getCurrentLevel(0);
>>>>>>> master
        this.shipModel = new ShipModel();
        this.update(ShipModel.Direction.NONE);
        this.startTimer();
    }

    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(() -> update(ShipModel.getCurrentDirection()));
            }
        };

        long frameTimeInMilliseconds = (long) (1000.0 / FPS);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    private void update(ShipModel.Direction direction) {
        this.shipModel.step(direction);
        this.gameView.update(shipModel);
        this.scoreLabel.setText(String.format("Score: %d", this.shipModel.getScore()));
        this.levelLabel.setText(String.format("Level: %d", this.shipModel.getLevel()));
        if (shipModel.isGameOver()) {
            this.gameOverLabel.setText(String.format("GAME OVER"));
            pause();
        }
        if (shipModel.isYouWon()) {
            this.gameOverLabel.setText(String.format("YOU WON!"));
        }
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();
        ShipModel.Direction direction = ShipModel.Direction.NONE;
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            direction = ShipModel.Direction.LEFT;
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            direction = ShipModel.Direction.RIGHT;
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            direction = ShipModel.Direction.UP;
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            direction = ShipModel.Direction.DOWN;
        } else if (code == KeyCode.P) {
            pause();
            this.gameOverLabel.setText(String.format(""));
            paused = false;
            this.shipModel.startNewGame();
            this.startTimer();
        } else {
            keyRecognized = false;
        }
        if (keyRecognized) {
            keyEvent.consume();
            shipModel.setCurrentDirection(direction);
        }
    }

    public void pause() {
        this.timer.cancel();
    }

    public double getBoardWidth() {
        return GameView.CELL_WIDTH * this.gameView.getColumnCount();
    }

    public double getBoardHeight() {
        return GameView.CELL_WIDTH * this.gameView.getRowCount();
    }

    public static String getCurrentLevel(int x) {
        return levelFiles[x];
    }
<<<<<<< HEAD
=======
    
    public boolean getPaused() {
        return paused;
    }
}
    
>>>>>>> master

    public boolean getPaused() {
        return paused;
    }
}