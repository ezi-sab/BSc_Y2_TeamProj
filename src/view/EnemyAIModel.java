package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import view.ShipModel.CellValue;
import view.ShipModel.Direction;

public class EnemyAIModel extends ShipModel{
	
    
	public EnemyAIModel(CellValue[][] gameGrid) {
		super(gameGrid);
		this.shipType = "ENEMYAI";
	}


	public Direction randomDirectionGenerator(){
    	Random generator = new Random();
    	int randInt;
    	Direction randDirection;
    	do {
    		randInt = generator.nextInt(3);
	        switch (randInt){
	        case 0:		randDirection = Direction.LEFT;
	        			break;
	        case 1:		randDirection = Direction.RIGHT;
						break;
    		case 2:		randDirection = Direction.UP;
						break;
    		default: 	randDirection = Direction.DOWN;
						break;
	        }
	        
    	} while(randDirection == this.nextDirection);
    	return randDirection;
    }
	
	
	public void moveEnemyChaseMode(Point2D playerLocation, int fps, int sps) {
		System.out.println("");
    	int xDistance = (int) (shipLocation.getX() - playerLocation.getX());
    	int yDistance = (int) (shipLocation.getY() - playerLocation.getY());
    	
    	if (xDistance < 0) {
    		xDistance = (-1) * xDistance;
    	}
    	
    	if (yDistance < 0) {
    		yDistance = (-1) * yDistance;
    	}
    	
    	int heuristic = xDistance + yDistance;
    	
//    	Don't increase the heuristic more than 15, game starts to hang after that
    	if (heuristic <= 14) {
    		
    		setLastDirection(currentDirection);
    		nextDirection = aStarPathFinding(playerLocation);
    		setCurrentDirection(nextDirection);
    		shipVelocity = changeVelocity(nextDirection, (double) sps/fps);
    		shipLocation = shipLocation.add(shipVelocity);
    		
    	} else {
    		nextDirection = randomDirectionGenerator();
    		Point2D predictedShipVelocity = changeVelocity(nextDirection,1);
    		Point2D predictedShipLocation = shipLocation.add(predictedShipVelocity);
    		
    		
    		/*System.out.println("Location");
			System.out.println(shipLocation.getX());
			System.out.println(shipLocation.getY());
			System.out.println(gameGrid [(int) shipLocation.getX()] [(int) shipLocation.getY()]);*/

    		if ((int)predictedShipLocation.getX() >= 0 && (int)predictedShipLocation.getX() < gameGrid.length && (int)predictedShipLocation.getY() >= 0 && (int)predictedShipLocation.getY() < gameGrid[0].length) {
    			
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				shipVelocity = changeVelocity(nextDirection, (double) sps/fps);
    				shipLocation = shipLocation.add(shipVelocity);
    				setLastDirection(currentDirection);
    				setCurrentDirection(nextDirection);
    				/*System.out.println("Predicted1");
					System.out.println(gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()]);*/
    			} else {
    				
    				do {
    					nextDirection = randomDirectionGenerator();
    					predictedShipVelocity = changeVelocity(nextDirection,1);
    					predictedShipLocation = shipLocation.add(predictedShipVelocity);
    					predictedShipLocation = roundLocation(predictedShipLocation);
    					/*System.out.println("Predicted2Loop");
    					System.out.println(gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()]);*/
    				} while ((int)predictedShipLocation.getX() < 0 || (int)predictedShipLocation.getX() >= gameGrid.length
    						|| (int)predictedShipLocation.getY() < 0 || (int)predictedShipLocation.getY() >= gameGrid[0].length
    	    				|| gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] == CellValue.BLOCK
    						);
					shipVelocity = changeVelocity(nextDirection, (double) sps/fps);
    				shipLocation = shipLocation.add(shipVelocity);
    				setLastDirection(currentDirection);
    				setCurrentDirection(nextDirection);
    				/*System.out.println("Predicted2");
					System.out.println(gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()]);*/
    				
    			}
    			
    		}
    		
    	}
    		
    }
    
    public void moveEnemy(int fps, int sps) {
    	Point2D nextShipVelocity = changeVelocity(this.currentDirection, (double) sps/fps);
		Point2D nextShipLocation = shipLocation.add(nextShipVelocity);
		
		nextShipLocation = setOffScreenLocation(nextShipLocation);
        
		shipVelocity = nextShipVelocity;
		shipLocation = nextShipLocation;
        
    }
    
    
    
    private Direction aStarPathFinding (Point2D playerLocation) {
    	
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> openNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> closedNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	
    	openNodes = findPossiblePoints(currentDirection, shipLocation, playerLocation);
    	
    	while (!(reachedPlayerLocation(openNodes, playerLocation))) {
    		
    		List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> equalFDistanceNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    		List <Integer> lowestDistanceIndex = new ArrayList<Integer>();
    		    		
    		int lowestDistance = 0;
    		if (openNodes.size() > 0){
    			lowestDistance = openNodes.get(0).getValue().getValue();
    		}
    		
    		for (int i = 0; i < openNodes.size(); i++) {
    			
    			if (openNodes.get(i).getValue().getValue() <= lowestDistance) {
    				
    				lowestDistanceIndex.add(i);
    				
    			}
    			
    		}
    		
    		if (lowestDistanceIndex.isEmpty()) {
    			
    			lowestDistanceIndex.add(0);
    			
    		}
    		
    		for (int i = 0; i < lowestDistanceIndex.size(); i++) {
    			
    			equalFDistanceNodes.add(openNodes.get(lowestDistanceIndex.get(i)));
    			
    		}
    		
    		Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>> currentNode = findClockWiseDirection(equalFDistanceNodes);
    		
    		openNodes.remove(currentNode);
    		closedNodes.add(currentNode);
    		
    		List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> newNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    		newNodes.addAll(findPossiblePoints(currentNode.getKey().getKey(), (new Point2D(currentNode.getValue().getKey().getX(), currentNode.getValue().getKey().getY())), playerLocation));
    		
    		List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> objectToRemove = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    		
    		for (int i = 0; i < openNodes.size(); i++) {
    			
    			for (int j = 0; j < newNodes.size(); j++) {
    				
    				if ((openNodes.get(i).getValue().getKey().getX() == newNodes.get(j).getValue().getKey().getX()) && (openNodes.get(i).getValue().getKey().getY() == newNodes.get(j).getValue().getKey().getY())) {
    					
    					if (openNodes.get(i).getValue().getValue() >= newNodes.get(j).getValue().getValue()) {
    						objectToRemove.add(openNodes.get(i));
    					}
    					
    				}
    				
    			}
    			
    		}
    		
    		for (int i = 0; i < objectToRemove.size(); i++) {
    			
    			openNodes.remove(objectToRemove.get(i));
    			
    		}
    		
    		openNodes.addAll(newNodes);
    		
    	}
    	
    	for (int i = 0; i < openNodes.size(); i++) {
    	
    		if ((openNodes.get(i).getValue().getKey().getX() == playerLocation.getX()) && (openNodes.get(i).getValue().getKey().getY() == playerLocation.getY())) {
    			
    			closedNodes.add(openNodes.get(i));
    			
    		}
    		
    	}
    	
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> reversedClosedNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	
    	for (int i = closedNodes.size() - 1; i >= 0; i--) {
    		reversedClosedNodes.add(closedNodes.get(i));
    	}
    	
    	List<Pair<Direction, Point2D>> reversePath = new ArrayList<Pair<Direction, Point2D>>();
    	reversePath = trackPath(reversedClosedNodes, shipLocation, playerLocation);
    	
    	Direction nextDirection;
    	Point2D predictedVelocity, predictedLocation;
    	
    	if (reversePath.size() == 0) {
    		do {
    			nextDirection = randomDirectionGenerator();
    			predictedVelocity = changeVelocity(nextDirection, 1);
    			predictedLocation = shipLocation.add(predictedVelocity);
    			predictedLocation = roundLocation(predictedLocation);
    		} while ((int)predictedLocation.getX() < 0 || (int)predictedLocation.getX() >= gameGrid.length 
					|| (int)predictedLocation.getY() < 0 || (int)predictedLocation.getY() >= gameGrid[0].length
					|| gameGrid [(int) predictedLocation.getX()] [(int) predictedLocation.getY()] == CellValue.BLOCK 
					);
    		
    	} else {
    		nextDirection = reversePath.get(reversePath.size() - 1).getKey();
    	}
    	
    	return nextDirection;
    	
    }
    
    
    private List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> findPossiblePoints(Direction direction, Point2D enemyLocation, Point2D playerLocation) {
    	
    	List<Pair<Direction, Point2D>> possiblePoints = new ArrayList<Pair<Direction, Point2D>>();
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> pointDistance = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	
    	if (direction == Direction.UP) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.UP, 1);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.UP, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.RIGHT, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.RIGHT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.LEFT, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.LEFT, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.RIGHT) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.RIGHT, 1);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.RIGHT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.DOWN, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.DOWN, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.UP, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.UP, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.DOWN) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.DOWN, 1);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.DOWN, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.LEFT, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.LEFT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.RIGHT, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.RIGHT, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.LEFT) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.LEFT, 1);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.LEFT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.UP, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.UP, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.DOWN, 1);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.DOWN, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.NONE) {
    		
    		direction = randomDirectionGenerator();
    		Point2D predictedShipVelocity = changeVelocity(direction, 1);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		predictedShipLocation = roundLocation(predictedShipLocation);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < gameGrid.length && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < gameGrid[0].length) {
    			
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (direction, predictedShipLocation));
    			} else {
    				
    				do {
    					nextDirection = randomDirectionGenerator();
    					predictedShipVelocity = changeVelocity(nextDirection,1);
    					predictedShipLocation = shipLocation.add(predictedShipVelocity);
    					predictedShipLocation = roundLocation(predictedShipLocation);

    				} while ((int)predictedShipLocation.getX() < 0 || (int)predictedShipLocation.getX() >= gameGrid.length 
    						|| (int)predictedShipLocation.getY() < 0 || (int)predictedShipLocation.getY() >= gameGrid[0].length
    						|| gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] == CellValue.BLOCK 
    						);
    				possiblePoints.add(new Pair<Direction, Point2D> (direction, predictedShipLocation));
    				
    			}
    			
    		}
    		
    	}
    	
    	for (int i = 0; i < possiblePoints.size(); i++) {
    		
    		int xShipDistance = (int) (shipLocation.getX() - possiblePoints.get(i).getValue().getX());
    		int yShipDistance = (int) (shipLocation.getY() - possiblePoints.get(i).getValue().getY());
    		int xPlayerDistance = (int) (playerLocation.getX() - possiblePoints.get(i).getValue().getX());
    		int yPlayerDistance = (int) (playerLocation.getY() - possiblePoints.get(i).getValue().getY());
    		
    		if (xShipDistance < 0) {
    			xShipDistance = (-1) * xShipDistance;
    		}
    		
    		if (yShipDistance < 0) {
    			yShipDistance = (-1) * yShipDistance;
    		}
    		
    		if (xPlayerDistance < 0) {
    			xPlayerDistance = (-1) * xPlayerDistance;
    		}
    		
    		if (yPlayerDistance < 0) {
    			yPlayerDistance = (-1) * yPlayerDistance;
    		}
    		
    		int shipAndPointDistance = xShipDistance + yShipDistance;
    		int heuristics = xPlayerDistance + yPlayerDistance;
    		
    		Pair<Direction, Point2D> distanceAndPoint = new Pair<Direction, Point2D> (possiblePoints.get(i).getKey(), (new Point2D(enemyLocation.getX(), enemyLocation.getY())));
    		Pair<Point2D, Integer> fDistance = new Pair<Point2D, Integer> ((new Point2D(possiblePoints.get(i).getValue().getX(), possiblePoints.get(i).getValue().getY())), shipAndPointDistance + heuristics);
    		pointDistance.add(new Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>> (distanceAndPoint, fDistance));
    		
    	}
    	
    	return pointDistance;
    	
    }
    
    
    private boolean reachedPlayerLocation(List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> location, Point2D playerLocation) {
    	
    	for (int i = 0; i < location.size(); i++) {
    		
    		if ((location.get(i).getValue().getKey().getX() == playerLocation.getX()) && (location.get(i).getValue().getKey().getY() == playerLocation.getY())) {
    			return true;
    		}
    		
    	}
    	
    	return false;
    	
    }
    
    
    private Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>> findClockWiseDirection(List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> equalNodes) {
    	
    	for (int i = 0; i < equalNodes.size(); i++) {
    		
    		if (equalNodes.get(i).getKey().getKey() == Direction.UP) {
    			
    			return equalNodes.get(i);
    			
    		}
    		
    	}
    	
    	for (int i = 0; i < equalNodes.size(); i++) {
    		
    		if (equalNodes.get(i).getKey().getKey() == Direction.RIGHT) {
    			
    			return equalNodes.get(i);
    			
    		}
    		
    	}
    	
    	for (int i = 0; i < equalNodes.size(); i++) {
    		
    		if (equalNodes.get(i).getKey().getKey() == Direction.DOWN) {
    			
    			return equalNodes.get(i);
    			
    		}
    		
    	}
    	
    	return equalNodes.get(0);
    	
    }
    
    
    private List<Pair<Direction, Point2D>> trackPath(List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> closedNodes, Point2D enemyLocation, Point2D playerLocation) {
    	
    	List<Pair<Direction, Point2D>> path = new ArrayList<Pair<Direction, Point2D>>();
    	Point2D point = new Point2D(playerLocation.getX(), playerLocation.getY());
    	
    	while (!((point.getX() == enemyLocation.getX()) && (point.getY() == enemyLocation.getY()))) {
    		
    		for (int i = 0; i < closedNodes.size(); i++) {
    			
    			if ((closedNodes.get(i).getValue().getKey().getX() == point.getX()) && (closedNodes.get(i).getValue().getKey().getY() == point.getY())) {
    				
    				path.add(new Pair<Direction, Point2D> (closedNodes.get(i).getKey().getKey(), (new Point2D(closedNodes.get(i).getKey().getValue().getX(), closedNodes.get(i).getKey().getValue().getY()))));
    				point = new Point2D(closedNodes.get(i).getKey().getValue().getX(), closedNodes.get(i).getKey().getValue().getY());
    				
    			}
    			
    		}
    		
    	}
    	
    	return path;
    	
    }
    
    
}
