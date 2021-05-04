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
    
    // Change all level1, level2, level3 to level0 to make the game easier
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};
    private static final double FPS = 5.0;
    
    private CellValue[][] grid;
    
    @FXML private Label scoreLabel;
    @FXML private Label gameOverLabel;
    @FXML private Label levelLabel;
    @FXML private HBox lifeBox;
    @FXML private HBox bulletBox;
    @FXML private GameView gameView;
    
    private int rowCount;
    private int columnCount;
    private int noEnemies;
    private int score = 0;
    private int level = 0;
    private int smalldot = 0;
    private int keyPressed = 0;
    private int step = 0;
    private int playerLives = 3;
    private int playerBullet = 0;
    private boolean gameOver = false;
    private boolean youWon = false;
    private boolean levelComplete = false;
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
    	startLevel(file);
    }

    
    private void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(()->update());
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
				Platform.runLater(() -> {
					soundManager.bgmFadeIn();
					viewManager.setToMainScene();
				});
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
        noEnemies = 2;
        enemies = new ArrayList<EnemyAIModel>();
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
                    	break;
                    case 'U':
                    	thisValue = CellValue.POWERUP;
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
                row++;
            }
            column++;
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
        playerBullet = 0;
        playerLives = 3;
        createLifeHBox();
        createBulletBox();
        this.gameOverLabel.setText(String.format(""));
        startLevel(Controller.getCurrentLevel(0));
    }
    
    
    public void startNextLevel() {
        if (this.isLevelComplete()) {
            level++;
            rowCount = 0;
            columnCount = 0;
            playerBullet = 0;
            playerLives = 3;
            youWon = false;
            levelComplete = false;
            gameOver = false;
            createLifeHBox();
            createBulletBox();
            startLevel(Controller.getCurrentLevel(level));
        }
    }
    
    
    public void step() {
    	step += 1;
        checkBullet();
    	if (step % 2 == 1) {
	        player.movePlayer();
	        Point2D shipLocation = player.getLocation();
	        CellValue shipLocationCellValue = grid[(int) player.shipLocation.getX()][(int) shipLocation.getY()];
	
	        if (shipLocationCellValue == CellValue.COIN) {
	        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
	        	soundManager.playCoinCollectMusic();
	        	smalldot--;
	            score += 1;
	        }
	        
	        if (shipLocationCellValue == CellValue.LIFE) {
	        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
	        	if (playerLives != 3) {
	        		playerLives++;
	        		createLifeHBox();
	        	}
	        	score += 20;
	        }
	        
	        if (shipLocationCellValue == CellValue.POWERUP) {
	        	grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
	        	if (playerBullet != 3) {
	        		playerBullet++;
	        		createBulletBox();
	        	}
	        	score += 10;
	        }
	        
	        //TODO: Add the power up buttons.
	        
	        for(int i = 0; i < noEnemies; i++) {
	        	
	        	if (shipLocation.equals(this.enemies.get(i).getLocation())) {
	        		playerLives--;
	        		createLifeHBox();
	                if(playerLives != 0) {                	
	                	player.setLocation(new Point2D(18, 16));
	                	pause();
	                	delayNextScene(1000);
	                }
	                soundManager.playPlayerExplodeMusic();
	            }
	        	
	        	if(playerLives != 0) {
	        		if(step != 1) {
	        			this.enemies.get(i).moveEnemy(player.getLocation());        		
	        		}
	        		if (shipLocation.equals(this.enemies.get(i).getLocation())) {
	        			playerLives--;
	        			createLifeHBox();
	        			if(playerLives != 0) {                	
	        				player.setLocation(new Point2D(18, 16));
	        				pause();
	        				delayNextScene(1000);
	        			}
	        			soundManager.playPlayerExplodeMusic();
	        		}
	        		
//	        		Point2D enemyLocation = this.enemies.get(i).getLocation();
//	        		Point2D playerLocation = player.getLocation();
//	        		switch (enemies.get(i).getCurrentDirection()) {
//	        		case RIGHT:
//	        			if (enemyLocation.getY() == playerLocation.getY() && playerLocation.getX() > enemyLocation.getX()) {
//	        				shoot(enemies.get(i));
//	        			}
//	        			break;
//	        		case DOWN:
//	        			if (enemyLocation.getX() == playerLocation.getX() && playerLocation.getY() > enemyLocation.getY()) {
//	        				shoot(enemies.get(i));
//	        			}
//	        			break;
//	        		case LEFT:
//	        			if (enemyLocation.getY() == playerLocation.getY() && playerLocation.getX() < enemyLocation.getX()) {
//	        				shoot(enemies.get(i));
//	        			}
//	        			break;
//	        		case UP:
//	        			if (enemyLocation.getX() == playerLocation.getX() && playerLocation.getY() < enemyLocation.getY()) {
//	        				shoot(enemies.get(i));
//	        			}
//	        			break;
//	        			
//	        		default:
//	        			break;
//	        		}
	        	
	        	}
	    	}
    	}
    	
    	if(playerLives == 0) {	
        	gameOver = true;
        }
    	
        if(smalldot == 0) {	
        	youWon = true;
        }
        if (noEnemies == 0) {
        	gameOver = true;
            player.setVelocity(new Point2D(0,0));
        }
    }
    
    // generate a new bullet according to enemy or play's location and direction
    public void shoot(ShipModel ship) {
    	
		Point2D predictedShipVelocity = ship.changeVelocity(ship.getCurrentDirection());
		Point2D predictedShipLocation = ship.shipLocation.add(predictedShipVelocity);
		
		if (grid[(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
			
			BulletModel bullet = new BulletModel(grid);
			bullet.setLocation(ship.shipLocation.add(ship.changeVelocity(ship.getCurrentDirection())));
			bullet.setCurrentDirection(ship.getCurrentDirection());
			bullets.add(bullet);
			if (ship instanceof PlayerModel) {
				soundManager.playPlayerShootMusic();
			}
			
		}
		
    }
    
    //check if the bullet shot player or enemies
    private void checkBullet() {

        for(int i = 0; i < bullets.size(); i++) {
			boolean disappear = false;
        	if (player.getLocation().equals(this.bullets.get(i).getLocation())) {
                gameOver = true;
                soundManager.playPlayerExplodeMusic();
                player.setVelocity(new Point2D(0,0));
                return;
            } else {
                for(int j = 0; j < noEnemies; j++) {
                	if (this.enemies.get(j).getLocation().equals(this.bullets.get(i).getLocation())) {
                        enemies.remove(j);
                        noEnemies -= 1;
                        disappear = true;
                        break;
                    }
            	}
            }
        	if (!disappear) {
				if (bullets.get(i).flyBullet()) {
		        	if (player.getLocation().equals(this.bullets.get(i).getLocation())) {
		                gameOver = true;
		                soundManager.playPlayerExplodeMusic();
		                player.setVelocity(new Point2D(0,0));
		                return;
		            } else {
		                for(int j = 0; j < noEnemies; j++) {
		                	if (this.enemies.get(j).getLocation().equals(this.bullets.get(i).getLocation())) {
		                        enemies.remove(j);
		                        noEnemies -= 1;
		                        disappear = true;
		                        break;
		                    }
		            	}
		            }
				} else {
					bullets.remove(i);
					i -= 1;
				}
        	}
        	if (disappear) {
				bullets.remove(i);
				i -= 1;
        	}
    	}
        
    }
    
    
    private void update() {
    	player.setGameGrid(grid);
    	this.step();
        this.gameView.update(player,enemies, bullets);
        
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
            delayMainScene(3000);
            
        } else if (youWon) {
        	
        	if (level == 0) {
        		
        		this.gameOverLabel.setText(String.format("LEVEL 1 COMPLETED!"));
        		levelComplete = true;
        		pause();
        		delayNextLevel(2000);
        		
        	} else if(level == 1) {
        		
        		this.gameOverLabel.setText(String.format("LEVEL 2 COMPLETED!"));
        		levelComplete = true;
        		pause();
        		delayNextLevel(2000);
        		
        	} else if(level == 2) {
        		
        		this.gameOverLabel.setText(String.format("YOU WON!"));
        		scoreBoard.writeScore(score);
				scoreBoard.ClearVBox();
        		scoreBoard.setScoreVBox();
        		pause();
        		youWon = false;
        		levelComplete = false;
        		gameOver = false;
        		score = 0;
        		level = 0;
        		playerLives = 3;
        		delayMainScene(3000);
        		
        	}
        	
        }
        
    }
    
    
    @Override
    public void handle(KeyEvent keyEvent) {
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
        			shoot(player);
        			playerBullet--;
        			createBulletBox();
        		}
        	}
        } else if (code == KeyCode.G) {
        	direction = ShipModel.Direction.NONE;
        	lastDirection = direction;
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
    	
    	Image bigRedLaserImage = new Image("res/laserRed15.png", 15, 35, false, false);
    	Image redLaserImage = new Image("res/laserRed15.png", 15, 30, false, false);
    	Image blackLaserImage = new Image("res/laserBlack15.png", 15, 30, false, false);
    	bulletBox.setLayoutX(1000);
    	bulletBox.setLayoutY(10);
    	
    	if (playerBullet == 3) {
    		
    		ImageView laser1 = new ImageView(bigRedLaserImage);
    		ImageView laser2 = new ImageView(redLaserImage);
    		ImageView laser3 = new ImageView(redLaserImage);
    		bulletBox.getChildren().add(laser1);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser2);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser3);
    		
    	} else if (playerBullet == 2) {
    		
    		ImageView laser1 = new ImageView(bigRedLaserImage);
    		ImageView laser2 = new ImageView(redLaserImage);
    		ImageView laser3 = new ImageView(blackLaserImage);
    		bulletBox.getChildren().add(laser1);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser2);
    		bulletBox.setSpacing(10);
    		bulletBox.getChildren().add(laser3);
    		
    	} else if (playerBullet == 1) {
    		
    		ImageView laser1 = new ImageView(bigRedLaserImage);
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
    
    
}
