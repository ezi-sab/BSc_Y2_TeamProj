package view;

import model.buttons;
import model.menuSubScene;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.buttons;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

public class viewManager {
	
	private static final int width = 1024;
	private static final int height = 768;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private final static int MENU_BUTTON_STARTX = 100;
	private final static int MENU_BUTTON_STARTY = 220;
	
	private menuSubScene shipSelectSubScene;
	private menuSubScene settingsSubScene;
	private menuSubScene helpSubScene;
	private menuSubScene creditsSubScene;
	
	private menuSubScene sceneToHide;

	List<buttons> menuButtons;
	
	public viewManager() {
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, width, height);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		createSubScene();
		createButtons();
		createBackground();
		createLogo();
		
		
	}
	
	private void showSubScene(menuSubScene subScene) {
		if(sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		
		subScene.moveSubScene();
		sceneToHide= subScene;
	}
	
	private void createSubScene() {
		
		shipSelectSubScene = new menuSubScene();
		mainPane.getChildren().add(shipSelectSubScene);
		
		settingsSubScene = new menuSubScene();
		mainPane.getChildren().add(settingsSubScene);
		
		helpSubScene = new menuSubScene();
		mainPane.getChildren().add(helpSubScene);
		
		creditsSubScene = new menuSubScene();
		mainPane.getChildren().add(creditsSubScene);
		
		
	}
	
	public Stage getMainStage() {
		return mainStage;
	}
	
	private void addMenuButton(buttons button) {
		button.setLayoutX(MENU_BUTTON_STARTX);
		button.setLayoutY(MENU_BUTTON_STARTY + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}

	private void createButtons() {
		createStartButton();
		createHelpButton();
		createSettingsButton();
		createCreditsButton();
		createExitButton();
	}
	
	private void createStartButton() {
		buttons startButton = new buttons("PLAY");
		addMenuButton(startButton);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(shipSelectSubScene);
			}
			
		});
	}
	
	private void createSettingsButton() {
		buttons settingsButton = new buttons("SETTINGS");
		addMenuButton(settingsButton);
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(settingsSubScene);
			}
			
			
		});
	}
	
	private void createHelpButton() {
		buttons helpButton = new buttons("HELP");
		addMenuButton(helpButton);
		
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(helpSubScene);
			}
			
			
		});
	}
	
	private void createCreditsButton() {
		buttons creditsButton = new buttons("CREDITS");
		addMenuButton(creditsButton);
		
		creditsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(creditsSubScene);
			}
			
			
		});
	}
	
	private void createExitButton() {
		buttons exitButton = new buttons("EXIT");
		addMenuButton(exitButton);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				mainStage.close();				
			}
			
		});
	}
	
	private void createBackground() {
		Image backgroundImage = new Image("view/resources/space.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	
	private void createLogo() {
		ImageView logo = new ImageView("view/resources/StarShooter.png");
		logo.setLayoutX(138);
		logo.setLayoutY(50);
		
		logo.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(new DropShadow());
			}
		});
		
		logo.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(null);
			}
				
		});
		
		mainPane.getChildren().add(logo);
	}
	
	
}
