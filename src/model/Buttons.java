package model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Buttons extends Button {
	
	private final String FONT_PATH = "src/resources/Fonts/Kenvector-Future-font.ttf";
	private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/Images/Button-Pressed-Blue-image.png');";
    private final String BUTTON_UNPRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/Images/Button-NotPressed-Blue-image.png');";
    
    /**
     * Constructor sets the style of Button.
     * @param title is passed as argument in setText()
     */
    public Buttons(String title) {
    	
    	setText(title);
    	setButtonFont();
    	setPrefWidth(190);
    	setPrefHeight(49);
    	setStyle(BUTTON_UNPRESSED_STYLE);
    	buttonListeners();
    	
    }
    
    /**
     * Function sets the Button Font.
     */
	private void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
    }
	
    /**
     * Function sets the style when button pressed.
     */
    private void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }
    
    /**
     * Function sets the style when button released.
     */
    private void setButtonReleasedStyle() {
        setStyle(BUTTON_UNPRESSED_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }
    
    /**
     * Triggers the button presses and calls the Button styles.
     */
    private void buttonListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }
            }
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }
            }
        });
        
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow());
            }
        });
        
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
                
            }
        });
        
    }
    
}
