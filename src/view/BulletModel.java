package view;

import javafx.geometry.Point2D;
/**
 * The BulletModel class defines a bullet and the move action of the bullet
 *
 */
public class BulletModel extends ShipModel {

	/**
	 * Constructor that calls the parent level game grid.
	 * It gets the location ond Cell Value.
	 * @param grid that is set to the parent level game grid
	 */
	public BulletModel(CellValue[][] grid) {
		super(grid);
	}

	/**
	 * Function that enables the velocity of the bullet that should travelled and hit.
	 * Handles the edge cases when the bullet hits the wall and disappears.
	 *
	 * @return boolean detects the bullet/ laser hit to enemy or not.
	 */
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
