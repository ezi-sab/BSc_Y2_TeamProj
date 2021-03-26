package view;

import javafx.geometry.Point2D;



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

    @FXML
    private int rowCount;
    @FXML
    private int columnCount;
    private CellValue[][] grid;
    private Point2D shipLocation;
    private Point2D enemy1Location;
    private Point2D enemy2Location;
    private Point2D shipVelocity;
    private Point2D enemy1Velocity;
    private Point2D enemy2Velocity;
    private static Direction lastDirection;
    private static Direction currentDirection;


    private int score;
    private int level;
    private int flagCount;
    private static boolean gameOver;
    private static boolean youWon;

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
        int shipRow = 0;
        int shipColumn = 0;
        int enemy1Row = 0;
        int enemy1Column = 0;
        int enemy2Row = 0;
        int enemy2Column = 0;

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
                        shipRow = row;
                        shipColumn = column;
                        break;
                    }
                    case "1":
                    {
                        thisValue = CellValue.ENEMY1STARTINGPOINT;
                        enemy1Row = row;
                        enemy1Column = column;
                        break;
                    }
                    case "2":
                    {
                        thisValue = CellValue.ENEMY2STARTINGPOINT;
                        enemy2Row = row;
                        enemy2Column = column;
                        break;
                    }
                    case "B":
                        thisValue = CellValue.BLOCK;
                        break;
                    case "F":
                        thisValue = CellValue.FLAG;
                        break;
                    default:
                        thisValue = CellValue.EMPTY;
                }
                grid[row][column] = thisValue;
                column++;
            }
            row++;
        }
        shipLocation = new Point2D(shipRow, shipColumn);
        shipVelocity = new Point2D(0, 0);
        currentDirection = Direction.NONE;
        lastDirection = Direction.NONE;
        enemy1Location = new Point2D(enemy1Row,enemy1Column);
        enemy1Velocity = new Point2D(-1, 0); //changed from -1,0
//        enemy1Velocity = new Point2D(0, 0); //changed from -1,0
        enemy2Location = new Point2D(enemy2Row,enemy2Column);
        enemy2Velocity = new Point2D(-1, 0); //changed from -1,0
//        enemy2Velocity = new Point2D(0, 0); //changed from -1,0
    }

    public void startNewGame(){
        rowCount = 0;
        columnCount = 0;
        this.gameOver = false;
        this.youWon = false;
        flagCount = 0;
        this.score = 0;
        this.level = 1;
        this.initializeLevel(Controller.getCurrentLevel(0));
    }

    public void moveShip(Direction direction) {
        Point2D predictedShipVelocity = changeVelocity(direction);
        Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
        predictedShipLocation = setOffScreenLocation(predictedShipLocation);
        if (direction.equals(lastDirection)) {
            if (grid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                shipVelocity = changeVelocity(Direction.NONE);
            } else {
                shipVelocity = predictedShipVelocity;
                shipLocation = predictedShipLocation;
                setLastDirection(direction);
            }
        }
        else {
            if (grid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                predictedShipVelocity = changeVelocity(lastDirection);
                predictedShipLocation = shipLocation.add(predictedShipVelocity);
                if (grid[(int) predictedShipLocation.getX()][(int) predictedShipLocation.getY()] == CellValue.BLOCK) {
                    shipVelocity = changeVelocity(Direction.NONE);
                } else {
                    shipVelocity = changeVelocity(lastDirection);
                    shipLocation = shipLocation.add(shipVelocity);
                }
            } else {
                shipVelocity = predictedShipVelocity;
                shipLocation = predictedShipLocation;
                setLastDirection(direction);
            }
        }
    }

    public Point2D[] enemyAI(Point2D velocity, Point2D location){
        Random generator = new Random();

        if (location.getY() == shipLocation.getY()) {
            if (location.getX() > shipLocation.getX()) {
                velocity = changeVelocity(Direction.UP);
            } else {
                velocity = changeVelocity(Direction.DOWN);
            }
            Point2D predictedLocation = location.add(velocity);
            //if the ghost would go offscreen, wrap around
            predictedLocation = setOffScreenLocation(predictedLocation);
            //generate new random directions until ghost can move without hitting a wall
            while (grid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
                Direction direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = location.add(velocity);
            }
            location = predictedLocation;
        }
        //check if ghost is in PacMan's row and move towards him
        else if (location.getX() == shipLocation.getX()) {
            if (location.getY() > shipLocation.getY()) {
                velocity = changeVelocity(Direction.LEFT);
            } else {
                velocity = changeVelocity(Direction.RIGHT);
            }
            Point2D predictedLocation = location.add(velocity);
            predictedLocation = setOffScreenLocation(predictedLocation);
            while (grid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
                Direction direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = location.add(velocity);
            }
            location = predictedLocation;
        }
        //move in a consistent random direction until it hits a wall, then choose a new random direction
        else{
            Point2D predictedLocation = location.add(velocity);
            predictedLocation = setOffScreenLocation(predictedLocation);
            while(grid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK){
                Direction direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = location.add(velocity);
            }
            location = predictedLocation;
        }

        Point2D[] data = {velocity, location};
        return data;

    }

    public void moveEnemy() {
        Point2D[] ghost1Data = enemyAI(enemy1Velocity, enemy1Location);
        Point2D[] ghost2Data = enemyAI(enemy2Velocity, enemy2Location);
        enemy1Velocity = ghost1Data[0];
        enemy1Location = ghost1Data[1];
        enemy2Velocity = ghost2Data[0];
        enemy2Location = ghost2Data[1];

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

    public Direction randomDirectionGenerator(){
        Random generator = new Random();
        int randInt = generator.nextInt(4);
        if (randInt == 0){
            return Direction.LEFT;
        }
        else if (randInt == 1){
            return Direction.RIGHT;
        }
        else if(randInt == 2){
            return Direction.UP;
        }
        else{
            return Direction.DOWN;
        }
    }


    public void step(Direction direction) {
        this.moveShip(direction);
        CellValue shipLocationCellValue = grid[(int) shipLocation.getX()][(int) shipLocation.getY()];
        if (shipLocationCellValue == CellValue.FLAG) {
            grid[(int) shipLocation.getX()][(int) shipLocation.getY()] = CellValue.EMPTY;
            flagCount--;
            score += 10;
        }
        if (shipLocation.equals(enemy1Location)) {
            gameOver = true;
            shipVelocity = new Point2D(0,0);
        }
        if (shipLocation.equals(enemy2Location)) {
            gameOver = true;
            shipVelocity = new Point2D(0,0);
        }
        this.moveEnemy();
        if (shipLocation.equals(enemy1Location)) {
            gameOver = true;
            shipVelocity = new Point2D(0,0);
        }
        if (shipLocation.equals(enemy2Location)) {
            gameOver = true;
            shipVelocity = new Point2D(0,0);
        }

        //start a new level if level is complete
        /*if (this.isLevelComplete()) {
            shipVelocity = new Point2D(0,0);
            startNextLevel();
        }*/
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static boolean isYouWon() {
        return youWon;
    }

    public Point2D getEnemy1Location() {
        return enemy1Location;
    }

    public void setEnemy1Location(Point2D enemy1Location) {
        this.enemy1Location = enemy1Location;
    }

    public Point2D getEnemy2Location() {
        return enemy2Location;
    }

    public void setEnemy2Location(Point2D enemy2Location) {
        this.enemy2Location = enemy2Location;
    }

    public Point2D getEnemy1Velocity() {
        return enemy1Velocity;
    }

    public void setEnemy1Velocity(Point2D enemy1Velocity) {
        this.enemy1Velocity = enemy1Velocity;
    }

    public Point2D getEnemy2Velocity() {
        return enemy2Velocity;
    }

    public void setEnemy2Velocity(Point2D enemy2Velocity) {
        this.enemy2Velocity = enemy2Velocity;
    }
}
