package view;

import javafx.geometry.Point2D;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;
import javafx.fxml.FXML;

import java.io.*;

import java.util.*;



public class ShipModel {

    public enum CellValue {
        EMPTY, SHIPSTARTINGPOINT, BLOCK, FLAG, ENEMY1STARTINGPOINT, ENEMY2STARTINGPOINT, COIN
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
    
    protected Point2D shipLocation, shipVelocity;
    protected Direction lastDirection, currentDirection;
    
    static protected CellValue[][] gameGrid;
    protected int columnCount, rowCount;


    public ShipModel(CellValue[][] grid, int rowCount, int columnCount) {
        gameGrid = grid;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }



    public Point2D changeVelocity(Direction direction) {
        if (direction == Direction.LEFT) {
            return new Point2D(0, -1);
        } else if (direction == Direction.RIGHT) {
            return new Point2D(0, 1);
        } else if (direction == Direction.UP) {
            return new Point2D(-1, 0);
        } else if (direction == Direction.DOWN) {
            return new Point2D(1, 0);
        } else {
            return new Point2D(0, 0);
        }
    }

    public Point2D setOffScreenLocation(Point2D objectLocation) {
        if (objectLocation.getY() >= columnCount) {
            objectLocation = new Point2D(objectLocation.getX(), 0);
        }
        if (objectLocation.getY() < 0) {
            objectLocation = new Point2D(objectLocation.getX(), columnCount - 1);
        }
        
        if (objectLocation.getX() >= rowCount) {
            objectLocation = new Point2D(0, objectLocation.getY());
        }
        if (objectLocation.getX() < 0) {
            objectLocation = new Point2D(rowCount -1, objectLocation.getY());
        }
        return objectLocation;
    }
    
    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < gameGrid.length && column >= 0 && column < gameGrid[0].length;
        return gameGrid[row][column];
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
    
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
    	this.currentDirection = direction;
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
