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

	/**
	 * Constructor the sets the String parameters and initialize them with those supplied /feeded in.
	 * @param urlShip All ships resource url strings.
	 * @param urlLife All Lives resource url strings.
	 * @param urlLaser All laser resource url strings.
	 * @param urlPowerUp All power-ups resource url strings.
	 */
	private Ship(String urlShip, String urlLife, String urlLaser, String urlPowerUp) {
		
		this.urlShip = urlShip;
		this.urlLife = urlLife;
		this.urlLaser = urlLaser;
		this.urlPowerUp = urlPowerUp;
		
	}

	/**
	 * Gets the URL of lives.
	 * @return String URL of Life
	 */
	public String getLifeUrl() {
		return urlLife;
	}
	/**
	 * Gets the URL of ships.
	 * @return String URL of ships.
	 */
	public String getShipUrl() {
		return urlShip;
	}
	/**
	 * Gets the URL of lasers.
	 * @return String URL of lasers.
	 */
	public String getLaserUrl() {
		return urlLaser;
	}
	/**
	 * Gets the URL of power-ups.
	 * @return String URL of power-ups.
	 */
	public String getPowerUpUrl() {
		return urlPowerUp;
	}

}
