package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ship;
import view.ShipModel.CellValue;

public class GameView extends Group {
	 //private AnchorPane gamePane;
	 private Scene gameScene;
	 private Stage gameStage;

	 //private static final int GAME_WIDTH = 2000;//changed from 600
	 //private static final int GAME_HEIGHT = 800;
	    
	 private Stage menuStage;
	 private ImageView ship;

    @FXML
    private int rowCount;
    @FXML
    private int columnCount;
    private ImageView[][] cellViews;
    private final Image shipRightImage = new Image(getClass().getResourceAsStream("/res/playerShip3_right.png"));
    private final Image shipUpImage = new Image(getClass().getResourceAsStream("/res/playerShip3_up.png"));
    private final Image shipDownImage = new Image(getClass().getResourceAsStream("/res/playerShip3_down.png"));
    private final Image shipLeftImage = new Image(getClass().getResourceAsStream("/res/playerShip3_left.png"));
    public final static double CELL_WIDTH = 30.0;

    public GameView() {
    	initializeStage();
    }
    
    private void initializeStage() {
    	//gamePane = new AnchorPane();
    	//gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
    	gameStage = new Stage();
    	gameStage.setScene(gameScene);
    }
    
    public void createNewGame(Stage menuStage, Ship chosenShip) throws Exception {
    	this.menuStage = menuStage;
    	this.menuStage.hide();
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
    	gameStage.show();
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
                }
                // display the ship
                if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && ShipModel.getLastDirection() == ShipModel.Direction.RIGHT) {
                    this.cellViews[row][column].setImage(this.shipRightImage);
                } else if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && ShipModel.getLastDirection() == ShipModel.Direction.LEFT) {
                    this.cellViews[row][column].setImage(this.shipLeftImage);
                } else if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && (ShipModel.getLastDirection() == ShipModel.Direction.UP || ShipModel.getLastDirection() == ShipModel.Direction.NONE)) {
                    this.cellViews[row][column].setImage(this.shipUpImage);
                } else if (row == model.getShipLocation().getX() && column == model.getShipLocation().getY() && ShipModel.getLastDirection() == ShipModel.Direction.DOWN) {
                    this.cellViews[row][column].setImage(this.shipDownImage);
                }
            }
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

}
