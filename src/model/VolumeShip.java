package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class VolumeShip extends HBox {
	
	
	private final ImageView blueOrBlack;
	
	private final Image blueShipImage = new Image("view/resources/shipPicker/playerShip3_blue.png", 90, 90, true, false);
	private final Image blackShipImage = new Image("view/resources/shipPicker/playerShip3_black.png", 90, 90, true, false);

	/*
	 * Constructor for VolumeShip class used to set initial image.
	 */
	public VolumeShip() {
		
		blueOrBlack = new ImageView(blueShipImage);
		this.getChildren().add(blueOrBlack);
		
	}
	
	/*
	 * Decides the Image with reference to the parameter and sets it to the VolumShip.
	 * 
	 * @param vol  value used to decide and set VolumeShip's Image.
	 */
	public void setVolumeShip(boolean vol) {
		
		Image imageToSet;
		
		if(vol) {
			imageToSet = blueShipImage;
		} else {
			imageToSet = blackShipImage;
		}
		
		blueOrBlack.setImage(imageToSet);
		
	}
	
	
}
