package view;

import javafx.geometry.Point2D;

public class PlayerModel extends ShipModel{
	
    /**
     * Constructor method for calling the parent level game grid.
     * @param gameGrid parent game grid which is coordinates of the game file.
     */
	public PlayerModel(CellValue[][] gameGrid) {
		super(gameGrid);
	}
	
    /**
     * Functions triggers and decides the movement of player in the game scene.
     * Decides the player traversing in the free space.
     * Or the player moving and hits the enemy.
     */
	public void movePlayer() {
		
		Point2D predictedShipVelocity = changeVelocity(this.currentDirection);
        
        Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
        predictedShipLocation = setOffScreenLocation(predictedShipLocation);
        
        if (this.currentDirection.equals(lastDirection)) {
        	
        	if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                shipVelocity = changeVelocity(Direction.NONE);
                setLastDirection(this.currentDirection);
        	} else {
        		shipVelocity = predictedShipVelocity;
        		shipLocation = predictedShipLocation;
        		setLastDirection(this.currentDirection);
            }
        	
        } else {
        	
            if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
            	
                predictedShipVelocity = changeVelocity(lastDirection);
                predictedShipLocation = shipLocation.add(predictedShipVelocity);
                predictedShipLocation = setOffScreenLocation(predictedShipLocation);
                
                if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                    shipVelocity = changeVelocity(Direction.NONE);
                } else {
                    shipVelocity = predictedShipVelocity;
                    shipLocation = predictedShipLocation;
                }
                
            } else {
            	
                shipVelocity = predictedShipVelocity;
                shipLocation = predictedShipLocation;
                setLastDirection(this.currentDirection);
                
            }   
        }   
    }
	
}
