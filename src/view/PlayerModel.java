package view;

import javafx.geometry.Point2D;

public class PlayerModel extends ShipModel{
	
	public PlayerModel(CellValue[][] gameGrid) {
		super(gameGrid);
	}

	public void movePlayer() {
		
		Point2D predictedShipVelocity = changeVelocity(this.currentDirection);
        
        Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
        predictedShipLocation = setOffScreenLocation(predictedShipLocation);
        
        setLastDirection(this.currentDirection);
        
        if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
        	
        	predictedShipVelocity = changeVelocity(Direction.NONE);
        	predictedShipLocation = shipLocation.add(predictedShipVelocity);
        	
        }
        
        shipVelocity = predictedShipVelocity;
        shipLocation = predictedShipLocation;
        
    }
}

