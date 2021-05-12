package model;

public enum Ship{
	
	blue("/resources/Images/PlayerShip-Blue-image.png", "/resources/Images/PowerUp-Life-image.png", "/resources/Images/Laser-Blue-image.png", "/resources/Images/PowerUp-Laser-image.png"),
	green("/resources/Images/PlayerShip-Green-image.png", "/resources/Images/PowerUp-Life-image.png", "/resources/Images/Laser-Green-image.png", "/resources/Images/PowerUp-Laser-image.png"),
	orange("/resources/Images/PlayerShip-Orange-image.png", "/resources/Images/PowerUp-Life-image.png", "/resources/Images/Laser-Orange-image.png", "/resources/Images/PowerUp-Laser-image.png"),
	red("/resources/Images/PlayerShip-Red-image.png", "/resources/Images/PowerUp-Life-image.png", "/resources/Images/Laser-Red-image.png", "/resources/Images/PowerUp-Laser-image.png");
	
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
