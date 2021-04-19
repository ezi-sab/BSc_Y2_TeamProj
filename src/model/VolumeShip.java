package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class VolumeShip extends HBox {
	
	private final ImageView blueOrBlack;
	private final Image blackShipImage = new Image("view/resources/shipPicker/playerShip3_black.png");
	private final Image blueShipImage = new Image("view/resources/shipPicker/playerShip3_blue.png");

	public VolumeShip() {
		
		blueOrBlack = new ImageView("view/resources/shipPicker/playerShip3_blue.png");
		this.getChildren().add(blueOrBlack);
		
	}
	
	public void setVolume(boolean vol) {
		Image imageToSet;
		if(vol) {
			imageToSet = blueShipImage;
		} else {
			imageToSet =blackShipImage;
		}
		blueOrBlack.setImage(imageToSet);
		
	}
}
