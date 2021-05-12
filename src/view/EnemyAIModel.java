package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.util.Pair;

public class EnemyAIModel extends ShipModel{
	
	/**
	 * Constructor method that calls the parent method's gameGrid for EnemyAI controls.
	 * @param gameGrid calls the parent gameGrid
	 */
	public EnemyAIModel(CellValue[][] gameGrid) {
		super(gameGrid);
	}
	
	/**
	 * A Random direction for Enemy AI is generated (UP, DOWN, RIGHT, LEFT).
	 * @return Direction generate the random direction and is returned.
	 */
	public Direction randomDirectionGenerator(){
		
    	Random generator = new Random();
    	int randInt;
    	Direction randomDirection;
    	randInt = generator.nextInt(3);
    	
    	switch (randInt){
    	case 0:		randomDirection = Direction.UP;
    	break;
    	case 1:		randomDirection = Direction.RIGHT;
    	break;
    	case 2:		randomDirection = Direction.DOWN;
    	break;
    	default: 	randomDirection = Direction.LEFT;
    	break;
    	}
    	
    	return randomDirection;
    	
    }
	
	/**
	 * Enemy detects the player location in the game grid and moved according.
	 * A heuristic based search for enemy to chase the player.
	 * Mode that searches for player from the game grid graph.
	 * @param playerLocation used to get the location of player in the Grid.
	 */
    public void moveEnemy(Point2D playerLocation, int level) {
    	
    	int xDistance = (int) (shipLocation.getX() - playerLocation.getX());
    	int yDistance = (int) (shipLocation.getY() - playerLocation.getY());
    	
    	if (xDistance < 0) {
    		xDistance = (-1) * xDistance;
    	}
    	
    	if (yDistance < 0) {
    		yDistance = (-1) * yDistance;
    	}
    	
    	int heuristic = xDistance + yDistance;
    	
    	int maxHeuristics;
    	
    	if (level == 0) {
    		maxHeuristics = 20;
    	} else if (level == 1) {
    		maxHeuristics = 20;
    	} else {
    		maxHeuristics = 25;
    	}
    	
    	if (heuristic <= maxHeuristics) {
    		
    		setLastDirection(currentDirection);
    		Direction direction = aStarPathFinding(playerLocation);
    		setCurrentDirection(direction);
    		shipVelocity = changeVelocity(direction);
    		shipLocation = shipLocation.add(shipVelocity);
    		
    	} else {
    		
    		setLastDirection(currentDirection);
    		Direction direction = simplePathFinding(playerLocation);
    		setCurrentDirection(direction);
    		shipVelocity = changeVelocity(direction);
    		shipLocation = shipLocation.add(shipVelocity);
    		
    	}
    		
    }
    
	/**
	 * Implements the A Star path finding algorithm and detects the possible nodes that should be visited.
	 * Nodes are visited according to heuristic function and cost function.
	 * The Best feasible and optimal path is then selected to chase the player by the AI.
	 * @param playerLocation used to get the location of player in the Grid.
	 * @return Direction after the A star algorithm search.
	 */
	private Direction aStarPathFinding (Point2D playerLocation) {
    	
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> openNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> closedNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	
    	openNodes = findPossiblePoints(currentDirection, shipLocation, playerLocation);
    	
    	while (!(reachedPlayerLocation(openNodes, playerLocation))) {
    		
    		List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> equalFDistanceNodes = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    		List <Integer> lowestDistanceIndex = new ArrayList<Integer>();
    		    		
    		int lowestDistance = openNodes.get(0).getValue().getValue();
    		
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
    		
    		for (int i = 0; i < openNodes.size(); i++) {
    			for (int j = 0; j < newNodes.size(); j++) {
    				if ((openNodes.get(i).getValue().getKey().getX() == newNodes.get(j).getValue().getKey().getX()) && (openNodes.get(i).getValue().getKey().getY() == newNodes.get(j).getValue().getKey().getY())) {
    					if (openNodes.get(i).getValue().getValue() >= newNodes.get(j).getValue().getValue()) {
    						openNodes.set(i, newNodes.get(j));
    						newNodes.remove(j);
    					}
    				}
    			}
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
    	
    	if (reversePath.size() == 0) {
    		while (true) {
    			nextDirection = randomDirectionGenerator();
    			Point2D predictedVelocity = changeVelocity(nextDirection);
    			Point2D predictedLocation = shipLocation.add(predictedVelocity);
    			if (predictedLocation.getX() >= 0 && predictedLocation.getX() < 37 && predictedLocation.getY() >= 0 && predictedLocation.getY() < 21) {
        			if (gameGrid [(int) predictedLocation.getX()] [(int) predictedLocation.getY()] != CellValue.BLOCK) {
        				break;
        			}
    			}
    		}
    		
    	} else {
    		nextDirection = reversePath.get(reversePath.size() - 1).getKey();
    	}
    	
    	return nextDirection;
    	
    }
	
	/**
	 * Algorithm searches for necessary point inside the level files from enemy perspective.
	 * The points are typically the coordinates in the game grid and game level.
	 * It stores the values of points in a List of Pairs with their position and int value.
	 * @param direction Gets the direction from randomDirectionGenerator.
	 * @param enemyLocation Gets the enemy location from game grid namely X and Y coordinates.
	 * @param playerLocation Gets the player location from game grid namely X and Y coordinates.
	 * @return findPossiblePoints after the node search.
	 */
	private List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> findPossiblePoints(Direction direction, Point2D enemyLocation, Point2D playerLocation) {
    	
    	List<Pair<Direction, Point2D>> possiblePoints = new ArrayList<Pair<Direction, Point2D>>();
    	List<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>> pointDistance = new ArrayList<Pair<Pair<Direction, Point2D>, Pair<Point2D, Integer>>>();
    	
    	if (direction == Direction.UP) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.UP);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.UP, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.RIGHT);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.RIGHT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.LEFT);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.LEFT, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.RIGHT) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.RIGHT);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.RIGHT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.DOWN);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.DOWN, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.UP);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.UP, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.DOWN) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.DOWN);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.DOWN, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.LEFT);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.LEFT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.RIGHT);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.RIGHT, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.LEFT) {
    		
    		Point2D predictedShipVelocity = changeVelocity(Direction.LEFT);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.LEFT, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.UP);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.UP, predictedShipLocation));
    			}
    		}
    		
    		predictedShipVelocity = changeVelocity(Direction.DOWN);
    		predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (Direction.DOWN, predictedShipLocation));
    			}
    		}
    		
    	} else if (direction == Direction.NONE) {
    		
    		direction = randomDirectionGenerator();
    		Point2D predictedShipVelocity = changeVelocity(direction);
    		Point2D predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    		
    		if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    			
    			if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    				possiblePoints.add(new Pair<Direction, Point2D> (direction, predictedShipLocation));
    			} else {
    				
    				while (true) {
    					
    					direction = randomDirectionGenerator();
    					predictedShipVelocity = changeVelocity(direction);
    					predictedShipLocation = enemyLocation.add(predictedShipVelocity);
    					
    					if (predictedShipLocation.getX() >= 0 && predictedShipLocation.getX() < 37 && predictedShipLocation.getY() >= 0 && predictedShipLocation.getY() < 21) {
    						if (gameGrid [(int) predictedShipLocation.getX()] [(int) predictedShipLocation.getY()] != CellValue.BLOCK) {
    							possiblePoints.add(new Pair<Direction, Point2D> (direction, predictedShipLocation));
    							break;
    						}
    					}
    					
    				}
    				
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
    
	/**
	 * For the enemy traversing in different directions gets the direction and Location(Point 2D).
	 * Every Direction (UP, DOWN, RIGHT, LEFT) a List of Pairs with Direction and Point2D is stored.
	 * Equivalent positions are returned in form of directions.
	 * @param equalNodes Gets the similar nodes from the search space graph.
	 * @return findClockWiseDirection for the enemy to traverse the graph.
	 */
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
	
	/**
	 * Checks and performs the path when player and enemy aren't collided.
	 * Traverses the graph in keeping while keeping track of enemy and player locations.
	 * @param closedNodes Nodes without the path for enemy to traverse in.
	 * @param enemyLocation Gets the enemy location from game grid namely X and Y coordinates.
	 * @param playerLocation Gets the player location from game grid namely X and Y coordinates.
	 * @return trackPath after accessing the nodes and searching for locations
	 */
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
	
	/**
	 * Implements the simplest AI algorithm.
	 * Checks for the both enemy and player character if traverses in same row or column.
	 * If checks are true then enemies chase the player.
	 * @param playerLocation Location of the player on game grid
	 * @return Direction that enemy should follow.
	 */
    private Direction simplePathFinding(Point2D playerLocation){
    	
        Direction direction = currentDirection == Direction.NONE ? Direction.UP : currentDirection;
        
        if (shipLocation.getY() == playerLocation.getY()) {
        	
            if (shipLocation.getX() > playerLocation.getX()) {
            	direction = Direction.LEFT;
            } else {
            	direction = Direction.RIGHT;
            }
            
            Point2D predictedVelocity = changeVelocity(direction);
            Point2D predictedLocation = shipLocation.add(predictedVelocity);
            
            if (!(predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            	
            	while (true) {
            		direction = randomDirectionGenerator();
            		predictedVelocity = changeVelocity(direction);
            		predictedLocation = shipLocation.add(predictedVelocity);
            		if ((predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            			if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] != CellValue.BLOCK) {
            				break;            				
            			}
            		}
            	}
            	
            } else {
            	
            	if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
            		
            		while(true) {
            			direction = randomDirectionGenerator();
            			predictedVelocity = changeVelocity(direction);
            			predictedLocation = shipLocation.add(predictedVelocity);
            			if ((predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            				if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] != CellValue.BLOCK) {
            					break;            				
            				}
            			}
            		}
            		
            	}
            	
            }
            
            
        } else if (shipLocation.getX() == playerLocation.getX()) {
        	
            if (shipLocation.getY() > playerLocation.getY()) {
            	direction = Direction.UP;
            } else {
            	direction = Direction.DOWN;
            }
            
            Point2D predictedVelocity = changeVelocity(direction);
            Point2D predictedLocation = shipLocation.add(predictedVelocity);
            
            if (!(predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            	
            	while (true) {
            		direction = randomDirectionGenerator();
            		predictedVelocity = changeVelocity(direction);
            		predictedLocation = shipLocation.add(predictedVelocity);
            		if ((predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            			if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] != CellValue.BLOCK) {
            				break;            				
            			}
            		}
            	}
            	
            } else {
            	
            	if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
            		
            		while(true) {
            			direction = randomDirectionGenerator();
            			predictedVelocity = changeVelocity(direction);
            			predictedLocation = shipLocation.add(predictedVelocity);
            			if ((predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            				if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] != CellValue.BLOCK) {
            					break;            				
            				}
            			}
            		}
            		
            	}
            	
            }
        } else {
        	
        	Point2D predictedVelocity = changeVelocity(direction);
            Point2D predictedLocation = shipLocation.add(predictedVelocity);
            
            if (!(predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            	
            	while (true) {
            		direction = randomDirectionGenerator();
            		predictedVelocity = changeVelocity(direction);
            		predictedLocation = shipLocation.add(predictedVelocity);
            		if ((predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            			if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] != CellValue.BLOCK) {
            				break;            				
            			}
            		}
            	}
            	
            } else {
            	
            	if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] == CellValue.BLOCK) {
            		
            		while(true) {
            			direction = randomDirectionGenerator();
            			predictedVelocity = changeVelocity(direction);
            			predictedLocation = shipLocation.add(predictedVelocity);
            			if ((predictedLocation.getX() > 0 && predictedLocation.getX() < 37 && predictedLocation.getY() > 0 && predictedLocation.getY() < 21)) {
            				if (gameGrid[(int) predictedLocation.getX()][(int) predictedLocation.getY()] != CellValue.BLOCK) {
            					break;            				
            				}
            			}
            		}
            		
            	}
            	
            }
        }
        
        return direction;
        
    }
    
}
