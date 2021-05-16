package view;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import model.Ship;
import view.ShipModel.CellValue;

public class GameView extends Group {
	
	
	private static HBox countDownShips = new HBox();
	private static HBox countDownHBox = new HBox();
	private static Scene countDownScene = new Scene(countDownHBox, 1258, 814);
	private static Scene gameScene;
	private static Parent root;
	
	private FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StarShooter.fxml"));

	static public int FPS;
	static private int StepsPerSecond;

    @FXML private int gvRowCount;
    @FXML private int gvColumnCount;
    
    private ImageView[][] cellViews;
    private static int shipNumber = 3;
    
    private final Image blockImage = new Image(getClass().getResourceAsStream("/res/spaceBuilding_018.png"));
    private final Image coinImage = new Image(getClass().getResourceAsStream("/res/smalldot.png"));
    private static Image shipImage;
    private static Image lifeImage;
    private static Image bulletImage;
    private static Image powerUpImage;
    
    private static List<Integer> rowList = new ArrayList<Integer>();
    private static List<Integer> columnList = new ArrayList<Integer>();
    
    private final Image enemyImages[] = {
    		new Image(getClass().getResourceAsStream("/res/spaceShips_004.png")), 
    		new Image(getClass().getResourceAsStream("/res/spaceShips_009.png")),
    		new Image(getClass().getResourceAsStream("/res/spaceShips_009.png"))
    		};

    private static Controller controller;
    public ViewManager viewManager = new ViewManager();
    private static SoundManager soundManager = new SoundManager();
    
    public final static double CELL_WIDTH = 34.0;
    
    // make new empty grid of cells
    private void initializeGameViewGrid() {

    	int viewGVRowCount = gvRowCount * (FPS/StepsPerSecond);
    	int viewGVColumnCount = gvColumnCount * (FPS/StepsPerSecond);
        if (viewGVRowCount > 0 && viewGVColumnCount > 0) {
            this.cellViews = new ImageView[viewGVRowCount][viewGVColumnCount];
            for (int row = 0; row < viewGVRowCount; row++) {
                for (int column = 0; column < viewGVColumnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)((double) row * CELL_WIDTH)/(FPS/StepsPerSecond));
                    imageView.setY((double)((double) column * CELL_WIDTH)/(FPS/StepsPerSecond));
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }
    
    public void createNewGame(Ship chosenShip) {
    	try {
    		setShipImage(chosenShip);
    		setShipLifeImage(chosenShip);
    		setLaserImage(chosenShip);
    		setPowerUpImage(chosenShip);
    		
    		root = loader.load();
    		controller = loader.getController();
    		controller.setFPS(FPS);
        	controller.setSPS(StepsPerSecond);
    		root.setOnKeyPressed(controller);
    		
    		double sceneWidth = controller.getBoardWidth();
    		double sceneHeight = controller.getBoardHeight();
    		gameScene = new Scene(root,sceneWidth, sceneHeight);
    		viewManager.getMainStage().setScene(gameScene);
    		
    		root.requestFocus();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void countDownTimer(Ship ship, int delay) {
		
    	Timer countDownTimer = new Timer();
    	soundManager.playCountDownMusic();
    	countDownShips.setAlignment(Pos.CENTER);
		countDownHBox.setAlignment(Pos.CENTER);
    	createBackground();
    	
    	TimerTask countDownTimerTask = new TimerTask() {
				
			@Override
			public void run() {
				
				Platform.runLater(() -> {
					
					createShipCountDown(ship);
					if(shipNumber == -1) {
					
						countDownTimer.cancel();
						countDownTimer.purge();
						shipNumber = 3;
						try {
							createNewGame(ship);
						} catch (Exception e) {
							e.printStackTrace();							
						}
						
					}
					
				});
				
			}
		};
		countDownTimer.schedule(countDownTimerTask, 0, delay);
    		
    }
    
    public void createShipCountDown(Ship ship) {
    	countDownShips.getChildren().removeAll(countDownShips.getChildren());
    	countDownHBox.getChildren().removeAll(countDownHBox.getChildren());
    	Image shipImage = new Image(ship.getShipUrl(), 100, 100, true, false);
    	if (shipNumber == 3) {
    		
    		ImageView ship1 = new ImageView(shipImage);
    		ImageView ship2 = new ImageView(shipImage);
    		ImageView ship3 = new ImageView(shipImage);
    		
    		countDownShips.getChildren().add(ship1);
    		countDownShips.setSpacing(50);
    		countDownShips.getChildren().add(ship2);
    		countDownShips.setSpacing(50);
    		countDownShips.getChildren().add(ship3);
    		countDownHBox.getChildren().add(countDownShips);
    		
    		viewManager.getMainStage().setScene(countDownScene);
    		
    	} else if(shipNumber == 2) {
    		
    		ImageView ship1 = new ImageView(shipImage);
    		ImageView ship2 = new ImageView(shipImage);
    		
    		countDownShips.getChildren().add(ship1);
    		countDownShips.setSpacing(50);
    		countDownShips.getChildren().add(ship2);
    		countDownHBox.getChildren().add(countDownShips);
    		
    	} else if(shipNumber == 1) {
    		
    		ImageView ship1 = new ImageView(shipImage);
    		
    		countDownShips.getChildren().add(ship1);
    		countDownHBox.getChildren().add(countDownShips);
    	}
    	shipNumber--;
    	
    }



    public void update(PlayerModel player, List<EnemyAIModel> enemies, List<BulletModel> bullets,  int fps, int sps, int cFrame) {
    	
		if (!(rowList.isEmpty() || columnList.isEmpty())) {
    		
    		int[] rowArray = new int[rowList.size()];
    		int[] columnArray = new int[columnList.size()];
    		int arraySize = rowList.size();
    		
    		for(int i = 0; i < arraySize; i++) {
    			rowArray[i] = rowList.get(i);
    			columnArray[i] = columnList.get(i);
    			this.cellViews[rowArray[i]][columnArray[i]].setRotate(0);
    		}
    		
    		for(int j = 0; j < rowList.size(); j++) {
    			rowList.remove(j);
    			columnList.remove(j);
    		}
    	}
    	
		for (int row = 0; row < gvRowCount * (FPS/StepsPerSecond); row++) {
            for (int column = 0; column < gvColumnCount * (FPS/StepsPerSecond); column++) {
            	this.cellViews[row][column].setImage(null);
            }
		}
		
		for (int column = 0; column < gvColumnCount; column++) {
			for (int row = 0; row < gvRowCount; row++) {
            	int gvRow = row * (FPS/StepsPerSecond);
            	int gvColumn = column * (FPS/StepsPerSecond);

            	CellValue value = player.getCellValue(row, column);
                if (value == CellValue.BLOCK) {
                    this.cellViews[gvRow][gvColumn].setImage(blockImage);
                } else if (value == CellValue.COIN) {
                    this.cellViews[gvRow][gvColumn].setImage(coinImage);
                } else if (value == CellValue.LIFE) {
                	this.cellViews[gvRow][gvColumn].setImage(lifeImage);
                } else if (value == CellValue.POWERUP) {
                	this.cellViews[gvRow][gvColumn].setImage(powerUpImage);
                }
            }
		}
		
		if (!(controller.isPlayerHidden())) {
			int playerX = (int)(player.getLocation().getX() * (double)(FPS/StepsPerSecond));
			int playerY = (int)(player.getLocation().getY() * (double)(FPS/StepsPerSecond));
			
			this.cellViews[playerX][playerY].setImage(shipImage);
	        player.rotateShip(this.cellViews, playerX, playerY, fps, sps, cFrame);
	        rowList.add(playerX);
		    columnList.add(playerY);
		}
        
        for (int i = 0; i < enemies.size(); i++) {
        	int enemyX = (int)(enemies.get(i).getLocation().getX() * (double)(FPS/StepsPerSecond));
    		int enemyY = (int)(enemies.get(i).getLocation().getY() * (double)(FPS/StepsPerSecond));
    		
    		this.cellViews[enemyX][enemyY].setImage(this.enemyImages[i]);
    		enemies.get(i).rotateShip(this.cellViews, enemyX, enemyY, fps, sps, cFrame);
    		rowList.add(enemyX);
		    columnList.add(enemyY);
        }
    	
        for (int i = 0; i < bullets.size(); i++) {
        	int bulletX = (int)(bullets.get(i).getLocation().getX() * (double)(FPS/StepsPerSecond));
    		int bulletY = (int)(bullets.get(i).getLocation().getY() * (double)(FPS/StepsPerSecond));
    		this.cellViews[bulletX][bulletY].setImage(bulletImage);
    		bullets.get(i).rotateShip(this.cellViews, bulletX, bulletY, fps, sps, cFrame);
    		rowList.add(bulletX);
		    columnList.add(bulletY);
        }
        
    }

    
    
    
    
    public int getGvRowCount() {
        return this.gvRowCount;
    }
    
    public void setGvRowCount(int rowCount) {
        this.gvRowCount = rowCount;
        this.initializeGameViewGrid();
    }

    public int getGvColumnCount() {
        return this.gvColumnCount;
    }

    public void setGvColumnCount(int columnCount) {
        this.gvColumnCount = columnCount;
        this.initializeGameViewGrid();
    }
    
    public void setShipImage(Ship chosenShip) {
    	shipImage = new Image(getClass().getResourceAsStream(chosenShip.getShipUrl()));
    }
    
    public void setShipLifeImage(Ship chosenShip) {
    	lifeImage = new Image(getClass().getResourceAsStream(chosenShip.getLifeUrl()), 30, 30, true, false);
    }
    
    public void setLaserImage(Ship chosenShip) {
    	bulletImage = new Image(getClass().getResourceAsStream(chosenShip.getLaserUrl()));
    }    
    
    public void setPowerUpImage(Ship chosenShip) {
    	powerUpImage = new Image(getClass().getResourceAsStream(chosenShip.getPowerUpUrl()), 30, 30, true, true);
    }
    
    private void createBackground() {
		
		Image backgroundImage = new Image("view/resources/space.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		countDownHBox.setBackground(new Background(background));
		
	}
    
    public int getFPS() {
        return FPS;
    }
    
    public void setFPS(int fps) {
        FPS = fps;
    }
    
    public int getSPS() {
        return StepsPerSecond;
    }

    public void setSPS(int sps) {
        StepsPerSecond = sps;
    }
    
}
