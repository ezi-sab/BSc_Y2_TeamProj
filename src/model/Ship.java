package model;

public enum Ship{
	
	blue("/res/playerShip3_blue.png", "/res/starSilver.png", "/res/laserBlue.png", "/res/boltBronze.png"),
	green("/res/playerShip3_green.png", "/res/starSilver.png", "/res/laserGreen.png", "/res/boltBronze.png"),
	orange("/res/playerShip3_orange.png", "/res/starSilver.png", "/res/laserOrange.png", "/res/boltBronze.png"),
	red("/res/playerShip3_red.png", "/res/starSilver.png", "/res/laserRed.png", "/res/boltBronze.png");
	
	private String urlShip;
	private String urlLife;
	private String urlLaser;
	private String urlPowerUp;
	
	private Ship(String urlShip, String urlLife, String urlLaser, String urlPowerUp) {
		
		this.urlShip = urlShip;
		this.urlLife = urlLife;
		this.urlLaser = urlLaser;
		this.urlPowerUp = urlPowerUp;
		
	}
	
	public String getLifeUrl() {
		return urlLife;
	}
	
	public String getShipUrl() {
		return urlShip;
	}
	
	public String getLaserUrl() {
		return urlLaser;
	}
	
	public String getPowerUpUrl() {
		return urlPowerUp;
	}

}
