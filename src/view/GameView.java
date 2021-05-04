package view;

import java.util.List;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Ship;
import view.ShipModel.CellValue;

public class GameView extends Group {
	
	private static Scene gameScene;
	private static Parent root;
	
	private FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StarShooter.fxml"));

    @FXML private int gvRowCount;
    @FXML private int gvColumnCount;
    
    private ImageView[][] cellViews;
    
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
    		new Image(getClass().getResourceAsStream("/res/spaceShips_009.png"))
    		};

    private static Controller controller;
    public ViewManager viewManager = new ViewManager();
    
    public final static double CELL_WIDTH = 34.0;
    
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
    
}
