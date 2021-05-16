package view;

import java.util.List;

import javafx.geometry.Point2D;

public class PlayerModel extends ShipModel{
	
	public PlayerModel(CellValue[][] gameGrid) {
		super(gameGrid);
		this.shipType = "PLAYER";
	}

	// generate a new bullet according to enemy or play's location and direction
    public void shoot(List<BulletModel> bullets) {
    	
		Point2D predictedShipVelocity = this.changeVelocity(this.getCurrentDirection(), 1);
		Point2D predictedShipLocation = this.shipLocation.add(predictedShipVelocity);
		
		if (gameGrid[(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
			
			BulletModel bullet = new BulletModel(gameGrid, this.getCurrentDirection());
			bullet.setLocation(predictedShipLocation);
			bullets.add(bullet);
			
		}
    }
	
	
	public void movePlayer(int fps, int sps) {
		Point2D predictedShipVelocity = changeVelocity(this.currentDirection, (double) sps/fps);
		Point2D predictedShipVelocityKeyFrame = changeVelocity(this.currentDirection, 1);

		Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
		Point2D predictedShipLocationKeyFrame = shipLocation.add(predictedShipVelocityKeyFrame);
		
        predictedShipLocation = setOffScreenLocation(predictedShipLocation);
        predictedShipLocationKeyFrame = setOffScreenLocation(predictedShipLocationKeyFrame);
        
        if (this.currentDirection.equals(lastDirection)) {
        	if (currentDirection.equals(Direction.UP) || currentDirection.equals(Direction.LEFT)) {
        		if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
	                shipVelocity = changeVelocity(Direction.NONE, 0);
	        	} 
	        	else {
		        	shipVelocity = predictedShipVelocity;
		            shipLocation = predictedShipLocation;
		            setLastDirection(this.currentDirection);
	            }
        	}
        	else {


	        	if (gameGrid[(int) predictedShipLocationKeyFrame.getX()][(int) predictedShipLocationKeyFrame.getY()] == CellValue.BLOCK) {
	                shipVelocity = changeVelocity(Direction.NONE, 0);
	        	} 
	        	else {
		        	shipVelocity = predictedShipVelocity;
		            shipLocation = predictedShipLocation;
		            setLastDirection(this.currentDirection);
	            }
        	}
        }
        else {
        	if (currentDirection.equals(Direction.UP) || currentDirection.equals(Direction.LEFT)) {
        		
	            if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
	                predictedShipVelocity = changeVelocity(lastDirection, (double) sps/fps);
	                predictedShipLocation = shipLocation.add(predictedShipVelocity);
	                if (gameGrid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
	                    shipVelocity = changeVelocity(Direction.NONE, 0);
	                } else {
	                    shipVelocity = changeVelocity(lastDirection, (double) sps/fps);
	                    shipLocation = shipLocation.add(shipVelocity);
	                }
	            } else {
	                shipVelocity = predictedShipVelocity;
	                shipLocation = predictedShipLocation;
	                setLastDirection(this.currentDirection);
	            }
        		
        	}
        	else {
        		
	            if (gameGrid[(int) predictedShipLocationKeyFrame.getX()][(int) predictedShipLocationKeyFrame.getY()] == CellValue.BLOCK) {
	                predictedShipVelocity = changeVelocity(lastDirection, (double) sps/fps);
	                predictedShipLocation = shipLocation.add(predictedShipVelocity);
	                if (gameGrid[(int) predictedShipLocationKeyFrame.getX()][(int) predictedShipLocationKeyFrame.getY()] == CellValue.BLOCK) {
	                    shipVelocity = changeVelocity(Direction.NONE, 0);
	                } else {
	                    shipVelocity = changeVelocity(lastDirection, (double) sps/fps);
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
	
}
