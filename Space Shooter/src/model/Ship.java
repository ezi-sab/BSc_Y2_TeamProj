package model;

public enum Ship{
	
	blue("view/resources/shipPicker/playerShip3_blue.png"),
	green("view/resources/shipPicker/playerShip3_green.png"),
	orange("view/resources/shipPicker/playerShip3_orange.png"),
	red("view/resources/shipPicker/playerShip3_red.png");
	
	private String urlShip;
	
	private Ship(String urlShip) {
		this.urlShip = urlShip;
	}
	
	public String getUrl() {
		return this.urlShip;
	}

}
