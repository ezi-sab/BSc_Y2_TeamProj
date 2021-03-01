package game;

import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import game.ShipModel.CellValue;

public class GameView extends Group {

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
