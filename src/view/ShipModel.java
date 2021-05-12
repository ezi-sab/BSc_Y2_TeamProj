package view;

import javafx.geometry.Point2D;

public class ShipModel {
	
    public enum CellValue {
        EMPTY, SHIPSTARTINGPOINT, BLOCK, FLAG, ENEMY1STARTINGPOINT, ENEMY2STARTINGPOINT, COIN, LIFE, POWERUP
    }
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
    
    protected Point2D shipLocation, shipVelocity;
    protected Direction lastDirection, currentDirection;
    
    static protected CellValue[][] gameGrid;
    protected int columnCount, rowCount;
    
    /**
     * Constructor method calling the grid and setups the scene with columns and rows respectively.
     * @param grid parent level game grid basically the coordinates of the level file.
     */
    public ShipModel(CellValue[][] grid) {
        gameGrid = grid;
        this.rowCount = grid.length;
        this.columnCount = grid[0].length;
    }
    
    /**
     * Changes the velocity of player and moves in four directions.
     * @param direction in which charcter can move
     * @return Point2D the coordinates in the level files.
     */
    public Point2D changeVelocity(Direction direction) {
    	
        if (direction == Direction.RIGHT) {
            return new Point2D(1, 0);
        } else if (direction == Direction.DOWN) {
        	return new Point2D(0, 1);
        } else if (direction == Direction.LEFT) {
            return new Point2D(-1, 0);
        } else if (direction == Direction.UP) {
            return new Point2D(0, -1);
        } else {
            return new Point2D(0, 0);
        }
        
    }
    
    /**
     * Function Teleports the player to opposite side of the game frame.
     * Sets the cell value to stop at the impossible areas in the game to traverse.
     * @param objectLocation coordinates of the object sets to new location.
     * @return
     */
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
    
    /**
     * Gets the cell value in th egrid
     * @param row in the grid
     * @param column in the grid
     * @return CellValue of particular row and column.
     */
    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < gameGrid.length && column >= 0 && column < gameGrid[0].length;
        return gameGrid[row][column];
    }
    
    /**
     * Gets the row count.
     * @return int count of rows.
     */
    public int getRowCount() {
        return rowCount;
    }
    
    /**
     * Gets the column count.
     * @return int count of columns.
     */
    public int getColumnCount() {
        return columnCount;
    }
    
    /**
     * Gets the location of player ship.
     * @return Point2D of the ship location
     */
    public Point2D getLocation() {
        return shipLocation;
    }
    
    /**
     * Sets the location of player ship.
     */
    public void setLocation(Point2D shipLocation) {
        this.shipLocation = shipLocation;
    }
    
    /**
     * Gets the velocity of the Ship.
     * @return Point2D velocity of ship.
     */
    public Point2D getVelocity() {
        return shipVelocity;
    }
    
    /**
     * Sets the velocity of the Ship.
     */
    public void setVelocity(Point2D shipVelocity) {
        this.shipVelocity = shipVelocity;
    }
    
    /**
     * Gets current direction of the character.
     * @return Direction of the character.
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }
    
    /**
     * Sets current direction of the character.
     */
    public void setCurrentDirection(Direction direction) {
    	this.currentDirection = direction;
    }
    
    /**
     * Gets last direction of the character.
     * @return Direction of the character
     */
    public Direction getLastDirection() {
        return lastDirection;
    }
    
    /**
     * Sets last direction of the character.
     */
    public void setLastDirection(Direction direction) {
    	this.lastDirection = direction;
    }
    
    /**
     * Sets the game grid.
     * @param grid is set to game grid
     */
    public void setGameGrid(CellValue[][] grid) {
    	gameGrid = grid;
    }
    
}
