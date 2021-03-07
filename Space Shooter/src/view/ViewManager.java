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

	private void createSubScene() {
		
		settingsSubScene = new menuSubScene();
		mainPane.getChildren().add(settingsSubScene);
		
		helpSubScene = new menuSubScene();
		mainPane.getChildren().add(helpSubScene);
		
		creditsSubScene = new menuSubScene();
		mainPane.getChildren().add(creditsSubScene);
		
		createShipSelectSubScene();
		
		
	}


	private void showSubScene(menuSubScene subScene) {
		if(sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		
		subScene.moveSubScene();
		sceneToHide= subScene;
	}



	private void createShipSelectSubScene() {
		shipSelectSubScene = new menuSubScene();
		mainPane.getChildren().add(shipSelectSubScene);
		
		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
		chooseShipLabel.setLayoutX(110);
		chooseShipLabel.setLayoutY(25);
		shipSelectSubScene.getPane().getChildren().add(chooseShipLabel);
		shipSelectSubScene.getPane().getChildren().add(createShipsToChoose());
		shipSelectSubScene.getPane().getChildren().add(createButtonToStart());
		
	}



	private buttons createButtonToStart() {
		buttons startButton = new buttons("START");
		startButton.setLayoutX(200);
		startButton.setLayoutY(300);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (chosenShip != null) {
					GameView gameManager = new GameView();
					try {
						gameManager.createNewGame(mainStage, chosenShip);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});
	
		return startButton;
	}



	private HBox createShipsToChoose() {
		HBox box = new HBox();
		box.setSpacing(20);
		shipsList = new ArrayList<>();
		for (Ship ship : Ship.values()) {
			ShipPicker shipToPick = new ShipPicker(ship);
			shipsList.add(shipToPick);
			box.getChildren().add(shipToPick);
			shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					for (ShipPicker ship:shipsList) {
						ship.setIsCircleChosen(false);
					}
					shipToPick.setIsCircleChosen(true);
					chosenShip = shipToPick.getShip();
				}
				
			});
		}
		box.setLayoutX(300-(118*2));
		box.setLayoutY(100);
		return box;
		
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

	MediaPlayer mediaPlayer;

	public void playSound(String soundPath) {
		String src = soundPath;
		Media tapped = new Media(Paths.get(src).toUri().toString());
		mediaPlayer = new MediaPlayer(tapped);
		mediaPlayer.setCycleCount(1);
		mediaPlayer.play();

	//		AudioClip tapped = new AudioClip(this.getClass().getResource("view/resources/fastinvader1.wav").toString());
	//		tapped.play();

	}



	private void createStartButton() {
		buttons startButton = new buttons("PLAY");
		addMenuButton(startButton);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
				showSubScene(shipSelectSubScene);
			}
			
		});
	}


	private void createHelpButton() {
		buttons helpButton = new buttons("HELP");
		addMenuButton(helpButton);
		
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
				showSubScene(helpSubScene);
			}
			
			
		});
	}

	private void createSettingsButton() {
		buttons settingsButton = new buttons("SETTINGS");
		addMenuButton(settingsButton);
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
				showSubScene(settingsSubScene);
			}
			
			
		});
	}

	private void createCreditsButton() {
		buttons creditsButton = new buttons("CREDITS");
		addMenuButton(creditsButton);
		
		creditsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
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
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
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
	





	

	



