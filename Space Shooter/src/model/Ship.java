package model;

public enum Ship{
	
	/*blue("view/resources/shipPicker/playerShip3_blue.png"),
	green("view/resources/shipPicker/playerShip3_green.png"),
	orange("view/resources/shipPicker/playerShip3_orange.png"),
	red("view/resources/shipPicker/playerShip3_red.png"); */

    blue("/res/playerShip3_blue.png", "/res/playerLife3_blue.png"),
    green("/res/playerShip3_green.png", "/res/playerLife3_green.png"),
    orange("/res/playerShip3_orange.png", "/res/playerLife3_orange.png"),
    red("/res/playerShip3_red.png", "/res/playerLife3_red.png");

    private String urlShip;
    private String urlLife;

    private Ship(String urlShip, String urlLife) {
        this.urlShip = urlShip;
        this.urlLife = urlLife;
    }

    public String getLifeUrl() {
        return urlLife;
    }

    public String getShipUrl() {
        return urlShip;
    }


}
