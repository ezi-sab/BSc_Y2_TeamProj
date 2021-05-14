package view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.Buttons;
import model.InfoLabel;
import model.ScoreBoard;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;
import javafx.animation.AnimationTimer;
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
	
    private List<EnemyAIModel> enemies;
    // Store all the flying bullets
    private List<BulletModel> bullets;
    private Pair<Integer,Integer> playerPosition;
    private List<Pair<Point2D, Integer>> scatterPosition = new ArrayList<Pair<Point2D, Integer>>();
    private boolean[] isEnemyDead;
    private List<Pair<Integer,Integer>> enemyStartingPositions = new ArrayList<Pair<Integer, Integer>>();
    private Point2D[] enemyCurrentPositions;
    private AStarThread[] aStarThread;
    private Thread[] moveEnemyThread;
    
    // Change all level1, level2, level3 to level0 to make the game easier
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};
    private static final double FPS = 5;
    
    private CellValue[][] grid;
    
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private Label levelLabel;
    @FXML private HBox lifeBox;
    @FXML private HBox bulletBox;
    @FXML private GameView gameView;
    
    private VBox gamePausedVBox = new VBox();
    private Scene pauseScene = new Scene(gamePausedVBox, 1258, 814);
    
    private int rowCount;
    private int columnCount;
    private int noEnemies;
    private int playerDeadStep = -20;
    private int score = 0;
    private int level = 0;
    private int smalldot = 0;
    private int step = 0;
    private int playerLives = 3;
    private int playerBullet = 0;
    private int powerUpCoins = 0;
    private boolean gameRunning = false;
    private boolean gameOver = false;
    private boolean youWon = false;
    private boolean levelComplete = false;
    private boolean hidePlayer = false;
    private boolean chasePlayer = true;
    private ShipModel.Direction lastDirection = Direction.NONE;
    
    private Timer timer;
    
    private PlayerModel player;
    private static SoundManager soundManager = new SoundManager();
    private static ScoreBoard scoreBoard = new ScoreBoard();
    private ViewManager viewManager = new ViewManager();
    
	/**
	 * Initialises and starts the game with the level 1 from cold.
	 */
	public void initialize() {
    	String file = getCurrentLevel(0);
    	createLifeHBox();
    	createBulletBox();
    	startLevel(file, 250);
    }
	
	/**
	 * A TimerTask for the gameplay and the FPS is set to 100.0 frames per ms.
	 */
	private void startTimer() {
        this.timer = new Timer();
        
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(() -> update());
            }
        };
        
        long frameTimeInMilliseconds = (long) (500.0 / FPS);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }
	
	/**
	 * TimerTask for pausing the screen and scene time for further scenes.
	 * Timer is paused with supplied argument value.
	 * @param delay time that should pause until next scene.
	 */
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
	
	/**
	 * TimerTask for delaying the screen and scene time for further scenes.
	 * Timer is delayed with supplied argument value.
	 * @param delay time that should delay until next scene.
	 */
	private void delayNextScene(int delay) {
    	
    	Timer delayNextSceneTimer = new Timer();
    	TimerTask delaySceneTask = new TimerTask() {
    		
    		@Override
    		public void run() {
    			Platform.runLater(() -> {    				
    				gameRunning = true;
    				startTimer();
    			});
    		}
    	};
    	
    	delayNextSceneTimer.schedule(delaySceneTask, new Date(System.currentTimeMillis() + delay));
    	
    }
	
	/**
	 * TimerTask for delaying the screen and scene time after a level finishes.
	 * Timer is delayed with supplied argument value.
	 * And a player game countdown music is played.
	 * @param delay time that should delay for next level to appear.
	 */
    private void delayNextLevel(int delay) {
    	
    	Timer delayNextLevelTimer = new Timer();
    	TimerTask delayNextLevelTask = new TimerTask() {
    		
    		@Override
    		public void run() {
    			Platform.runLater(() -> {
    				soundManager.playCountDownMusic();
    				startNextLevel();
   				});
    		}
    	};
    	
    	delayNextLevelTimer.schedule(delayNextLevelTask, new Date(System.currentTimeMillis() + delay));    	
    }
    
	/**
	 * TimerTask for delaying the screen and scene time after countdown finishes and before level game starts.
	 * Timer is delayed with supplied argument value.
	 * And a player game fadeIn music is played.
	 * @param delay time that should delay for before game starts.
	 */
    private void delayMainScene(int delay) {
    	
    	Timer delayMainSceneTimer = new Timer();
    	TimerTask delayMainSceneTask = new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(() -> {
					viewManager.setToMainScene();
				});
			}
		};
    	
		delayMainSceneTimer.schedule(delayMainSceneTask, new Date(System.currentTimeMillis() + delay));
		
    }
    
	/**
	 * Constructs and draws the game level by considering the text file.
	 * Adds the necessary blocks , players, enemies , powerups, lasers in the level scene.
	 * Starting points for every character is set.
	 * @param fileName for the game level that should be displayer for player.
	 * @param delay time after a level finishes and before a new level starts.
	 */
	public void startLevel(String fileName, int delay) {
    	
        File file = new File(fileName);
        Scanner scanner = null;
        this.startTimer();
        delayPause(10);
        delayNextScene(delay);
        
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        columnCount = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            @SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line);
            
            rowCount = 0;
            while (lineScanner.hasNext()) {
                lineScanner.next();
                rowCount++;
            }
            columnCount++;
        }
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        
		this.player = new PlayerModel(grid);
		
		if (level == 0) {			
			noEnemies = 2;
		} else {
			noEnemies = 3;
		}
		
        enemyCurrentPositions = new Point2D[noEnemies];
        aStarThread = new AStarThread[noEnemies];
        moveEnemyThread = new Thread[noEnemies];
        enemies = new ArrayList<EnemyAIModel>();
        isEnemyDead = new boolean[noEnemies];
    	for(int i = 0; i < noEnemies; i++) {
    		EnemyAIModel buffer = new EnemyAIModel(grid);
    		this.enemies.add(buffer);
    	}
    	bullets = new ArrayList<>();
        int column = 0;
        
        while (scanner2.hasNextLine()) {
        	int row = 0;
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
                        playerPosition = new Pair<Integer,Integer> (row, column);
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
                    case 'L':
                    	thisValue = CellValue.LIFE;
                    	scatterPosition.add(new Pair<Point2D, Integer>(new Point2D(row, column), 0));
                    	powerUpCoins++;
                    	break;
                    case 'U':
                    	thisValue = CellValue.POWERUP;
                    	scatterPosition.add(new Pair<Point2D, Integer>(new Point2D(row, column), 0));
                    	powerUpCoins++;
                    	break;
                    case 'F':
                    	thisValue = CellValue.EMPTY;
                    	break;
                    default:
                    	if (Character.isDigit(valChar) && (valChar - '0' <= noEnemies)) {
	                    	thisValue = CellValue.ENEMY1STARTINGPOINT;
	                    	int valInt = valChar - '0' - 1;
	                    	enemies.get(valInt).setLocation(new Point2D(row,column));
	                    	enemyStartingPositions.add(new Pair<Integer,Integer> (row, column));
	            	        enemies.get(valInt).setVelocity(new Point2D(0, 0));
	            	        enemies.get(valInt).setCurrentDirection(Direction.NONE);
	            	        enemies.get(valInt).setLastDirection(Direction.NONE);
	            	        isEnemyDead[valInt] = false;
                    	}
                    	else {
                    		thisValue = CellValue.EMPTY;
                    	}
                }
                grid[row][column] = thisValue;
                row++;
            }
            column++;
        }
        
    }
	
	/**
	 * Create a stub and cold start of the game.
	 * Initialises all values to the starting values.
	 * Called everytime a game started.
	 */
	public void startNewGame(){
		
        rowCount = 0;
        columnCount = 0;
        noEnemies = 0;
        playerDeadStep = -20;
        score = 0;
        level = 0;
        smalldot = 0;
        step = 0;
        playerLives = 3;
        playerBullet = 0;
        powerUpCoins = 0;
        gameRunning = false;
        gameOver = false;
        levelComplete = false;
        youWon = false;
        createLifeHBox();
        createBulletBox();
        soundManager.playCountDownMusic();
        this.gameOverLabel.setText(String.format(""));
        startLevel(Controller.getCurrentLevel(0), 3500);
        
    }
	

	/**
	 * Create a next level of the game after first level completes..
	 * Initialises all values to the starting values.
	 * Called everytime a previous level finishes.
	 */
	public void startNextLevel() {
		
        if (this.isLevelComplete()) {
            level++;
            rowCount = 0;
            columnCount = 0;
            noEnemies = 0;
            playerDeadStep = -20;
            smalldot = 0;
            step = 0;
            playerLives = 3;
            playerBullet = 0;
            powerUpCoins = 0;
            gameRunning = false;
            gameOver = false;
            youWon = false;
            levelComplete = false;
            createLifeHBox();
            createBulletBox();
            this.gameOverLabel.setText(String.format(""));
            startLevel(Controller.getCurrentLevel(level), 3500);
        }
        
    }
	
	/**
	 * Counts the steps of player and enemies.
	 * Each move is a step indeed.
	 * At each step verifies the state of the game and collect the power-ups, coins and execute methods.
	 * Decides the player dead scenarios.
	 * And enemies scattering positions in a quadrant basis for smoothening enemy AI moves.
	 */
	public void step() {
    	
    	step += 1;
        checkBullet();
        
    	if (step % 2 == 1) {
    		if (step % 300 <= 200) {
    			chasePlayer = true;
    		} else {
    			chasePlayer = false;
    		}
	        player.movePlayer();
	        Point2D shipLocation = player.getLocation();
	        CellValue shipLocationCellValue = grid[(int) shipLocation.getX()][(int) shipLocation.getY()];
	
	        if (shipLocationCellValue == CellValue.COIN) {
	        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
	        	soundManager.playCoinPickUpMusic();
	        	smalldot--;
	            score += 1;
	        }
	        
	        if (shipLocationCellValue == CellValue.LIFE) {
	        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
	        	powerUpCoins--;
	        	soundManager.playLifePickUpMusic();
	        	if (playerLives != 3) {
	        		playerLives++;
	        		createLifeHBox();
	        	}
	        	score += 25;
	        }
	        
	        if (shipLocationCellValue == CellValue.POWERUP) {
	        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
	        	powerUpCoins--;
	        	soundManager.playLaserPickUpMusic();
	        	if (playerBullet != 3) {
	        		playerBullet++;
	        		createBulletBox();
	        	}
	        	score += 10;
	        }
	        
	        for(int i = 0; i < noEnemies; i++) {
	        	enemyCurrentPositions[i] = new Point2D(enemies.get(i).getLocation().getX(), enemies.get(i).getLocation().getY());
	        }
	        
	        for(int i = 0; i < noEnemies; i++) {
	        	
	        	if (playerDeadStep <= step) {
	        		
	        		playerDeadStep = -20;
	        		
	        		if (shipLocation.equals(enemyCurrentPositions[i])) {
	        			gameRunning = false;
	        			playerLives--;
	        			playerDeadStep = step + 20;
	        			createLifeHBox();
	        			if(playerLives != 0) {
	        				player.setLocation(new Point2D(playerPosition.getKey(), playerPosition.getValue()));
	        				pause();
	        				delayNextScene(1000);
	        				soundManager.playPlayerExplodeMusic();
	        			} else {
	        				break;
	        			}
	        		}
	        		
	        	}
	        	
	        }
	        
	        for(int i = 0; i < noEnemies; i++) {
	        	aStarThread[i] = new AStarThread();
	        	moveEnemyThread[i] = new Thread(aStarThread[i]);
	        }
	        
	        for(int i = 0; i < noEnemies; i++) {
	        	
	        	if (playerDeadStep <= step) {
	        		
	        		if(playerLives != 0) {
	        			if(step != 1) {
	        				if (chasePlayer) {
	        					if (!(isEnemyDead[i])) {
	        						if (i == 0) {
	        							aStarThread[i].setEnemyAIModel(enemies.get(i));
	        							aStarThread[i].setPlayerLocation(shipLocation);
	        							moveEnemyThread[i].start();
	        						} else if (i == 1) {
	        							Point2D predictedPlayerVelocity = player.changeVelocity((player.currentDirection == Direction.NONE ? Direction.UP : player.currentDirection));
	        							Point2D predictedPlayerLocation = player.shipLocation.add(predictedPlayerVelocity);
	        							aStarThread[i].setEnemyAIModel(enemies.get(i));
	        							if (predictedPlayerLocation.getX() >= 0 && predictedPlayerLocation.getX() < 37 && predictedPlayerLocation.getY() >= 0 && predictedPlayerLocation.getY() < 21) {
	        								if (grid [(int) predictedPlayerLocation.getX()] [(int) predictedPlayerLocation.getY()] != CellValue.BLOCK) {	        						
	        									aStarThread[i].setPlayerLocation(predictedPlayerLocation);
	        								} else {
	        									aStarThread[i].setPlayerLocation(shipLocation);
	        								}
	        							} else {
        									aStarThread[i].setPlayerLocation(shipLocation);
	        							}
	        							moveEnemyThread[i].start();
	        						} else {
	        							Direction cellBehind;
	        							if (player.currentDirection == Direction.UP) {
	        								cellBehind = Direction.DOWN;
	        							} else if (player.currentDirection == Direction.RIGHT) {
	        								cellBehind = Direction.LEFT;
	        							} else if (player.currentDirection == Direction.DOWN) {
	        								cellBehind = Direction.UP;
	        							} else if (player.currentDirection == Direction.LEFT) {
	        								cellBehind = Direction.RIGHT;
	        							} else {
	        								cellBehind = Direction.UP;
	        							}
	        							Point2D predictedPlayerVelocity = player.changeVelocity(cellBehind);
	        							Point2D predictedPlayerLocation = player.shipLocation.add(predictedPlayerVelocity);
	        							aStarThread[i].setEnemyAIModel(enemies.get(i));
	        							if (predictedPlayerLocation.getX() >= 0 && predictedPlayerLocation.getX() < 37 && predictedPlayerLocation.getY() >= 0 && predictedPlayerLocation.getY() < 21) {
	        								if (grid [(int) predictedPlayerLocation.getX()] [(int) predictedPlayerLocation.getY()] != CellValue.BLOCK) {	        						
	        									aStarThread[i].setPlayerLocation(predictedPlayerLocation);
	        								} else {
	        									aStarThread[i].setPlayerLocation(shipLocation);
	        								}
	        							} else {
        									aStarThread[i].setPlayerLocation(shipLocation);
	        							}
	        							moveEnemyThread[i].start();
	        						}
	        					}
	        					
	        					for (int j = 0; j < scatterPosition.size(); j++) {
	        						scatterPosition.set(j, new Pair<Point2D, Integer>(scatterPosition.get(j).getKey(), 0));
	        					}
	        					
	        				} else {
	        					if (enemyStartingPositions.get(i).getKey() <= 18) {
	        						int isAllPlacesReached = 0;
	        						int totalPlacesOnLeft = 0;
	        						
	        						for (int j = 0; j < scatterPosition.size(); j++) {
	        							if (scatterPosition.get(j).getKey().getX() <= 18) {
	        								totalPlacesOnLeft++;	        								
	        							}
	        						}
	        						
	        						for (int j = 0; j < scatterPosition.size(); j++) {
	        							
	        							if ((enemyCurrentPositions[i].getX() == scatterPosition.get(j).getKey().getX()) && (enemyCurrentPositions[i].getY() == scatterPosition.get(j).getKey().getY()) && (scatterPosition.get(j).getValue() == 0)) {
	        								scatterPosition.set(j, new Pair<Point2D, Integer>(enemies.get(i).getLocation(), 1));
	        							}
	        							
	        							if ((scatterPosition.get(j).getKey().getX() <= 18) && (scatterPosition.get(j).getValue() == 1)) {
	        								isAllPlacesReached++;
	        							}
	        							
	        							if (isAllPlacesReached == totalPlacesOnLeft) {
	        								for (int k = 0; k < scatterPosition.size(); k++) {
	        									if (scatterPosition.get(k).getKey().getX() <= 18) {
	        										scatterPosition.set(k, new Pair<Point2D, Integer>(scatterPosition.get(k).getKey(), 0));
	        										isAllPlacesReached = 0;
	        									}
	        								}
	        								break;
	        							}
	        							
	        						}
	        						        						
	        						for (int j = 0; j < scatterPosition.size(); j++) {
	        							if ((scatterPosition.get(j).getKey().getX() <= 18) && (scatterPosition.get(j).getValue() == 0)) {
	        								aStarThread[i].setEnemyAIModel(enemies.get(i));
	        								aStarThread[i].setPlayerLocation(scatterPosition.get(j).getKey());
	        								moveEnemyThread[i].start();
	        								break;
	        							}
	        						}	        						
	        						
	        					} else {
	        						int isAllPlacesReached = 0;
	        						int totalPlacesOnRight = 0;
	        						
	        						for (int j = 0; j < scatterPosition.size(); j++) {
	        							if (scatterPosition.get(j).getKey().getX() > 18) {
	        								totalPlacesOnRight++;	        								
	        							}
	        						}
	        						
	        						for (int j = 0; j < scatterPosition.size(); j++) {
	        							
	        							if ((enemyCurrentPositions[i].getX() == scatterPosition.get(j).getKey().getX()) && (enemyCurrentPositions[i].getY() == scatterPosition.get(j).getKey().getY()) && (scatterPosition.get(j).getValue() == 0)) {
	        								scatterPosition.set(j, new Pair<Point2D, Integer>(enemies.get(i).getLocation(), 1));
	        							}
	        							
	        							if ((scatterPosition.get(j).getKey().getX() > 18) && (scatterPosition.get(j).getValue() == 1)) {
	        								isAllPlacesReached++;
	        							}
	        							
	        							if (isAllPlacesReached == totalPlacesOnRight) {
		        							for (int k = 0; k < scatterPosition.size(); k++) {
		        								if (scatterPosition.get(k).getKey().getX() > 18) {
		        									scatterPosition.set(k, new Pair<Point2D, Integer>(scatterPosition.get(k).getKey(), 0));
		        									isAllPlacesReached = 0;
		        								}
		    	        					}
		        							break;
		        						}
	        							
	        						}
	        						
	        						for (int j = 0; j < scatterPosition.size(); j++) {
	        							if ((scatterPosition.get(j).getKey().getX() > 18) && (scatterPosition.get(j).getValue() == 0)) {
	        								aStarThread[i].setEnemyAIModel(enemies.get(i));
	        								aStarThread[i].setPlayerLocation(scatterPosition.get(j).getKey());
	        								moveEnemyThread[i].start();
	        								break;
	        							}
	        						}
	        						
	        					}
	        				}
	        			}
	        		}
	        	} else {
	        		
	        		if (enemyCurrentPositions[i].getX() <= 18 && enemyCurrentPositions[i].getY() <= 11) {
	        			aStarThread[i].setEnemyAIModel(enemies.get(i));
						aStarThread[i].setPlayerLocation(new Point2D(1, 2));
	        		} else if (enemyCurrentPositions[i].getX() > 18 && enemyCurrentPositions[i].getY() <= 11) {
	        			aStarThread[i].setEnemyAIModel(enemies.get(i));
						aStarThread[i].setPlayerLocation(new Point2D(35, 2));
	        		} else if (enemyCurrentPositions[i].getX() <= 18 && enemyCurrentPositions[i].getY() > 11) {
	        			aStarThread[i].setEnemyAIModel(enemies.get(i));
						aStarThread[i].setPlayerLocation(new Point2D(1, 18));
	        		} else if (enemyCurrentPositions[i].getX() > 18 && enemyCurrentPositions[i].getY() > 11) {
	        			aStarThread[i].setEnemyAIModel(enemies.get(i));
						aStarThread[i].setPlayerLocation(new Point2D(35, 18));
	        		}
	        		
	        		moveEnemyThread[i].start();
	        		
	        	}
	        }
	        
	        for(int i = 0; i < noEnemies; i++) {
	        	
	        	if (playerDeadStep <= step) {
	        		
	        		playerDeadStep = -20;
	        		
	        		if(playerLives != 0) {
	        			
	        			if (!(isEnemyDead[i])) {
	        				
	        				while (moveEnemyThread[i].isAlive()) {
	        				}
	        				
	        				if(step != 1) {	        				
	        					enemies.set(i, aStarThread[i].getEnemyAIModel());
	        				}
	        				
	        				if (shipLocation.equals(enemies.get(i).getLocation())) {
	        					gameRunning = false;
	        					playerLives--;
	        					playerDeadStep = step + 20;
	        					createLifeHBox();
	        					if(playerLives != 0) {                	
	        						player.setLocation(new Point2D(playerPosition.getKey(), playerPosition.getValue()));
	        						pause();
	        						delayNextScene(1000);
	        						soundManager.playPlayerExplodeMusic();
	        					} else {
	        						break;
	        					}
	        				}
	        				
	        			} else {
	        				isEnemyDead[i] = false;	        				
	        			}
	        		}
	        	}
	        }
    	}
    	
    	
    	if(playerLives == 0) {
        	gameOver = true;
        }
    	
        if(smalldot == 0 && powerUpCoins == 0) {
        	youWon = true;
        }
        
    }
	
	/**
	 * Function that implements shooting mechanism .
	 * Shooting while it sets the location of player and direction.
	 * @param ship selected that implements shoot mechanism.
	 */
	// generate a new bullet according to enemy or player's location and direction
    public void shoot(ShipModel ship) {
    	
		Point2D predictedShipVelocity = ship.changeVelocity(ship.getCurrentDirection());
		Point2D predictedShipLocation = ship.shipLocation.add(predictedShipVelocity);
		
		if (grid[(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
			
			BulletModel bullet = new BulletModel(grid);
			bullet.setLocation(ship.shipLocation.add(ship.changeVelocity(ship.getCurrentDirection())));
			bullet.setCurrentDirection(ship.getCurrentDirection());
			bullets.add(bullet);
			
		}
		
    }
    
	/**
	 * Function that checks bullet has shot the enemy .
	 * Shooting while it sets the location of player and direction.
	 * Plays enemy shoot music when bullet hits the enemy.
	 * Score of player gets increased.
	 */
    private void checkBullet() {
    	
        for(int i = 0; i < bullets.size(); i++) {
        	
			boolean disappear = false;
			
            for(int j = 0; j < noEnemies; j++) {
              	if (this.enemies.get(j).getLocation().equals(this.bullets.get(i).getLocation())) {
               		score += 100;
               		soundManager.playEnemyExplodeMusic();
               		enemies.get(j).setLocation(new Point2D (enemyStartingPositions.get(j).getKey(), enemyStartingPositions.get(j).getValue()));
               		isEnemyDead[j] = true;
                    disappear = true;
                    break;
                }
            }
            
        	if (!disappear) {
				if (bullets.get(i).flyBullet()) {
		            for(int j = 0; j < noEnemies; j++) {
		               	if (this.enemies.get(j).getLocation().equals(this.bullets.get(i).getLocation())) {
		               		score += 100;
		               		soundManager.playEnemyExplodeMusic();
		               		enemies.get(j).setLocation(new Point2D (enemyStartingPositions.get(j).getKey(), enemyStartingPositions.get(j).getValue()));
		               		isEnemyDead[j] = true;
		                    disappear = true;
		                    break;
		                }
		            }
				} else {
					disappear = true;
				}
			}
        	
        	if (disappear) {
				bullets.remove(i);
				i -= 1;
        	}
        	
    	}
    }
    
	/**
	 * Updates the scores , level and triggers the level completion.
	 * And keep track of the text displayed when player finishes levels.
	 * Game is set back to main scene when finished.
	 */
	private void update() {
		
    	player.setGameGrid(grid);
    	this.step();
    	
        if (playerDeadStep - step >= 0) {
        	
	        if ((playerDeadStep-step) % 2 == 0) {
	        	hidePlayer = true;
	        } else {
	        	hidePlayer = false;
	        }
	        
        }
        this.gameView.update(player, enemies, bullets);
        this.scoreLabel.setText(String.format("Score: %d", score));
        this.levelLabel.setText(String.format("Level: %d", level + 1));
        
        if (gameOver) {
        	
        	this.gameOverLabel.setText(String.format("GAME OVER"));
        	pause();
            scoreBoard.writeScore(viewManager.getPlayerName(), score);
            scoreBoard.setScoreVBox();
            soundManager.playGameOverMusic();
            gameRunning = false;
            gameOver = false;
            score = 0;
            delayMainScene(3000);
            
        } else if (youWon) {
        	
        	if (level == 0) {
        		
        		this.gameOverLabel.setText(String.format("LEVEL 1 COMPLETED!"));
        		levelComplete = true;
        		pause();
        		soundManager.playLevelCompletedMusic();
        		gameRunning = false;
        		delayNextLevel(2000);
        		
        	} else if(level == 1) {
        		
        		this.gameOverLabel.setText(String.format("LEVEL 2 COMPLETED!"));
        		levelComplete = true;
        		pause();
        		soundManager.playLevelCompletedMusic();
        		gameRunning = false;
        		delayNextLevel(2000);
        		
        	} else if(level == 2) {
        		
        		this.gameOverLabel.setText(String.format("YOU WON!"));
        		pause();
        		scoreBoard.writeScore(viewManager.getPlayerName(), score);
        		scoreBoard.setScoreVBox();
        		soundManager.playGameWonMusic();
        		gameRunning = false;
        		youWon = false;
        		levelComplete = false;
        		gameOver = false;
        		score = 0;
        		level = 0;
        		smalldot = 0;
        		powerUpCoins = 0;
        		playerLives = 3;
        		delayMainScene(3000);
        		
        	}
        }
    }
	
	/**
	 * Controls the player movement.
	 * Implements two way keyboard controls for player.
	 * @override handle from key events comes from javafx
	 * @param keyEvent gets the jkeys pressed by player
	 */
	@Override
    public void handle(KeyEvent keyEvent) {
    	if (gameRunning) {    		
    		boolean keyRecognized = true;
    		KeyCode code = keyEvent.getCode();
    		ShipModel.Direction direction = lastDirection;
    		if (code == KeyCode.RIGHT || code == KeyCode.D) {
    			direction = ShipModel.Direction.RIGHT;
    			lastDirection = direction;
    		} else if (code == KeyCode.DOWN || code == KeyCode.S) {
    			direction = ShipModel.Direction.DOWN;
    			lastDirection = direction;
    		} else if (code == KeyCode.LEFT || code == KeyCode.A) {
    			direction = ShipModel.Direction.LEFT;
    			lastDirection = direction;
    		} else if (code == KeyCode.UP || code == KeyCode.W) {
    			direction = ShipModel.Direction.UP;
    			lastDirection = direction;
    		} else if (code == KeyCode.SPACE) {
    			if (playerBullet != 0) {
    				if (player.getCurrentDirection() != Direction.NONE) {
    					direction = player.getCurrentDirection();
    					soundManager.playLaserShootMusic();
    					shoot(player);
    					playerBullet--;
    					createBulletBox();
    				}
    			} else {
    				soundManager.playLaserEmptyMusic();
    			}
    		} else if (code == KeyCode.P) {
    			pause();
    			gameRunning = false;
    			createPauseScene();
    		} else if (code == KeyCode.G) {
    			direction = ShipModel.Direction.NONE;
    			lastDirection = direction;
    			pause();
    			this.startNewGame();
    		} else if (code == KeyCode.ESCAPE) {
    			pause();
    			gameRunning = false;
    			delayMainScene(0);
    		} else {
    			keyRecognized = false;
    		}
    		
    		if (keyRecognized) {
    			keyEvent.consume();
    			player.setCurrentDirection(direction);
    		}
    	}
    }
	
	/**
	 * Creates the HBox for player lives.
	 * Reduces by 1 image each time player dies.
	 * Increased by 1 image each time player collects the life coin
	 */
	public void createLifeHBox() {
    	
		lifeBox.getChildren().removeAll(lifeBox.getChildren());
    	
    	Image shipImage = new Image(viewManager.getChosenShip().getShipUrl(), 40, 40, true, false);
    	Image blackImage = new Image("/resources/Images/PlayerShip-NoLife-image.png", 40, 40, true, false);
    	
    	if (playerLives == 3) {
    		
    		ImageView life1 = new ImageView(shipImage);
    		ImageView life2 = new ImageView(shipImage);
    		ImageView life3 = new ImageView(shipImage);
    		lifeBox.getChildren().add(life1);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life2);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life3);
    		
    	} else if (playerLives == 2) {
    		
    		ImageView life1 = new ImageView(shipImage);
    		ImageView life2 = new ImageView(shipImage);
    		ImageView life3 = new ImageView(blackImage);
    		lifeBox.getChildren().add(life1);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life2);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life3);
    		
    	} else if (playerLives == 1) {
    		
    		ImageView life1 = new ImageView(shipImage);
    		ImageView life2 = new ImageView(blackImage);
    		ImageView life3 = new ImageView(blackImage);
    		lifeBox.getChildren().add(life1);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life2);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life3);
    		
    	} else if (playerLives == 0) {
    		
    		ImageView life1 = new ImageView(blackImage);
    		ImageView life2 = new ImageView(blackImage);
    		ImageView life3 = new ImageView(blackImage);
    		lifeBox.getChildren().add(life1);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life2);
    		lifeBox.setSpacing(10);
    		lifeBox.getChildren().add(life3);
    		
    	}
    	
    }
	
	/**
	 * Creates the HBox for bullet count that to be shot by player.
	 * Reduces by 1 image each time player shoots a bullet.
	 * Increased by 1 image each time player collects the power-up coin.
	 */
    public void createBulletBox() {
    	
    	bulletBox.getChildren().removeAll(bulletBox.getChildren());
    	
    	Image redLaserImage = new Image("/resources/Images/Laser-Holder-image.png", 20, 40, false, false);
    	Image blackLaserImage = new Image("/resources/Images/Laser-Empty-image.png", 20, 40, false, false);
    	bulletBox.setLayoutX(1000);
    	bulletBox.setLayoutY(5);
    	
    	if (playerBullet == 3) {
    		
    		ImageView laser1 = new ImageView(redLaserImage);
    		ImageView laser2 = new ImageView(redLaserImage);
    		ImageView laser3 = new ImageView(redLaserImage);
    		
    		bulletBox.getChildren().add(laser1);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser2);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser3);
    		
    	} else if (playerBullet == 2) {
    		
    		ImageView laser1 = new ImageView(redLaserImage);
    		ImageView laser2 = new ImageView(redLaserImage);
    		ImageView laser3 = new ImageView(blackLaserImage);
    		
    		bulletBox.getChildren().add(laser1);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser2);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser3);
    		
    	} else if (playerBullet == 1) {
    		
    		ImageView laser1 = new ImageView(redLaserImage);
    		ImageView laser2 = new ImageView(blackLaserImage);
    		ImageView laser3 = new ImageView(blackLaserImage);
    		
    		bulletBox.getChildren().add(laser1);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser2);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser3);
    		
    	} else if (playerBullet == 0) {
    		
    		ImageView laser1 = new ImageView(blackLaserImage);
    		ImageView laser2 = new ImageView(blackLaserImage);
    		ImageView laser3 = new ImageView(blackLaserImage);
    		
    		bulletBox.getChildren().add(laser1);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser2);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser3);
    		
    	}
    	
    }
    
    public void createPauseScene() {
    	
    	VBox pauseMenuVBox = new VBox();
    	gamePausedVBox.getChildren().add(pauseMenuVBox);
    	gamePausedVBox.setAlignment(Pos.CENTER);
    	
    	viewManager.getMainStage().setScene(pauseScene);
    	
    	Buttons resumeButton, newGameButton, settingsButton, helpButton, exitButton;
    	
    	resumeButton = new Buttons("RESUME");
    	resumeButton.setAlignment(Pos.CENTER);
    	resumeButton.setOnAction(new EventHandler<ActionEvent>() {
    		
    		@Override
    		public void handle(ActionEvent event) {
    			soundManager.playMenuOpenMusic();
    			viewManager.getMainStage().setScene(gameView.getGameScene());
    			gameRunning = false;
    			soundManager.playCountDownMusic();
    			delayNextScene(3500);
    			gamePausedVBox.getChildren().removeAll(gamePausedVBox.getChildren());
    		}
		});
    	
    	pauseMenuVBox.setSpacing(20);
    	
    	newGameButton = new Buttons("NEW GAME");
    	newGameButton.setAlignment(Pos.CENTER);
    	newGameButton.setOnAction(new EventHandler<ActionEvent>() {
    		
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				viewManager.getMainStage().setScene(gameView.getGameScene());
    			gameRunning = false;
    			Direction direction = ShipModel.Direction.NONE;
    			lastDirection = direction;
    			gamePausedVBox.getChildren().removeAll(gamePausedVBox.getChildren());
    			startNewGame();
			}
		});
    	
    	pauseMenuVBox.setSpacing(20);
    	
    	helpButton = new Buttons("HELP");
    	helpButton.setAlignment(Pos.CENTER);
    	helpButton.setOnAction(new EventHandler<ActionEvent>() {
    		
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();				
				AnchorPane helpSection = new AnchorPane();
				helpSection.setPrefWidth(600);
				helpSection.setPrefHeight(600);
				pauseMenuVBox.getChildren().removeAll(pauseMenuVBox.getChildren());
				
				BackgroundImage image = new BackgroundImage(new Image("/resources/Images/Panel-Blue-image.png", 600, 500, false, true),
						BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
				
				helpSection.setBackground(new Background(image));
				
				pauseMenuVBox.getChildren().add(helpSection);
				InfoLabel help = new InfoLabel("HELP");
				help.setLayoutX(450);
				help.setLayoutY(75);
				GridPane helpGrid = new GridPane();
				helpGrid.setLayoutX(425);
				helpGrid.setLayoutY(150);
				helpGrid.setHgap(20);
				helpGrid.setVgap(20);
				
				ImageView playerShip = new ImageView(new Image("/resources/Images/PlayerShip-Red-image.png", 80, 80, true, false));
				ImageView enemyShip = new ImageView(new Image("/resources/Images/EnemyShip-1-image.png", 80, 80, true, false));
				ImageView laserPowerUp = new ImageView(new Image("/resources/Images/PowerUp-Laser-image.png", 40, 40, true, false));
				ImageView lifePowerUp = new ImageView(new Image("/resources/Images/PowerUp-Life-image.png", 40, 40, true, false));
				
				Label playerShipHelp = new Label("This is your ship. Choose colour from the \nPlay menu. Control it with arrow keys or W/S/A/D keys.");
				Label enemyShipHelp = new Label("These are enemy ships.\nAvoid them!");
				Label laserPowerUpHelp = new Label("The coins give you points,\nIF you can grab them!");
				Label lifePowerUpHelp = new Label("This is extra life.\nGrab it to gain an extra ship\nif you have less than three ships.");
				
				AnimationTimer animationTimer = new AnimationTimer() {
					@Override
					public void handle(long now) {
						enemyShip.setRotate(90+now/10000000l);
						playerShip.setRotate(-now/10000000l);
					}
				};
				animationTimer.start();
				
				helpGrid.add(playerShip, 0, 0);
				helpGrid.add(playerShipHelp, 1, 0);
				helpGrid.add(enemyShip, 0, 1);
				helpGrid.add(enemyShipHelp, 1, 1);
				helpGrid.add(laserPowerUp, 0, 2);
				helpGrid.add(laserPowerUpHelp, 1, 2);
				helpGrid.add(lifePowerUp, 0, 3);
				helpGrid.add(lifePowerUpHelp, 1, 3);
				
				Buttons backButton = new Buttons("BACK");
				backButton.setLayoutX(530);
				backButton.setLayoutY(465);
				backButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event1) {
						soundManager.playMenuOpenMusic();
						pauseMenuVBox.getChildren().removeAll(pauseMenuVBox.getChildren());
						createPauseScene();
					}
				});
				
				helpSection.getChildren().addAll(help, helpGrid, backButton);
				help.setAlignment(Pos.CENTER);
				helpGrid.setAlignment(Pos.CENTER);
				backButton.setAlignment(Pos.CENTER);
			}
		});
    	
    	pauseMenuVBox.setSpacing(20);
    	
    	settingsButton = new Buttons("SETTINGS");
    	settingsButton.setAlignment(Pos.CENTER);
    	settingsButton.setOnAction(new EventHandler<ActionEvent>() {
    		
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				pauseMenuVBox.getChildren().removeAll(pauseMenuVBox.getChildren());
				AnchorPane musicControls = new AnchorPane();
				pauseMenuVBox.getChildren().add(musicControls);
				
				musicControls.setPrefWidth(600);
				musicControls.setPrefHeight(475);
				
				BackgroundImage image = new BackgroundImage(new Image("/resources/Images/Panel-Blue-image.png", 600, 475, false, false),
						BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
				
				musicControls.setBackground(new Background(image));				
				
				InfoLabel chooseBGMusicOption = new InfoLabel("BACKGROUND MUSIC");
				chooseBGMusicOption.setLayoutX(450);
				chooseBGMusicOption.setLayoutY(35);
				musicControls.getChildren().add(chooseBGMusicOption);
				musicControls.getChildren().add(soundManager.bgmVolumeShips());
				soundManager.getBgmBox().setLayoutX(383);
				soundManager.getBgmBox().setLayoutY(110);
				
				InfoLabel chooseIGMusicOption = new InfoLabel("IN-GAME MUSIC");
				chooseIGMusicOption.setLayoutX(450);
				chooseIGMusicOption.setLayoutY(220);
				musicControls.getChildren().add(chooseIGMusicOption);
				musicControls.getChildren().add(soundManager.igmVolumeShips());
				soundManager.getIgmBox().setLayoutX(383);
				soundManager.getIgmBox().setLayoutY(295);
				
				Buttons backButton = new Buttons("BACK");
				backButton.setAlignment(Pos.CENTER);
				backButton.setLayoutX(530);
				backButton.setLayoutY(400);
				backButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event1) {
						soundManager.playMenuOpenMusic();
						soundManager.setBGMVolumeBeforeGame(soundManager.getBackGroundMusicVolume());
						pauseMenuVBox.getChildren().removeAll(pauseMenuVBox.getChildren());
						createPauseScene();
					}
				});
				
				musicControls.getChildren().add(backButton);
				
			}
		});
    	
    	pauseMenuVBox.setSpacing(20);
    	
    	exitButton = new Buttons("EXIT GAME");
    	exitButton.setAlignment(Pos.CENTER);
    	exitButton.setOnAction(new EventHandler<ActionEvent>() {
    		
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
    			gameRunning = false;
    			pauseMenuVBox.getChildren().removeAll(pauseMenuVBox.getChildren());
    			delayMainScene(0);
			}
		});
    	
    	Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/Images/Space-BackGround-image.png"), 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		gamePausedVBox.setBackground(new Background(background));
		pauseMenuVBox.getChildren().addAll(resumeButton, newGameButton, helpButton, settingsButton, exitButton); 
		pauseMenuVBox.setAlignment(Pos.CENTER);
    	
    }
    
	/**
	 * Function that stops the timer and pauses the game.
	 *
	 */
	public void pause() {
        this.timer.cancel();
    }
	
	/**
	 * Gets the width of the Game window.
	 * Scene is set according to the size(width) of game window.
	 * @return double size of the game window and scene.
	 */
	public double getBoardWidth() {
        return GameView.CELL_WIDTH * this.gameView.getGvRowCount();
    }
	
	/**
	 * Gets the height of the Game window.
	 * Scene is set according to the size(height) of game window.
	 * @return double size of the game window and scene.
	 */
    public double getBoardHeight() {
        return (GameView.CELL_WIDTH * this.gameView.getGvColumnCount()) + 100;
    }
    
	/**
	 * Gets the Game Grid with point values and coordinates.
	 * @return grid of game
	 */
	public CellValue[][] getGrid(){
    	return grid;
    }
	
	/**
	 * Gets the current level and the URL file.
	 * @param x integer level file is is passed into  levelFiles
	 * @return String with the current level resource URL.
	 */
	public static String getCurrentLevel(int x) {
        return levelFiles[x];
    }
	
	/**
	 * Gets the Player Template model.
	 * @return PlayerModel
	 */
	public PlayerModel getPlayer() {
    	return this.player;
    }
	
	/**
	 * Gets the Enemy model.
	 * @return List of enemies of EnemyModel
	 */
	public List<EnemyAIModel> getEnemies() {
    	return this.enemies;
    }
	
	/**
	 * Functions return a is level completed or not.
	 * @return boolean whether is level complete.
	 */
	public boolean isLevelComplete() {
    	return levelComplete;
    }
	
	/**
	 * Functions return a is player hidden or not.
	 * @return boolean whether is player hidden.
	 */
    public boolean isPlayerHidden() {
    	return hidePlayer;
    }
    
}
