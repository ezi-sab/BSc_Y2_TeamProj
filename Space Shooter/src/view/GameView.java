package view;

import javafx.fxml.FXML;



import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;
import model.Ship;
import model.ShipPicker;
import model.SmallInfoLabel;
import view.ShipModel.CellValue;

public class GameView extends Group {
	 private AnchorPane gamePane;
	 private Scene gameScene;
	 private Stage gameStage;

	 //private static final int GAME_WIDTH = 2000;//changed from 600
	 //private static final int GAME_HEIGHT = 800;
	    
	 private Stage menuStage;
	 private ImageView ship;

    @FXML private int rowCount;
    @FXML private int columnCount;
    private ImageView[][] cellViews;
   
    /*private final Image shipRightImage = new Image(getClass().getResourceAsStream("/res/playerShip3_right.png"));
    private final Image shipUpImage = new Image(getClass().getResourceAsStream("/res/playerShip3_up.png"));
    private final Image shipDownImage = new Image(getClass().getResourceAsStream("/res/playerShip3_down.png"));
    private final Image shipLeftImage = new Image(getClass().getResourceAsStream("/res/playerShip3_left.png")); */
    private Image coin; //set to final
    private Image powerUp; //set to final
    private final Image blockImage = new Image(getClass().getResourceAsStream("/res/spaceBuilding_018.png"));
    private static Image shipUpImage;
    private static Image shipDownImage;
    private static Image shipLeftImage;
    private static Image shipRightImage;
    
    private final Image enemy1Image = new Image(getClass().getResourceAsStream("/res/spaceShips_004.png"));
    private final Image enemy2Image = new Image(getClass().getResourceAsStream("/res/spaceShips_009.png"));

    private final Image bulletImage = new Image(getClass().getResourceAsStream("/res/laserRed15.png"));
    
    
    public final static double CELL_WIDTH = 30.0;
    
    private SmallInfoLabel pointsLabel;
    private ImageView[] playerLifes;
    private int playerLife;
    private int points;

    public GameView() {
    	initializeStage();
    }
    
    private void initializeStage() {
    	gamePane = new AnchorPane();
    	//gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
    	gameStage = new Stage();
    	gameStage.setScene(gameScene);
    }
    
    public void createNewGame(Stage menuStage, Ship chosenShip) throws Exception {
    	this.menuStage = menuStage;
    	this.menuStage.hide();
    	
    	createGameElements(chosenShip);
    	//this.shipImage = new Image(getClass().getResourceAsStream(chosenShip.getUrl()));
    	/*this.gamePane = new AnchorPane();
    	Image backgroundImage = new Image("view/resources/space.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		gamePane.setBackground(new Background(background)); */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StarShooter.fxml"));
        Parent root = loader.load();
        gameStage.setTitle("Space Shooter");
        Controller controller = loader.getController(); 
        root.setOnKeyPressed(controller);
        double sceneWidth = controller.getBoardWidth();
        double sceneHeight = controller.getBoardHeight();
        gameStage.setScene(new Scene(root, sceneWidth, sceneHeight));
        gameStage.show();
        root.requestFocus();{
    	//gameStage.show();
        }
    }
    
    private void createGameElements(Ship chosenShip) {
    	playerLife = 2;
    	pointsLabel = new SmallInfoLabel("POINTS : 00");
    	pointsLabel.setLayoutX(460);
    	pointsLabel.setLayoutY(20);
    	gamePane.getChildren().add(pointsLabel);
    	playerLifes = new ImageView[3];
    	
    	for (int i = 0; i < playerLifes.length; i++) {
    		playerLifes[i] = new ImageView(chosenShip.getLifeUrl());
    		playerLifes[i].setLayoutX(455 + (i*50));
    		playerLifes[i].setLayoutY(80);
    		gamePane.getChildren().add(playerLifes[i]);	
    	}
    	
    }
        

    // make new empty grid of cells
    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double) column * CELL_WIDTH);
                    imageView.setY((double) row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    // update based off of model of grid
    public void update(ShipModel model) {
    	assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        if (model.getRowCount() != this.rowCount || model.getColumnCount() != this.columnCount) {
            this.rowCount = model.getRowCount();
            this.columnCount = model.getColumnCount();
            initializeGrid();
        }
        //set the image to correspond with the value of that cell
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                CellValue value = model.getCellValue(row, column);
                if (value == CellValue.EMPTY) {
                    this.cellViews[row][column].setImage(null);
                } else if (value == CellValue.BLOCK) {
                    this.cellViews[row][column].setImage(this.blockImage);
                } else {
                    this.cellViews[row][column].setImage(null);
                }
                // display the ship
                if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && ShipModel.getLastDirection() == ShipModel.Direction.RIGHT) {
                    this.cellViews[row][column].setImage(shipRightImage);
                } else if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && ShipModel.getLastDirection() == ShipModel.Direction.LEFT) {
                    this.cellViews[row][column].setImage(shipLeftImage);
                } else if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && (ShipModel.getLastDirection() == ShipModel.Direction.UP || ShipModel.getLastDirection() == ShipModel.Direction.NONE)) {
                    this.cellViews[row][column].setImage(shipUpImage);
                } else if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && ShipModel.getLastDirection() == ShipModel.Direction.DOWN) {
                    this.cellViews[row][column].setImage(shipDownImage);
                }
                //display enemy ship
                if (row == model.getEnemy1Location().getX() && column == model.getEnemy1Location().getY()) {
                    this.cellViews[row][column].setImage(this.enemy1Image);
                }
                if (row == model.getEnemy2Location().getX() && column == model.getEnemy2Location().getY()) {
                    this.cellViews[row][column].setImage(this.enemy2Image);
                }
            }
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }
    
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

    public int getColumnCount() {
        return this.columnCount;
    }
    
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }
    
    public void setShipImage(Ship chosenShip) {
    	shipUpImage = new Image(getClass().getResourceAsStream(chosenShip.getUpUrl()));
    	shipDownImage = new Image(getClass().getResourceAsStream(chosenShip.getDownUrl()));
    	shipLeftImage = new Image(getClass().getResourceAsStream(chosenShip.getLeftUrl()));
    	shipRightImage = new Image(getClass().getResourceAsStream(chosenShip.getRightUrl()));
    }

}
