package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ShipPicker extends VBox {
	
	private ImageView circleImage;
	private ImageView shipImage;
	
	private String circleNotChosen = "/resources/Images/RadioButton-NotPressed-image.png";
	private String circleChosen = "/resources/Images/RadioButton-Pressed-image.png";
	
	private Ship ship;
	
	private boolean isCircleChosen;
	
	/**
	 * Constructor that selects a ship and sets the necessary image.
	 * That image is passed over the game scene as player's chosen ship.
	 */
	public ShipPicker(Ship ship) {
		
		circleImage = new ImageView(circleNotChosen);
		shipImage = new ImageView(ship.getShipUrl());
		this.ship = ship;
		isCircleChosen = false;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.getChildren().add(circleImage);
		this.getChildren().add(shipImage);
		
	}
	
	/**
	 * Gets the Ship selected.
	 * @return Ship selected ship is returned.
	 */
	public Ship getShip() {
		return ship;
	}
	
	/**
	 * Gets the boolean value for each selected Ship.
	 * @return boolean of is Ship selected.
	 */
	public boolean getIsCircleChosen() {
		return isCircleChosen;
	}
	
	/**
	 * Sets the Ship images selected if a Ship is chosen otherwise a Blank Ship is displayed.
	 * Enables to detect Ships from selected or unselected.
	 * Dark Ship image for unselected.
	 * A Bright Ship image for unselected.
	 * @param isCircleChosen from getIsCircleChosen() and set to selected otr unselected.
	 */
	public void setIsCircleChosen(boolean isCircleChosen) {
		this.isCircleChosen = isCircleChosen;
		String imageToSet = this.isCircleChosen ? circleChosen : circleNotChosen;
		circleImage.setImage(new Image(imageToSet));
	}
	
}
