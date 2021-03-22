package model;


public enum Ship{
<<<<<<< HEAD

=======
	
>>>>>>> master
	/*blue("view/resources/shipPicker/playerShip3_blue.png"),
	green("view/resources/shipPicker/playerShip3_green.png"),
	orange("view/resources/shipPicker/playerShip3_orange.png"),
	red("view/resources/shipPicker/playerShip3_red.png"); */
<<<<<<< HEAD

    blue("/res/playerShip3_blue.png", "/res/playerShip3_blueD.png", "/res/playerShip3_blueL.png", "/res/playerShip3_blueR.png", "/res/playerLife3_blue.png"),
    green("/res/playerShip3_green.png", "/res/playerShip3_greenD.png", "/res/playerShip3_greenL.png", "/res/playerShip3_greenR.png", "/res/playerLife3_green.png"),
    orange("/res/playerShip3_orange.png", "/res/playerShip3_orangeD.png", "/res/playerShip3_orangeL.png", "/res/playerShip3_orangeR.png", "/res/playerLife3_orange.png"),
    red("/res/playerShip3_red.png", "/res/playerShip3_redD.png", "/res/playerShip3_redL.png", "/res/playerShip3_redR.png", "/res/playerLife3_red.png");

    private String urlUpShip;
    private String urlDownShip;
    private String urlLeftShip;
    private String urlRightShip;
    private String urlLife;

    private Ship(String urlUpShip, String urlDownShip, String urlLeftShip, String urlRightShip, String urlLife) {
        this.urlUpShip = urlUpShip;
        this.urlDownShip = urlDownShip;
        this.urlLeftShip = urlLeftShip;
        this.urlRightShip = urlRightShip;
        this.urlLife = urlLife;
    }

    public String getUpUrl() {
        return this.urlUpShip;
    }

    public String getDownUrl() {
        return urlDownShip;
    }

    public String getLeftUrl() {
        return urlLeftShip;
    }

    public String getRightUrl() {
        return urlRightShip;
    }

    public String getLifeUrl() {
        return urlLife;
    }
=======
	
	blue("/res/playerShip3_blue.png", "/res/playerShip3_blueD.png", "/res/playerShip3_blueL.png", "/res/playerShip3_blueR.png", "/res/playerLife3_blue.png"),
	green("/res/playerShip3_green.png", "/res/playerShip3_greenD.png", "/res/playerShip3_greenL.png", "/res/playerShip3_greenR.png", "/res/playerLife3_green.png"),
	orange("/res/playerShip3_orange.png", "/res/playerShip3_orangeD.png", "/res/playerShip3_orangeL.png", "/res/playerShip3_orangeR.png", "/res/playerLife3_orange.png"),
	red("/res/playerShip3_red.png", "/res/playerShip3_redD.png", "/res/playerShip3_redL.png", "/res/playerShip3_redR.png", "/res/playerLife3_red.png");
	
	private String urlUpShip;
	private String urlDownShip;
	private String urlLeftShip;
	private String urlRightShip;
	private String urlLife;
	
	private Ship(String urlUpShip, String urlDownShip, String urlLeftShip, String urlRightShip, String urlLife) {
		this.urlUpShip = urlUpShip;
		this.urlDownShip = urlDownShip;
		this.urlLeftShip = urlLeftShip;
		this.urlRightShip = urlRightShip;
		this.urlLife = urlLife;
	}
	
	public String getUpUrl() {
		return this.urlUpShip;
	}
	
	public String getDownUrl() {
		return urlDownShip;
	}
	
	public String getLeftUrl() {
		return urlLeftShip;
	}
	
	public String getRightUrl() {
		return urlRightShip;
	}
	
	public String getLifeUrl() {
		return urlLife;
	}
>>>>>>> master

}
