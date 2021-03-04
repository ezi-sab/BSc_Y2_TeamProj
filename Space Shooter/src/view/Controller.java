package view;

import javafx.fxml.FXML;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;

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
