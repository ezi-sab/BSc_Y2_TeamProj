package view;

import javafx.geometry.Point2D;
/**
 * The BulletModel class defines a bullet and the move action of the bullet
 *
 */
public class BulletModel extends ShipModel {

	public BulletModel(CellValue[][] grid) {
		super(grid);
	}

	public boolean flyBullet() {
		
		Point2D predictedShipVelocity = changeVelocity(this.currentDirection);
		Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
		predictedShipLocation = setOffScreenLocation(predictedShipLocation);
		
		// disappear when hit the wall
		if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
			
			shipVelocity = changeVelocity(Direction.NONE);
			
			return false;
			
		} else {
			
			shipVelocity = predictedShipVelocity;
			shipLocation = predictedShipLocation;
			setLastDirection(this.currentDirection);
			
			return true;
			
		}
		
	}
	
}
