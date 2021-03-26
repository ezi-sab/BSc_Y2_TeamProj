package view;

import java.util.Random;

import javafx.geometry.Point2D;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;

public class EnemyAIModel extends ShipModel{
    
	public EnemyAIModel(CellValue[][] gameGrid, int rowCount, int columnCount) {
		super(gameGrid, columnCount, columnCount);
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
            while (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
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
            while (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
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
            while(gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK){
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
        Point2D[] enemyData = enemyAI(shipVelocity, shipLocation);
        shipVelocity = enemyData[0];
        shipLocation = enemyData[1];

    }
    
}
