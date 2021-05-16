package view;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class ShipModel {

    public enum CellValue {
        EMPTY, SHIPSTARTINGPOINT, BLOCK, FLAG, ENEMY1STARTINGPOINT, ENEMY2STARTINGPOINT, COIN, LIFE, POWERUP
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
    
    protected Point2D shipLocation, shipVelocity;
    protected Direction lastDirection, currentDirection, nextDirection;
    protected double currentRotation;
    
    static protected CellValue[][] gameGrid;
    protected int columnCount, rowCount;
    protected String shipType;
    

    public ShipModel(CellValue[][] grid) {
    	shipType = "SHIP";
        gameGrid = grid;
        this.rowCount = grid.length;
        this.columnCount = grid[0].length;
        this.currentRotation = 0.0;
    }


    public Point2D changeVelocity(Direction direction, double velocity) {
        if (direction == Direction.LEFT) {
            return new Point2D(-velocity, 0);
        } else if (direction == Direction.RIGHT) {
            return new Point2D(velocity, 0);
        } else if (direction == Direction.UP) {
            return new Point2D(0, -velocity);
        } else if (direction == Direction.DOWN) {
            return new Point2D(0, velocity);
        } else {
            return new Point2D(0, 0);
        }
        
    }


    public Point2D setOffScreenLocation(Point2D objectLocation) {
    	
        if (objectLocation.getY() > columnCount - 1) {
            objectLocation = new Point2D(objectLocation.getX(), 0);
        }
        
        if (objectLocation.getY() < 0) {
            objectLocation = new Point2D(objectLocation.getX(), columnCount - 1);
        }
        
        if (objectLocation.getX() > rowCount - 1) {
            objectLocation = new Point2D(0, objectLocation.getY());
        }
        
        if (objectLocation.getX() < 0) {
            objectLocation = new Point2D(rowCount -1, objectLocation.getY());
        }
        
        return objectLocation;
        
    }
    
    public int getDirectionRotation(ShipModel.Direction direction) {
    	switch (direction){
    	case LEFT:
    		return -90;
		case RIGHT:
			return 90;
		case DOWN:
			return 180;
		default:
			return 0;
    	}
    }
    
    public void rotateShip(ImageView[][] cellViews, int r, int c, int fps, int sps, int cFrame) {
    	
    	ShipModel.Direction nextDirection = this.getNextDirection();
    	ShipModel.Direction currentDirection = this.getCurrentDirection();
    	double currentRota = this.getCurrentRotation();
    	int framesBeforeNextStep = (int) (fps/sps) -  (int)(cFrame % (double)(fps/sps));

    	
    	//Keyframes
    	if (cFrame % (int)(fps/sps) == 0) {
    		this.setCurrentRotation(getDirectionRotation(currentDirection));	
    	}
    	else {
	    	if (currentDirection != nextDirection && nextDirection != null) {
		    	double nextRota = getDirectionRotation(nextDirection);
		    	double rotaDifference = nextRota - currentRota;
		    	double frameRotation = (double)rotaDifference/framesBeforeNextStep;
		    	
		    	if (rotaDifference > 180) {
		    		frameRotation = (double)(rotaDifference - 360)/framesBeforeNextStep;
		    	}
		    	else if (rotaDifference < -180) {
		    		frameRotation = (double)(rotaDifference + 360)/framesBeforeNextStep;
		    	}
		    
		    	this.setCurrentRotation(currentRota + frameRotation);
	    	}

    	}
    	cellViews[r][c].setRotate(this.getCurrentRotation());
    	
    }
    
    
    public Point2D roundLocation(Point2D location) {
    	Point2D roundedLocation = new Point2D (Math.round(location.getX()),Math.round(location.getY())) ;
    	return roundedLocation;
    }
    
    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < gameGrid.length && column >= 0 && column < gameGrid[0].length;
        return gameGrid[row][column];
    }
    
    public String getShipType() {
    	return this.shipType;
    }
    
    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
    
    public Point2D getLocation() {
        return shipLocation;
    }

    public void setLocation(Point2D shipLocation) {
        this.shipLocation = shipLocation;
    }
    
    public Point2D getVelocity() {
        return shipVelocity;
    }

    public void setVelocity(Point2D shipVelocity) {
        this.shipVelocity = shipVelocity;
    }
    
    public Direction getNextDirection() {
        return nextDirection;
    }

    public void setNextDirection(Direction direction) {
    	this.nextDirection = direction;
    }
    
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
    	this.currentDirection = direction;
    }

    public double getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(double rotation) {
    	this.currentRotation = rotation;
    }
    
    public Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction direction) {
    	this.lastDirection = direction;
    }
    
    public void setGameGrid(CellValue[][] grid) {
    	gameGrid = grid;
    }
}
