package view;

import javafx.geometry.Point2D;

public class AStarThread implements Runnable {
	
	private EnemyAIModel enemy;
	private Point2D playerLocation;

	/**
	 * Sets the player location to location.
	 * @param location of a point in the grid.
	 */
	public void setPlayerLocation(Point2D location) {
		playerLocation = location;

	}

	/**
	 * Sets the enemy to this enemy ai model.
	 */
	public void setEnemyAIModel(EnemyAIModel enemyAIModel) {
		enemy = enemyAIModel;

	}

	/**
	 * Gets the enemy AI model.
	 * @return enemy
	 */
	public EnemyAIModel getEnemyAIModel() {
		return enemy;

	}

	/**
	 * Implements the core method run that tracks and moves.
	 * Moves the enemy to the player location.
	 */
	@Override
	public void run() {
		enemy.moveEnemy(playerLocation);

	}
	
}

