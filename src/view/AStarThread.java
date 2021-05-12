package view;

import javafx.geometry.Point2D;

public class AStarThread implements Runnable {
	
	private EnemyAIModel enemy;
	private Point2D playerLocation;
	private int level;
	
	public void setPlayerLocation(Point2D location) {
		playerLocation = location;
	}
	
	public void setEnemyAIModel(EnemyAIModel enemyAIModel) {
		enemy = enemyAIModel;
	}
	
	public EnemyAIModel getEnemyAIModel() {
		return enemy;
	}
	
	public void setLevel(int lel) {
		level = lel;
	}
	
	@Override
	public void run() {
		enemy.moveEnemy(playerLocation, level);
	}
	
}
