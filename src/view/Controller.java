package view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
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
	
	
    private List<EnemyAIModel> enemies;
    // Store all the flying bullets
    private List<BulletModel> bullets;
    private Pair<Integer,Integer> playerPosition;
    private List<Pair<Point2D, Integer>> scatterPosition = new ArrayList<Pair<Point2D, Integer>>();
    private boolean[] isEnemyDead;
    private List<Pair<Integer,Integer>> enemyPosition = new ArrayList<Pair<Integer,Integer>>();

    static private int FPS;
    static private int StepsPerSecond;
    
    // Change all level1, level2, level3 to level0 to make the game easier
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};
    
    private CellValue[][] grid;
    
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private Label levelLabel;
    @FXML private HBox lifeBox;
    @FXML private HBox bulletBox;
    @FXML private GameView gameView;
    
    static private int currentFrame;
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
    private boolean gamePaused = false;
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
    
    
    public void initialize() {
    	String file = getCurrentLevel(0);
    	createLifeHBox();
    	createBulletBox();
    	startLevel(file, 250);
    }
    	

    public Controller() {
    	currentFrame = 0;
    }


    
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
    			Platform.runLater(() -> {    				
    				gameRunning = true;
    				startTimer();
    			});
    		}
    	};
    	
    	delayNextSceneTimer.schedule(delaySceneTask, new Date(System.currentTimeMillis() + delay));
    	
    }
    
    
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
    
    
    private void delayMainScene(int delay) {
    	
    	Timer delayMainSceneTimer = new Timer();
    	TimerTask delayMainSceneTask = new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(() -> {
					soundManager.bgmFadeIn();
					viewManager.setToMainScene();
				});
			}
		};
    	
		delayMainSceneTimer.schedule(delayMainSceneTask, new Date(System.currentTimeMillis() + delay));
		
    }
    
    
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

		
		//noEnemies should be decided by the number of enemies declared in level file
        noEnemies = 1;
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
                        player.setNextDirection(Direction.NONE);
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
	                    	enemyPosition.add(new Pair<Integer,Integer> (row, column));
	            	        enemies.get(valInt).setVelocity(new Point2D(0, 0)); //changed from -1,0
	            	        enemies.get(valInt).setNextDirection(Direction.NONE);
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
    
    public Point2D roundShipLocation(ShipModel ship) {
    	Point2D shipLocation = new Point2D (Math.round(ship.getLocation().getX()),Math.round(ship.getLocation().getY())) ;
    	return shipLocation;
    }
    
    public void checkCoins(Point2D shipLocation) {
    	CellValue playerLocationCellValue = grid[(int) shipLocation.getX()][(int) shipLocation.getY()];
    	
    	if (playerLocationCellValue == CellValue.COIN) {
        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
        	soundManager.playCoinPickUpMusic();
        	smalldot--;
            score += 1;
        }
        
        if (playerLocationCellValue == CellValue.LIFE) {
        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
        	powerUpCoins--;
        	soundManager.playLifePickUpMusic();
        	if (playerLives != 3) {
        		playerLives++;
        		createLifeHBox();
        	}
        	score += 25;
        }
        
        if (playerLocationCellValue == CellValue.POWERUP) {
        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
        	powerUpCoins--;
        	soundManager.playLaserPickUpMusic();
        	if (playerBullet != 3) {
        		playerBullet++;
        		createBulletBox();
        	}
        	score += 10;
        }	
    }
    
    public void step() {
        
    	step += 1;

        player.setLastDirection(player.getCurrentDirection());
    	player.setCurrentDirection(player.getNextDirection());
        player.movePlayer(FPS, StepsPerSecond);
        
        player.setLocation(roundShipLocation(player));
        for(int i = 0; i < noEnemies; i++) {
        	enemies.get(i).setLocation(roundShipLocation(enemies.get(i)));;
        }
        for(int i = 0; i < bullets.size(); i++) {
        	
        	bullets.get(i).setLocation(roundShipLocation(bullets.get(i)));
        }
        
        
        
        
    	
		if (step % 200 <= 100) {
			chasePlayer = true;
		} else {
			chasePlayer = false;
		}
		
        	
        for(int i = 0; i < noEnemies; i++) {
        	
        	if (playerDeadStep <= step) {
        		
        		playerDeadStep = -20;
        		/*if (checkCollision(playerLocation,enemies.get(i).getLocation())) {
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
        		}*/
        		if(playerLives != 0) {
        			if(step != 1) {
        				if (chasePlayer) {
        					if (!(isEnemyDead[i])) {
        						if (i == 0) {
        							this.enemies.get(i).moveEnemyChaseMode(player.getLocation(), FPS, StepsPerSecond);
        						} else if (i == 1) {
        							Point2D predictedPlayerVelocity = player.changeVelocity(player.currentDirection, 1);
        							Point2D predictedPlayerLocation = player.shipLocation.add(predictedPlayerVelocity);
        							if (predictedPlayerLocation.getX() >= 0 
        									&& predictedPlayerLocation.getX() < 37 
        									&& predictedPlayerLocation.getY() >= 0 
        									&& predictedPlayerLocation.getY() < 21) {
        								if (grid [(int) predictedPlayerLocation.getX()] [(int) predictedPlayerLocation.getY()] != CellValue.BLOCK) {
        									this.enemies.get(i).moveEnemyChaseMode(predictedPlayerLocation, FPS, StepsPerSecond);	        						
        								} else {
        									this.enemies.get(i).moveEnemyChaseMode(player.getLocation(), FPS, StepsPerSecond);
        								}
        							} else {
        								this.enemies.get(i).moveEnemyChaseMode(player.getLocation(), FPS, StepsPerSecond);
        							}
        						}
        					} else {
        						isEnemyDead[i] = false;
        					}
        					
        					for (int j = 0; j < scatterPosition.size(); j++) {
        						scatterPosition.set(j, new Pair<Point2D, Integer>(scatterPosition.get(j).getKey(), 0));
        					}
        					
        				} else {
        					if (enemyPosition.get(i).getKey() <= 18) {
        						boolean allScatterPointsPassedThough = true;
        						for (int j = 0; j < scatterPosition.size(); j++) {
        							if ((scatterPosition.get(j).getKey().getX() < 18) && (scatterPosition.get(j).getValue() == 0)) {
        								allScatterPointsPassedThough = false;
        							}
        						}
        						
        						if (allScatterPointsPassedThough) {
        							for (int j = 0; j < scatterPosition.size(); j++) {
            							if (scatterPosition.get(j).getKey().getX() < 18) {
            								scatterPosition.set(j, new Pair<Point2D, Integer>(scatterPosition.get(j).getKey(), 0));
            							}
            						}
        						}
        						
        						for (int j = 0; j < scatterPosition.size(); j++) {
        							if ((enemies.get(i).getLocation().getX() == scatterPosition.get(j).getKey().getX()) 
        									&& (enemies.get(i).getLocation().getY() == scatterPosition.get(j).getKey().getY()) 
        									&& (scatterPosition.get(j).getValue() == 0)) {
        								scatterPosition.set(j, new Pair<Point2D, Integer>(enemies.get(i).getLocation(), 1));
        							}
        							
        							if ((scatterPosition.get(j).getKey().getX() < 18) && (scatterPosition.get(j).getValue() == 0)) {
        								this.enemies.get(i).moveEnemyChaseMode(scatterPosition.get(j).getKey(), FPS, StepsPerSecond);
        								break;
        							}
        						}
        					} else {
        						boolean allScatterPointsPassedThough = true;
        						for (int j = 0; j < scatterPosition.size(); j++) {
        							if ((scatterPosition.get(j).getKey().getX() >= 18) && (scatterPosition.get(j).getValue() == 0)) {
        								allScatterPointsPassedThough = false;
        							}
        						}
        						
        						if (allScatterPointsPassedThough) {
        							for (int j = 0; j < scatterPosition.size(); j++) {
            							if (scatterPosition.get(j).getKey().getX() >= 18) {
            								scatterPosition.set(j, new Pair<Point2D, Integer>(scatterPosition.get(j).getKey(), 0));
            							}
            						}
        						}
           						for (int j = 0; j < scatterPosition.size(); j++) {
        							if ((enemies.get(i).getLocation().getX() == scatterPosition.get(j).getKey().getX()) 
        									&& (enemies.get(i).getLocation().getY() == scatterPosition.get(j).getKey().getY()) 
        									&& (scatterPosition.get(j).getValue() == 0)) {
        								scatterPosition.set(j, new Pair<Point2D, Integer>(enemies.get(i).getLocation(), 1));
        							}
        							if ((scatterPosition.get(j).getKey().getX() >= 18) && (scatterPosition.get(j).getValue() == 0)) {
        								this.enemies.get(i).moveEnemyChaseMode(scatterPosition.get(j).getKey(), FPS, StepsPerSecond);
        								break;
        							}
        						}
        					}
        				}
        			}
        			
        			if (checkCollision(player.getLocation(),enemies.get(i).getLocation())) {
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
	        			
//	        			Point2D enemyLocation = this.enemies.get(i).getLocation();
//	        			Point2D playerLocation = player.getLocation();
//	        			switch (enemies.get(i).getCurrentDirection()) {
//	        			case RIGHT:
//	        				if (enemyLocation.getY() == playerLocation.getY() && playerLocation.getX() > enemyLocation.getX()) {
//	        					shoot(enemies.get(i));
//	        				}
//	        				break;
//	        			case DOWN:
//	        				if (enemyLocation.getX() == playerLocation.getX() && playerLocation.getY() > enemyLocation.getY()) {
//	        					shoot(enemies.get(i));
//	        				}
//	        				break;
//	        			case LEFT:
//	        				if (enemyLocation.getY() == playerLocation.getY() && playerLocation.getX() < enemyLocation.getX()) {
//	        					shoot(enemies.get(i));
//	        				}
//	        				break;
//	        			case UP:
//	        				if (enemyLocation.getX() == playerLocation.getX() && playerLocation.getY() < enemyLocation.getY()) {
//	        					shoot(enemies.get(i));
//	        				}
//	        				break;
//	        				
//	        			default:
//	        				break;
//	        			}
	        				
	        	}
        		else {
	        		
	        		if (enemies.get(i).getLocation().getX() <= 18 && enemies.get(i).getLocation().getY() <= 11) {
	        			enemies.get(i).moveEnemyChaseMode(new Point2D(1, 2), FPS, StepsPerSecond);
	        		} else if (enemies.get(i).getLocation().getX() > 18 && enemies.get(i).getLocation().getY() <= 11) {
	        			enemies.get(i).moveEnemyChaseMode(new Point2D(35, 2), FPS, StepsPerSecond);
	        		} else if (enemies.get(i).getLocation().getX() <= 18 && enemies.get(i).getLocation().getY() > 11) {
	        			enemies.get(i).moveEnemyChaseMode(new Point2D(1, 18), FPS, StepsPerSecond);
	        		} else if (enemies.get(i).getLocation().getX() > 18 && enemies.get(i).getLocation().getY() > 11) {
	        			enemies.get(i).moveEnemyChaseMode(new Point2D(35, 18), FPS, StepsPerSecond);
	        		}
	        		
	        	}
	        	
	        } 	
	        
    	}
    	
    	for(int i = 0; i < noEnemies; i++) {
    		enemies.get(i).setLocation(roundShipLocation(enemies.get(i)));
        }
    	
    	if(playerLives == 0) {
        	gameOver = true;
        }
    	
        if(smalldot == 0 && powerUpCoins == 0) {
        	youWon = true;
        }
    }
    
    
    
    public boolean checkCollision(Point2D ship1Location, Point2D ship2Location) {
    	double range = 0.9;
    	double distanceX = Math.abs(ship1Location.getX() - ship2Location.getX());
    	double distanceY = Math.abs(ship1Location.getY() - ship2Location.getY());
    	if(distanceX < range && distanceY < range) {
    		return true;
    	}
    	return false;
    }
    
    // check if the bullet shot player or enemies
    private void checkBullet() {
    	
        for(int i = 0; i < bullets.size(); i++) {
			boolean disappear = false;
        	if (checkCollision(player.getLocation(), bullets.get(i).getLocation())) {
        		gameRunning = false;
                playerLives--;
                playerDeadStep = step;
                createLifeHBox();
                if(playerLives != 0) {                	
                	player.setLocation(new Point2D(playerPosition.getKey(), playerPosition.getValue()));
                	pause();
                	delayNextScene(1000);
                }
                soundManager.playPlayerExplodeMusic();
                player.setVelocity(new Point2D(0,0));
                return;
            } else {
                for(int j = 0; j < noEnemies; j++) {
                	if (checkCollision(enemies.get(j).getLocation(), bullets.get(i).getLocation())) {
                		score += 100;
                		soundManager.playEnemyExplodeMusic();
                		enemies.get(j).setLocation(new Point2D (enemyPosition.get(j).getKey(), enemyPosition.get(j).getValue()));
                		isEnemyDead[j] = true;
//                      enemies.remove(j);
//                      noEnemies -= 1;
                        disappear = true;
                        break;
                    }
            	}
            }
        	if (!disappear) {
				if (bullets.get(i).getIsActiveBullet()) {
		        	if (checkCollision(player.getLocation(), bullets.get(i).getLocation())) {
		        		gameRunning = false;
		        		playerLives--;
		        		playerDeadStep = step;
		                createLifeHBox();
		                if(playerLives != 0) {                	
		                	player.setLocation(new Point2D(playerPosition.getKey(), playerPosition.getValue()));
		                	pause();
		                	delayNextScene(1000);
		                }
		                soundManager.playPlayerExplodeMusic();
		                player.setVelocity(new Point2D(0,0));
		                return;
		            } else {
		                for(int j = 0; j < noEnemies; j++) {
		                	if (checkCollision(enemies.get(j).getLocation(), bullets.get(i).getLocation())) {
		                		score += 100;
		                		soundManager.playEnemyExplodeMusic();
		                		enemies.get(j).setLocation(new Point2D (enemyPosition.get(j).getKey(), enemyPosition.get(j).getValue()));
		                		isEnemyDead[j] = true;
//		                        enemies.remove(j);
//		                        noEnemies -= 1;
		                        disappear = true;
		                        break;
		                    }
		            	}
		            }
				} else {
//					bullets.remove(i);
//					i -= 1;
					disappear = true;
				}
        	}
        	if (disappear) {
				bullets.remove(i);
				i -= 1;
        	}
    	}
        
    }
    
    
    private void update() {
    	currentFrame++;
    	if (currentFrame == FPS){
    		currentFrame = 0;
    	}
    	
    	player.setGameGrid(grid);
		if (currentFrame % (int)(FPS/StepsPerSecond) == 0) {
			this.step();
		}
		else {
			
			player.movePlayer(FPS, StepsPerSecond);
			
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).flyBullet(FPS, StepsPerSecond);
			}
			if(playerDeadStep - step <= 0) {
				for(int i = 0; i < noEnemies; i++) {
					this.enemies.get(i).moveEnemy(FPS, StepsPerSecond);
				}
			}
		}
    	
        if (playerDeadStep - step >= 0) {
        	
	        if ((playerDeadStep-step) % 2 == 0) {
	        	hidePlayer = true;
	        } else {
	        	hidePlayer = false;
	        }
	        
        }
        checkBullet();
        checkCoins(roundShipLocation(player));
        this.gameView.update(player,enemies, bullets, FPS, StepsPerSecond, currentFrame);
        
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
    		} else if (code == KeyCode.SPACE) { //shooting
    			if (playerBullet != 0) {
    				if (player.getCurrentDirection() != Direction.NONE) {
    					direction = player.getCurrentDirection();
    					soundManager.playLaserShootMusic();
    					player.shoot(bullets);
    					playerBullet--;
    					createBulletBox();
    				}
    			} else {
    				soundManager.playLaserEmptyMusic();
    			}
    		} else if (code == KeyCode.P) {
    			pause();
    			gameRunning = false;
    			gamePaused = true;
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
    			player.setNextDirection(direction);
    		}
    	} else if (gamePaused) {
    		KeyCode code = keyEvent.getCode();
    		if (code == KeyCode.P) {
    			gamePaused = false;
    			gameRunning = false;
    			soundManager.playCountDownMusic();
    			delayNextScene(3500);    			
    		} else if (code == KeyCode.G) {
    			gamePaused = false;
    			gameRunning = false;
    			this.startNewGame();
    		} else if (code == KeyCode.ESCAPE) {
    			gamePaused = false;
    			gameRunning = false;
    			delayMainScene(0);
    		}
    	}
    }
    
    
    public void createLifeHBox() {
    	
		lifeBox.getChildren().removeAll(lifeBox.getChildren());
    	
    	Image shipImage = new Image(viewManager.getChosenShip().getShipUrl(), 40, 40, true, false);
    	Image blackImage = new Image("res/playerLife3_black.png", 40, 40, true, false);
    	
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
    
    
    public void createBulletBox() {
    	
    	bulletBox.getChildren().removeAll(bulletBox.getChildren());
    	
    	Image redLaserImage = new Image("res/laserRed15.png", 20, 40, false, false);
    	Image blackLaserImage = new Image("res/laserBlack15.png", 20, 40, false, false);
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
    

    public void pause() {
        this.timer.cancel();
    }
    
    public double getBoardWidth() {
        return GameView.CELL_WIDTH * this.gameView.getGvRowCount();
    }
    

    public double getBoardHeight() {
        return (GameView.CELL_WIDTH * this.gameView.getGvColumnCount()) + 100;
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
    
    
    public boolean isLevelComplete() {
    	return levelComplete;
    }

    public void setFPS(int fps) {
        FPS = fps;
    }

    public void setSPS(int sps) {
        StepsPerSecond = sps;
    }
    

    public boolean isPlayerHidden() {
    	return hidePlayer;
    }
    
    
}
