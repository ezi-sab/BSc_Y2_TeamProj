package view;

import java.util.Random;
import javafx.geometry.Point2D;

public class EnemyAIModel extends ShipModel{

    public EnemyAIModel(CellValue[][] gameGrid) {
        super(gameGrid);
        // TODO Auto-generated constructor stub
    }


    public Direction randomDirectionGenerator(){
        Random generator = new Random();
        int randInt;
        Direction randDirection;
        do {
            randInt = generator.nextInt(3);
            switch (randInt){
                case 0:		randDirection = Direction.RIGHT;
                    break;
                case 1:		randDirection = Direction.DOWN;
                    break;
                case 2:		randDirection = Direction.LEFT;
                    break;
                default: 	randDirection = Direction.UP;
                    break;
            }
        } while(randDirection == this.currentDirection);
        return randDirection;
    }


    public Point2D[] enemyAI(Point2D velocity, Point2D playerLocation){
        Random generator = new Random();
        Direction direction = currentDirection;
        if (shipLocation.getY() == playerLocation.getY()) {
            if (shipLocation.getX() > playerLocation.getX()) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
            velocity = changeVelocity(direction);
            Point2D predictedLocation = shipLocation.add(velocity);
            //if the ghost would go offscreen, wrap around
            predictedLocation = setOffScreenLocation(predictedLocation);
            //generate new random directions until ghost can move without hitting a wall
            while (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
                direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = shipLocation.add(velocity);
            }
            shipLocation = predictedLocation;
        }
        //check if ghost is in PacMan's row and move towards him
        else if (shipLocation.getX() == playerLocation.getX()) {
            if (shipLocation.getY() > playerLocation.getY()) {
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
            velocity = changeVelocity(direction);
            Point2D predictedLocation = shipLocation.add(velocity);
            predictedLocation = setOffScreenLocation(predictedLocation);
            while (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
                direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = shipLocation.add(velocity);
            }
            shipLocation = predictedLocation;
        }
        //move in a consistent random direction until it hits a wall, then choose a new random direction
        else{
            Point2D predictedLocation = shipLocation.add(velocity);
            predictedLocation = setOffScreenLocation(predictedLocation);
            while(gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK){
                direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = shipLocation.add(velocity);
            }
            shipLocation = predictedLocation;
        }

        this.lastDirection = this.currentDirection;
        this.currentDirection = direction;
        Point2D[] data = {velocity, shipLocation};
        return data;

    }


    public void moveEnemy(Point2D playerLocation) {
        Point2D[] enemyData = enemyAI(shipVelocity, playerLocation);
        shipVelocity = enemyData[0];
        shipLocation = enemyData[1];

    }


}
