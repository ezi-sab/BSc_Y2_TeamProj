package view;

import javafx.geometry.Point2D;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;

public class PlayerModel extends ShipModel{
	
	public PlayerModel(CellValue[][] gameGrid) {
		super(gameGrid);
	}

	public void movePlayer() {
		Point2D predictedShipVelocity = changeVelocity(this.currentDirection);
        
        Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
        predictedShipLocation = setOffScreenLocation(predictedShipLocation);
        
        
        if (this.currentDirection.equals(lastDirection)) {

        	if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                shipVelocity = changeVelocity(Direction.NONE);
        } else {
        	shipVelocity = predictedShipVelocity;
            shipLocation = predictedShipLocation;
            setLastDirection(this.currentDirection);
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
                setLastDirection(this.currentDirection);
            }
        }
    }
}
