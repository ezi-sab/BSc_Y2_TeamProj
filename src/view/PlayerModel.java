package view;

import javafx.geometry.Point2D;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;

public class PlayerModel extends ShipModel{
	
	public PlayerModel(CellValue[][] gameGrid, int rowCount, int columnCount) {
		super(gameGrid, columnCount, columnCount);
	}

	public void moveShip(Direction direction) {
		Point2D predictedShipVelocity = changeVelocity(direction);
        
        Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
        predictedShipLocation = setOffScreenLocation(predictedShipLocation);
        if (direction.equals(lastDirection)) {

        	if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                shipVelocity = changeVelocity(Direction.NONE);
        } else {
        	shipVelocity = predictedShipVelocity;
            shipLocation = predictedShipLocation;
            setLastDirection(direction);
            }
        }
        else {
            if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                predictedShipVelocity = changeVelocity(lastDirection);
                predictedShipLocation = shipLocation.add(predictedShipVelocity);
                if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                    shipVelocity = changeVelocity(Direction.NONE);
                } else {
                    shipVelocity = changeVelocity(lastDirection);
                    shipLocation = shipLocation.add(shipVelocity);
                }
            } else {
                shipVelocity = predictedShipVelocity;
                shipLocation = predictedShipLocation;
                setLastDirection(direction);
            }
        }
    }
}
