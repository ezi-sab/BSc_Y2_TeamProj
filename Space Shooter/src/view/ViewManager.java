package view;


import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.InfoLabel;
import model.Ship;
import model.ShipPicker;
import model.buttons;
import model.menuSubScene;

public class ViewManager {
	
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
	
	List<ShipPicker> shipsList;
	private Ship chosenShip;
	
	public ViewManager() {
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
		
		settingsSubScene = new menuSubScene();
		mainPane.getChildren().add(settingsSubScene);
		
		helpSubScene = new menuSubScene();
		mainPane.getChildren().add(helpSubScene);
		
		creditsSubScene = new menuSubScene();
		mainPane.getChildren().add(creditsSubScene);
		
		createShipSelectSubScene();
		
		
	}
