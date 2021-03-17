package view;

import javafx.geometry.Point2D;

import javafx.fxml.FXML;

import java.io.*;

import java.util.*;

public class ShipModel {

    public enum CellValue {
        EMPTY, SHIPSTARTINGPOINT
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    @FXML
    private int rowCount;
    @FXML
    private int columnCount;
    private CellValue[][] grid;
    private Point2D shipLocation;
    private Point2D shipVelocity;
    private static Direction lastDirection;
    private static Direction currentDirection;

    public ShipModel() {
        this.startNewGame();
    }

    public void initializeLevel(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            @SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                lineScanner.next();
                columnCount++;
            }
            rowCount++;
        }
        columnCount = columnCount / rowCount;
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        
        int row = 0;
        int pacmanRow = 0;
        int pacmanColumn = 0;
        
        while (scanner2.hasNextLine()) {
            int column = 0;
            String line = scanner2.nextLine();
            @SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                String value = lineScanner.next();
                CellValue thisValue;
                switch (value) {
                    case "P":
                    {
                        thisValue = CellValue.SHIPSTARTINGPOINT;
                        pacmanRow = row;
                        pacmanColumn = column;
                        break;
                    }
                    default:
                    	thisValue = CellValue.EMPTY;
                }
                grid[row][column] = thisValue;
                column++;
            }
            row++;
        }
        shipLocation = new Point2D(pacmanRow, pacmanColumn);
        shipVelocity = new Point2D(0, 0);
        currentDirection = Direction.NONE;
        lastDirection = Direction.NONE;
    }

    public void startNewGame(){
        rowCount = 0;
        columnCount = 0;
        this.initializeLevel(Controller.getLevelFile(0));
    }

    public void moveShip(Direction direction) {
        Point2D predictedShipVelocity = changeVelocity(direction);
        Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
        if (direction.equals(lastDirection)) {
        	shipVelocity = predictedShipVelocity;
            shipLocation = predictedShipLocation;
        } else {
        	shipVelocity = predictedShipVelocity;
            shipLocation = predictedShipLocation;
            setLastDirection(direction);
            }
        }


    public void step(Direction direction) {
        this.moveShip(direction);
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

    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.grid.length && column >= 0 && column < this.grid[0].length;
        return this.grid[row][column];
    }

    public static Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
        currentDirection = direction;
    }

    public static Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction direction) {
        lastDirection = direction;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Point2D getShipLocation() {
        return shipLocation;
    }
}
