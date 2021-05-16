package view;

import javafx.geometry.Point2D;
/**
 * The BulletModel class defines a bullet and the move action of the bullet
 *
 */
public class BulletModel extends ShipModel {

	public BulletModel(CellValue[][] grid, Direction dir) {
		super(grid);
		isActive = true;
		this.currentDirection = dir;
		this.currentRotation = getDirectionRotation(this.currentDirection);
	}
	
	private boolean isActive;
	
	public void flyBullet(int fps, int sps) {

		Point2D predictedBulletVelocity = changeVelocity(this.currentDirection, (double) ((double)sps/fps)*2);
		Point2D predictedBulletLocation = shipLocation.add(predictedBulletVelocity);
		predictedBulletLocation = setOffScreenLocation(predictedBulletLocation);
		// disappear when hit the wall
		if (gameGrid[(int) predictedBulletLocation.getX()][(int) predictedBulletLocation.getY()] == CellValue.BLOCK) {
			
			this.shipVelocity = changeVelocity(Direction.NONE, 0);
			isActive = false;
			
		} else {
			
			this.shipVelocity = predictedBulletVelocity;
			this.shipLocation = predictedBulletLocation;
			setLastDirection(this.currentDirection);
			
		}
		
	}
	
	public boolean getIsActiveBullet() {
		return isActive;
	}
	
}
