package view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.ScoreBoard;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;
import javafx.application.Platform;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import view.Controller;

public class Controller implements EventHandler<KeyEvent> {
	
	
    private PlayerModel player;
    private List<EnemyAIModel> enemies;
    
//  Change all level1, level2, level3 to level0.txt to make the game easier
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};
    final private static double FPS = 5.0;
    
    private Timer timer;
    private boolean paused;
    
    private CellValue[][] grid;
    
    @FXML private int rowCount;
    @FXML private int columnCount;
        
    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private GameView gameView;
    
    private int noEnemies;
    private int score = 0;
    private int level = 0;
    private boolean gameOver = false;
    private boolean youWon = false;
    private int smalldot = 0;
    private boolean levelComplete = false;
    private int keyPressed = 0;
    
    private static SoundManager soundManager = new SoundManager();
    private static ScoreBoard scoreBoard = new ScoreBoard();
    private static ViewManager viewManager = new ViewManager();
    
    
    public Controller() {
    	this.paused = false;
    }

    
    public void initialize() {
    	String file = getCurrentLevel(0);
    	startLevel(file);
    }

    
    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(()->update());
            }
        };

        long frameTimeInMilliseconds = (long) (1000.0 / FPS);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }
    
    
    private void delayPause(int delay) {
    	
    	Timer delayPauseTimer = new Timer();
    	TimerTask delayPauseTask = new TimerTask() {
    		
    		@Override
    		public void run() {
    			Platform.runLater(() -> pause());
    		}
    	};
    	
    	delayPauseTimer.schedule(delayPauseTask, new Date(System.currentTimeMillis() + delay));
    }
    
    
    private void delayNextScene(int delay) {
    	
    	Timer delayNextSceneTimer = new Timer();
    	TimerTask delaySceneTask = new TimerTask() {
    		
    		@Override
    		public void run() {
    			Platform.runLater(() -> startTimer());
    		}
    	};
    	
    	delayNextSceneTimer.schedule(delaySceneTask, new Date(System.currentTimeMillis() + delay));
    	
    }
    
    
    private void delayNextLevel(int delay) {
    	
    	Timer delayNextLevelTimer = new Timer();
    	TimerTask delayNextLevelTask = new TimerTask() {
    		
    		@Override
    		public void run() {
    			Platform.runLater(() -> startNextLevel());
    		}
    	};
    	
    	delayNextLevelTimer.schedule(delayNextLevelTask, new Date(System.currentTimeMillis() + delay));    	
    }
    
    
    private void delayMainScene(int delay) {
    	
    	Timer delayMainSceneTimer = new Timer();
    	TimerTask delayMainSceneTask = new TimerTask() {
			
			@Override
			public void run() {
				soundManager.bgmFadeIn();
				Platform.runLater(() -> viewManager.setToMainScene());
			}
		};
    	
		delayMainSceneTimer.schedule(delayMainSceneTask, new Date(System.currentTimeMillis() + delay));
		
    }
    
    
    public void startLevel(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        this.startTimer();
        delayPause(10);
        soundManager.playCountDownMusic();
        delayNextScene(3500);
        
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            @SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line);
            
            columnCount = 0;
            while (lineScanner.hasNext()) {
                lineScanner.next();
                columnCount++;
            }
            rowCount++;
        }
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        
		this.player = new PlayerModel(grid);

		
		//noEnemies should be decided by the number of enemies declared in level file
        noEnemies = 2;
        enemies = new ArrayList<EnemyAIModel>();
    	for(int i = 0; i < noEnemies; i++) {
    		EnemyAIModel buffer = new EnemyAIModel(grid);
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
                    case 'S':
                    	thisValue = CellValue.COIN;
                    	smalldot++;
                    	break;
                    case 'F':
                    	thisValue = CellValue.EMPTY;
                    	break;
                    default:
                    	if (Character.isDigit(valChar) && (valChar - '0' <= noEnemies)) {
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
        levelComplete = false;
        youWon = false;
        smalldot = 0;
        score = 0;
        level = 0;
        this.gameOverLabel.setText(String.format(""));
        startLevel(Controller.getCurrentLevel(0));
    }
    
    
    public void startNextLevel() {
        if (this.isLevelComplete()) {
            level++;
            rowCount = 0;
            columnCount = 0;
            youWon = false;
            levelComplete = false;
            startLevel(Controller.getCurrentLevel(level));
        }
    }
    
    
    public void step() {
        player.movePlayer();
        Point2D shipLocation = player.getLocation();
        Point2D shipVelocity = player.getVelocity();
        CellValue shipLocationCellValue = grid[(int) player.shipLocation.getX()][(int) shipLocation.getY()];

        if (shipLocationCellValue == CellValue.COIN) {
        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
        	soundManager = new SoundManager();
        	soundManager.playCoinCollectMusic();
        	smalldot--;
            score += 1;
        }
        
        //TODO: Add the power up buttons.
        
        for(int i = 0; i < noEnemies; i++) {
        	
        	if (shipLocation.equals(this.enemies.get(i).getLocation())) {
                gameOver = true;
                soundManager.playPlayerDeadMusic();
                shipVelocity = new Point2D(0,0);
            }
        	
        	this.enemies.get(i).moveEnemy(player.getLocation());
        	
    	}
        
        if(smalldot == 0) {	
        	youWon = true;
        }

    }
    
    
    private void update() {
    	player.setGameGrid(grid);
    	this.step();
        
        this.gameView.update(player,enemies);
        
        this.scoreLabel.setText(String.format("Score: %d", score));
        this.levelLabel.setText(String.format("Level: %d", level + 1));
        if (gameOver) {
        	
        	this.gameOverLabel.setText(String.format("GAME OVER"));
            scoreBoard.writeScore(score);
			scoreBoard.ClearVBox();
            scoreBoard.setScoreVBox();
            pause();
            gameOver = false;
            score = 0;
            delayMainScene(2000);
            
        } else if (youWon) {
        	
        	if (level == 0) {
        		
        		this.gameOverLabel.setText(String.format("LEVEL 1 COMPLETED!"));
        		levelComplete = true;
        		pause();
        		delayNextLevel(1000);
        		
        	} else if(level == 1) {
        		
        		this.gameOverLabel.setText(String.format("LEVEL 2 COMPLETED!"));
        		levelComplete = true;
        		pause();
        		delayNextLevel(1000);
        		
        	} else if(level == 2) {
        		
        		this.gameOverLabel.setText(String.format("YOU WON!"));
        		scoreBoard.writeScore(score);
				scoreBoard.ClearVBox();
        		scoreBoard.setScoreVBox();
        		pause();
        		youWon = false;
        		levelComplete = false;
        		score = 0;
        		delayMainScene(2000);
        		
        	}
        	
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
        } else if (code == KeyCode.G) {
        	pause();
        	this.startNewGame();
        } else if (code == KeyCode.P) {
        	if (keyPressed % 2 == 0) {
        		pause();
        	} else {
        		soundManager.playCountDownMusic();
        		delayNextScene(3500);
        	}
        	keyPressed++; 
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
        this.paused = true;
    }
    

    public double getBoardWidth() {
        return GameView.CELL_WIDTH * this.gameView.getGvColumnCount();
    }
    

    public double getBoardHeight() {
        return (GameView.CELL_WIDTH * this.gameView.getGvRowCount()) + 100;
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
    
    
    public boolean isLevelComplete() {
    	return levelComplete;
    }
    
    
}
