package model;

import javafx.animation.TranslateTransition;

//import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

public class MenuSubScene extends SubScene {
	
	private final static String FONT_PATH = "src/model/resources/kenvector_future.ttf";
	private final static String BACKGROUND_IMG = "model/resources/blue_panel.png";

	private boolean isHidden;

	/**
	 * Creates a widget styled scene for menu sub scene.
	 * The scene is layout according to the screen.
	 * a background image is also set which is bluePanel.
	 */
	public MenuSubScene() {
		super(new AnchorPane(), 600, 400);
		prefWidth(600);
		prefHeight(450);
		
		BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMG, 600, 400, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		
		AnchorPane root2 = (AnchorPane) this.getRoot();
		
		root2.setBackground(new Background(image));
		
		isHidden = true;
		
		setLayoutX(1300);
		setLayoutY(260);
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * Transition effect while a menu sub scene is closed and opened.
	 * Plays accordingly when it's hidden or visible.
	 */
	public void moveSubScene() {
		TranslateTransition transition = new TranslateTransition();
		transition.setDuration(Duration.seconds(0.4));
		transition.setNode(this);
		
		if(isHidden) {
			transition.setToX(-775);
			isHidden = false;
		} 
		else {
			transition.setToX(0);
			isHidden = true;
		}
		
		transition.play();
	}

	/**
	 * Gets the AnchorPane that which is from root od subscene.
	 * @return AnchorPane that is set to root of this sub scene.
	 */
	public AnchorPane getPane() {
		return (AnchorPane) this.getRoot();
	}

}
