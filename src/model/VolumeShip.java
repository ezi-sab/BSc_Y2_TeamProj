package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class VolumeShip extends HBox {
	
	private ImageView blueOrBlack;
	private Image blackShipImage = new Image("view/resources/shipPicker/playerShip3_black.png");
	private Image blueShipImage = new Image("view/resources/shipPicker/playerShip3_blue.png");
	
	private boolean volume; 
	
	public VolumeShip() {
		
		blueOrBlack = new ImageView("view/resources/shipPicker/playerShip3_blue.png");
		this.getChildren().add(blueOrBlack);
		
	}
	
	public void setVolume(boolean vol) {
		
		volume = vol;
		Image imageToSet = volume ? blueShipImage : blackShipImage;
		blueOrBlack.setImage(imageToSet);
		
	}
}
