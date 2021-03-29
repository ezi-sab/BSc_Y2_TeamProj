package view;

import java.util.Random;

import javafx.geometry.Point2D;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;

public class EnemyAIModel extends ShipModel{
    
	public EnemyAIModel(CellValue[][] gameGrid) {
		super(gameGrid);
		// TODO Auto-generated constructor stub
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

	
    public Point2D[] enemyAI(Point2D velocity, Point2D playerLocation){
        Random generator = new Random();
        
        if (shipLocation.getY() == playerLocation.getY()) {
            if (shipLocation.getX() > playerLocation.getX()) {
                velocity = changeVelocity(Direction.UP);
            } else {
                velocity = changeVelocity(Direction.DOWN);
            }
            Point2D predictedLocation = shipLocation.add(velocity);
            //if the ghost would go offscreen, wrap around
            predictedLocation = setOffScreenLocation(predictedLocation);
            //generate new random directions until ghost can move without hitting a wall
            while (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
            	Direction direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = shipLocation.add(velocity);
            }
            shipLocation = predictedLocation;
        }
        //check if ghost is in PacMan's row and move towards him
        else if (shipLocation.getX() == playerLocation.getX()) {
            if (shipLocation.getY() > playerLocation.getY()) {
                velocity = changeVelocity(Direction.LEFT);
            } else {
                velocity = changeVelocity(Direction.RIGHT);
            }
            Point2D predictedLocation = shipLocation.add(velocity);
            predictedLocation = setOffScreenLocation(predictedLocation);
            while (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
            	Direction direction = randomDirectionGenerator();
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
                Direction direction = randomDirectionGenerator();
                velocity = changeVelocity(direction);
                predictedLocation = shipLocation.add(velocity);
            }
            shipLocation = predictedLocation;
        }
        
        Point2D[] data = {velocity, shipLocation};
        return data;
        
    }
    
    public void moveEnemy(Point2D playerLocation) {
        Point2D[] enemyData = enemyAI(shipVelocity, playerLocation);
        shipVelocity = enemyData[0];
        shipLocation = enemyData[1];

    }
    
}
