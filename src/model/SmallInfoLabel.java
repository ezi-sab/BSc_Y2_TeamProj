package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;

public class SmallInfoLabel extends Label {
	
	/**
	 * Constructor that sets the text , font and Alignment on the Game Scene.
	 * @param text is passed as argument to setText() for title.
	 */
	public SmallInfoLabel(String text) {
		
		setPrefWidth(130);
		setPrefHeight(50);
		BackgroundImage backgroundImg = new BackgroundImage(new Image("/resources/Images/Label-BackGround-image.png", 130, 50, false, true), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		setBackground(new Background(backgroundImg));
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(10,10,10,10));
		setText(text);
		
	}
	
}
