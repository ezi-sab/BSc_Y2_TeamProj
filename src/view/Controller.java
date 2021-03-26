package view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;
import javafx.application.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements EventHandler<KeyEvent> {


    private PlayerModel player;
    private List<EnemyAIModel> enemies;
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};
    final private static double FPS = 5.0;
    
    private Timer timer;
    private boolean paused;
    
    @FXML
    private int rowCount;
    @FXML
    private int columnCount;
    
    private CellValue[][] grid;
    
    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private GameView gameView;
    
    
    static private int noEnemies;
    static private int score;
    static private int level;
    static private int flagCount;
    static private boolean gameOver;
    static private boolean youWon;

    public Controller() {
    	this.paused = false;
    }

    public void initialize() {
    	String file = this.getCurrentLevel(0);
    	initializeLevel(file);
    	
        this.update(ShipModel.Direction.NONE);
        this.startTimer();
    }

    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(()->update(player.getCurrentDirection()));
            }
        };

        long frameTimeInMilliseconds = (long) (1000.0 / FPS);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }
    
    
    public void initializeLevel(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            @SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                lineScanner.next();
                columnCount++;
            }
            rowCount++;
        }
        columnCount = columnCount / rowCount;
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        
		this.player = new PlayerModel(grid,rowCount,columnCount);
		        
        noEnemies = 2;
        enemies = new ArrayList<EnemyAIModel>();
    	for(int i = 0; i < noEnemies; i++) {
    		EnemyAIModel buffer = new EnemyAIModel(grid,rowCount,columnCount);
    		this.enemies.add(buffer);
    	}
    	
        int row = 0;
        
        while (scanner2.hasNextLine()) {
            int column = 0;
            String line = scanner2.nextLine();
            @SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                String value = lineScanner.next();
                CellValue thisValue;
                char valChar = value.charAt(0);
                switch (valChar) {
                    case 'P':
                    {
                        thisValue = CellValue.SHIPSTARTINGPOINT;
                        player.setLocation(new Point2D(row, column));
                        player.setVelocity(new Point2D(0, 0));
                        player.setCurrentDirection(Direction.NONE);
                        player.setLastDirection(Direction.NONE);
                        break;
                    }
                    case 'B':
                    	thisValue = CellValue.BLOCK;
                    	break;
                    case 'F':
                    	thisValue = CellValue.FLAG;
                    	break;
                    default:
                    	if (Character.isDigit(valChar)) {
	                    	thisValue = CellValue.ENEMY1STARTINGPOINT;
	                    	int valInt = valChar - '0' - 1;
	                    	enemies.get(valInt).setLocation(new Point2D(row,column));
	            	        enemies.get(valInt).setVelocity(new Point2D(0, 0)); //changed from -1,0
	            	        enemies.get(valInt).setCurrentDirection(Direction.NONE);
	            	        enemies.get(valInt).setLastDirection(Direction.NONE);
                    	}
                    	else {
                    		thisValue = CellValue.EMPTY;
                    	}
                }
                grid[row][column] = thisValue;
                column++;
            }
            row++;
        }
        
    }
    
    
    public void startNewGame(){
        rowCount = 0;
        columnCount = 0;
        gameOver = false;
        youWon = false;
        flagCount = 0;
        score = 0;
        level = 1;
        initializeLevel(Controller.getCurrentLevel(0));
    }
    
    public void step(Direction direction) {
        player.moveShip(direction);
        Point2D shipLocation = player.getLocation();
        Point2D shipVelocity = player.getVelocity();
        CellValue shipLocationCellValue = grid[(int) player.shipLocation.getX()][(int) shipLocation.getY()];
        if (shipLocationCellValue == CellValue.FLAG) {
        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
            flagCount--;
            score += 10;
        }
        
        for(int i = 0; i < noEnemies; i++) {
        	if (shipLocation.equals(this.enemies.get(i).getLocation())) {
                gameOver = true;
                shipVelocity = new Point2D(0,0);
            }
        	
        	this.enemies.get(i).moveEnemy();
        
        	if (shipLocation.equals(this.enemies.get(i).getLocation())) {
                gameOver = true;
                shipVelocity = new Point2D(0,0);
            }
    	}

        
        //start a new level if level is complete
        /*if (this.isLevelComplete()) {
            shipVelocity = new Point2D(0,0);
            startNextLevel();
        }*/
    }
    
    
    
    private void update(ShipModel.Direction direction) {
    	player.setGameGrid(grid);
    	this.step(direction);
        
        this.gameView.update(player,enemies);
        
        this.scoreLabel.setText(String.format("Score: %d", score));
        this.levelLabel.setText(String.format("Level: %d", level));
        if (gameOver) {
            this.gameOverLabel.setText(String.format("GAME OVER"));
            pause();
        }
        if (youWon) {
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
            this.startNewGame();
            this.startTimer();
        } else {
            keyRecognized = false;
        }
        if (keyRecognized) {
            keyEvent.consume();
            player.setCurrentDirection(direction);
        }
    }

    public void pause() {
        this.timer.cancel();
    }

    public double getBoardWidth() {
        return GameView.CELL_WIDTH * this.gameView.getGvColumnCount();
    }

    public double getBoardHeight() {
        return GameView.CELL_WIDTH * this.gameView.getGvRowCount();
    }
    
    public CellValue[][] getGrid(){
    	return grid;
    }
    
    public static String getCurrentLevel(int x) {
        return levelFiles[x];
    }
    
    public PlayerModel getPlayer() {
    	return this.player;
    }
    
    public List<EnemyAIModel> getEnemies() {
    	return this.enemies;
    }
    
    public boolean getPaused() {
        return paused;
    }
}
    

