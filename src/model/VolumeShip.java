package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class VolumeShip extends HBox {	
	
	private ImageView blueOrBlack;
	
	private Image blueShipImage = new Image(getClass().getResourceAsStream(Ship.blue.getShipUrl()), 90, 90, true, false);
	private Image blackShipImage = new Image(getClass().getResourceAsStream("/resources/Images/PlayerShip-Black-image.png"), 90, 90, true, false);
	
	/**
	 * Constructor for VolumeShip class used to set initial image.
	 */
	public VolumeShip() {
		
		blueOrBlack = new ImageView(blueShipImage);
		this.getChildren().add(blueOrBlack);
		
	}
	
	/**
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
