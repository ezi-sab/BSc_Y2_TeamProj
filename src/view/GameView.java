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
    		new Image(getClass().getResourceAsStream("/res/spaceShips_009.png")),
			new Image(getClass().getResourceAsStream("/res/spaceShips_004.png"))
    		};

    private static Controller controller;
    public ViewManager viewManager = new ViewManager();
    private static SoundManager soundManager = new SoundManager();
    
    public final static double CELL_WIDTH = 34.0;

	/**
	 * Initializes the empty grid of cells.
	 */
	// make new empty grid of cells
    private void initializeGameViewGrid() {
        if (gvRowCount > 0 && gvColumnCount > 0) {
            this.cellViews = new ImageView[gvRowCount][gvColumnCount];
            for (int row = 0; row < gvRowCount; row++) {
                for (int column = 0; column < gvColumnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double) row * CELL_WIDTH);
                    imageView.setY((double) column * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

	/**
	 * Creates a new game with the chosen ship by player.
	 * Sets the scene and window with the values from the Controller.
	 * @param chosenShip is selected for the player in the game
	 */
	public void createNewGame(Ship chosenShip) {
    	
    	try {
    		setShipImage(chosenShip);
    		setShipLifeImage(chosenShip);
    		setLaserImage(chosenShip);
    		setPowerUpImage(chosenShip);
    		
    		root = loader.load();
    		controller = loader.getController(); 
    		root.setOnKeyPressed(controller);
    		
    		double sceneWidth = controller.getBoardWidth();
    		double sceneHeight = controller.getBoardHeight();
    		gameScene = new Scene(root, sceneWidth, sceneHeight);
    		viewManager.getMainStage().setScene(gameScene);
    		
    		root.requestFocus();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        
    }

	/**
	 * Implements a count down timer and sets the ships in a countdown mode.
	 * After completion it starts a new game.
	 * @param ship selected for the game.
	 * @param delay time for the scene to display.
	 */
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

	/**
	 * Ships are displayed accordingly in a countdown mode.
	 * @param ship to be displayed in countdown scene
	 */
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


	/**
	 * Updates the game grid with the current state .
	 * Enemies, players movement will decide the updation.
	 * @param player Player Model
	 * @param enemies Enemy Model
	 * @param bullets Bullet Model
	 */
    // update based off of model of grid
    public void update(PlayerModel player, List<EnemyAIModel> enemies, List<BulletModel> bullets) {
    	/*
    	assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        if (model.getRowCount() != this.rowCount || model.getColumnCount() != this.columnCount) {
            initializeGrid();
        }
        set the image to correspond with the value of that cell
        */
    	
    	if (rowList.isEmpty() == false && columnList.isEmpty() == false) {
    		
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
    	
        for (int column = 0; column < gvColumnCount; column++) {
            for (int row = 0; row < gvRowCount; row++) {
            	CellValue value = player.getCellValue(row, column);
                if (value == CellValue.COIN) {
                    this.cellViews[row][column].setImage(this.coinImage);
                } else if (value == CellValue.BLOCK) {
                    this.cellViews[row][column].setImage(this.blockImage);
                } else if (value == CellValue.LIFE) {
                	this.cellViews[row][column].setImage(lifeImage);
                } else if (value == CellValue.POWERUP) {
                	this.cellViews[row][column].setImage(powerUpImage);
                } else {
                    this.cellViews[row][column].setImage(null);
                }

                if (!(controller.isPlayerHidden())) {
                	
                	if (row == player.getLocation().getX() && column == player.getLocation().getY()){
                		this.cellViews[row][column].setImage(shipImage);
                		if (player.getLastDirection() == ShipModel.Direction.RIGHT) {
                			this.cellViews[row][column].setRotate(90);
                			rowList.add(row);
                			columnList.add(column);
                		} else if (player.getLastDirection() == ShipModel.Direction.LEFT) {
                			this.cellViews[row][column].setRotate(-90);
                			rowList.add(row);
                			columnList.add(column);
                		} else if (player.getLastDirection() == ShipModel.Direction.UP || player.getLastDirection() == ShipModel.Direction.NONE) {
                			this.cellViews[row][column].setRotate(0);
                			rowList.add(row);
                			columnList.add(column);
                		} else if (player.getLastDirection() == ShipModel.Direction.DOWN) {
                			this.cellViews[row][column].setRotate(180);
                			rowList.add(row);
                			columnList.add(column);
                		}
                	}
                	
                }
                
                for (int i = 0; i < enemies.size(); i++) {
                	if (row == enemies.get(i).getLocation().getX() && column == enemies.get(i).getLocation().getY()) {
                		this.cellViews[row][column].setImage(this.enemyImages[i]);
		                if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.RIGHT) {
		                	this.cellViews[row][column].setRotate(90);
		                	rowList.add(row);
		                	columnList.add(column);
		                } else if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.LEFT) {
		                	this.cellViews[row][column].setRotate(-90);
		                	rowList.add(row);
		                	columnList.add(column);
		                } else if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.UP || enemies.get(i).getCurrentDirection() == ShipModel.Direction.NONE) {
		                	this.cellViews[row][column].setRotate(0);
		                	rowList.add(row);
		                	columnList.add(column);
		                } else if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.DOWN) {
		                	this.cellViews[row][column].setRotate(180);
		                	rowList.add(row);
		                	columnList.add(column);
		                }
                	}
                }
                
                for (int i = 0; i < bullets.size(); i++) {
                	if (row == bullets.get(i).getLocation().getX() && column == bullets.get(i).getLocation().getY()) {
                		this.cellViews[row][column].setImage(bulletImage);
		                if (bullets.get(i).getCurrentDirection() == ShipModel.Direction.RIGHT) {
		                	this.cellViews[row][column].setRotate(90);
		                	rowList.add(row);
		                	columnList.add(column);
		                } else if (bullets.get(i).getCurrentDirection() == ShipModel.Direction.LEFT) {
		                	this.cellViews[row][column].setRotate(-90);
		                	rowList.add(row);
		                	columnList.add(column);
		                } else if (bullets.get(i).getCurrentDirection() == ShipModel.Direction.UP || bullets.get(i).getCurrentDirection() == ShipModel.Direction.NONE) {
		                	this.cellViews[row][column].setRotate(0);
		                	rowList.add(row);
		                	columnList.add(column);
		                } else if (bullets.get(i).getCurrentDirection() == ShipModel.Direction.DOWN) {
		                	this.cellViews[row][column].setRotate(180);
		                	rowList.add(row);
		                	columnList.add(column);
		                }
                	}
                }
            }
        }
        
    }

	/**
	 * Gets the count of the Row.
	 * @return int row count.
	 */
	public int getGvRowCount() {
        return this.gvRowCount;
    }

	/**
	 * Sets the count of the Row.
	 * Initialises the game grid.
	 * @return int row count.
	 */
    public void setGvRowCount(int rowCount) {
        this.gvRowCount = rowCount;
        this.initializeGameViewGrid();
    }

	/**
	 * Gets the count of the Column.
	 * @return int column count.
	 */
    public int getGvColumnCount() {
        return this.gvColumnCount;
    }

	/**
	 * Sets the count of the Row.
	 * Initialises the game grid.
	 * @return int row count.
	 */
    public void setGvColumnCount(int columnCount) {
        this.gvColumnCount = columnCount;
        this.initializeGameViewGrid();
    }

	/**
	 * Sets the ShipImage
	 * @param chosenShip
	 */
	public void setShipImage(Ship chosenShip) {
    	shipImage = new Image(getClass().getResourceAsStream(chosenShip.getShipUrl()));
    }
	/**
	 * Sets the Ship life Image
	 * @param chosenShip
	 */
    public void setShipLifeImage(Ship chosenShip) {
    	lifeImage = new Image(getClass().getResourceAsStream(chosenShip.getLifeUrl()), 30, 30, true, false);
    }

	/**
	 * Sets the laser Image
	 * @param chosenShip
	 */
    public void setLaserImage(Ship chosenShip) {
    	bulletImage = new Image(getClass().getResourceAsStream(chosenShip.getLaserUrl()));
    }

	/**
	 * Sets the power up Image
	 * @param chosenShip
	 */
    public void setPowerUpImage(Ship chosenShip) {
    	powerUpImage = new Image(getClass().getResourceAsStream(chosenShip.getPowerUpUrl()), 30, 30, true, true);
    }

	/**
	 * Creates the background for the Game Scene and window.
	 */
    private void createBackground() {
		
		Image backgroundImage = new Image("view/resources/space.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		countDownHBox.setBackground(new Background(background));
		
	}
    
}
