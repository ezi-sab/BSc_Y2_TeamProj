package view;

import javafx.fxml.FXML;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyEvent;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class Controller implements EventHandler<KeyEvent> {

    @FXML
    private GameView gameView;
    private ShipModel shipModel;
    private static final String[] levelFiles = {"Space Shooter/src/levels/level1.txt"};
    final private static double FRAMES_PER_SECOND = 5.0;
    private Timer timer;

    public Controller() {}

    public void initialize() {
    	//String file = this.getLevelFile(0);
        
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

        long frameTimeInMilliseconds = (long) (1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }


     private void update(ShipModel.Direction direction) {
        this.shipModel.step(direction);
        this.gameView.update(shipModel);
    }


    @Override
    public void handle(KeyEvent keyEvent) {
        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();
        ShipModel.Direction direction = ShipModel.Direction.NONE;
        if (code == KeyCode.LEFT) {
            direction = ShipModel.Direction.LEFT;
        } else if (code == KeyCode.RIGHT) {
            direction = ShipModel.Direction.RIGHT;
        } else if (code == KeyCode.UP) {
            direction = ShipModel.Direction.UP;
        } else if (code == KeyCode.DOWN) {
            direction = ShipModel.Direction.DOWN;
        } else if (code == KeyCode.G) {
            pause();
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

    public static String getLevelFile(int x) {
        return levelFiles[x];
    }
    
}

    

