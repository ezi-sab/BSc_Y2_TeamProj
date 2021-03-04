package model;

import javafx.event.EventHandler;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class buttons extends Button {
	
	private final String FONT_PATH = "Space Shooter/src/model/resources/kenvector_future.ttf";
	private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/resources/blue_button04.png');";
    private final String BUTTON_UNPRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/resources/blue_button05.png');";

    public buttons(String title) {
    	setText(title);
    	setButtonFont();
    	setPrefWidth(190);
    	setPrefHeight(49);
    	setStyle(BUTTON_UNPRESSED_STYLE);
    	buttonListeners();
    	
    }
