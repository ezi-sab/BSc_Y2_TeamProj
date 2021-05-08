package model;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public class InfoLabel extends Label{
	
	public final static String FONT_PATH = "src/model/resources/kenvector_future.ttf";
	
	public final static String BACKGROUND_IMG = "view/resources/blue_button13.png";

	/**
	 * Constructor that sets the Info label.
	 * @param text is passed as argument in setText().
	 */
	public InfoLabel(String text) {
		
		setPrefWidth(350);
		setPrefHeight(49);
		setText(text);
		setWrapText(true);
		setLabelFont();
		setAlignment(Pos.CENTER);
		
		BackgroundImage backgroundImage = new BackgroundImage(new Image(BACKGROUND_IMG, 350, 49, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null); 
		
		setBackground(new Background(backgroundImage));
		
	}

	/**
	 * Sets the font style of the label.
	 */
	private void setLabelFont() {
		try {
			setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 23));
		} catch (FileNotFoundException e) {
			setFont(Font.font("Verdana", 23));
		}
	}

}
