package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.InfoLabel;
import model.ScoreBoard;
import model.Ship;
import model.ShipPicker;
import model.buttons;
import model.menuSubScene;

public class ViewManager {
	
	
	private static final int width = 1258;
	private static final int height = 814;
	private static AnchorPane mainPane;
	private static Scene mainScene;
	private static Stage mainStage = new Stage();
	
	private final static int MENU_BUTTON_STARTX = 100;
	private final static int MENU_BUTTON_STARTY = 185;
	private static TextField name;
	private static String playerName = "";
	
	private menuSubScene shipSelectSubScene;
	private static menuSubScene scoreSubScene;
	private menuSubScene settingsSubScene;
	private menuSubScene helpSubScene;
	private menuSubScene creditsSubScene;
	private menuSubScene musicControls;
	private menuSubScene sceneToHide;
	
	private static SoundManager soundManager = new SoundManager();
	private static ScoreBoard scoreBoard = new ScoreBoard();

	List<buttons> menuButtons;
	List<ShipPicker> shipsList;
	
	private static Ship chosenShip;
	
	
	public ViewManager() {
		
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, width, height);
		mainStage.setScene(mainScene);
		mainStage.setTitle("Star Shooter");
		soundManager.playBackGroundMusic();
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

		
		createShipSelectSubScene();
		
		createScoreSubScene();
		
		createCreditsSubScene();
		
		createHelpSubScene();
		
		createMusicButtons();
		
	}
	
	
	private void createShipSelectSubScene() {
		
		shipSelectSubScene = new menuSubScene();
		mainPane.getChildren().add(shipSelectSubScene);
		
		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
		chooseShipLabel.setLayoutX(120);
		chooseShipLabel.setLayoutY(20);
		shipSelectSubScene.getPane().getChildren().add(chooseShipLabel);
		shipSelectSubScene.getPane().getChildren().add(createShipsToChoose());
		shipSelectSubScene.getPane().getChildren().add(retrievePlayerName());
		shipSelectSubScene.getPane().getChildren().add(createButtonToStart());
		
	}
	
	
	private void createScoreSubScene() {
		
		scoreSubScene = new menuSubScene();
		mainPane.getChildren().add(scoreSubScene);
		InfoLabel score = new InfoLabel("SCORES");
		score.setLayoutX(115);
		score.setLayoutY(20);
		
		scoreBoard.setScoreVBox();
		
		scoreSubScene.getPane().getChildren().add(score);
		scoreSubScene.getPane().getChildren().add(scoreBoard.getScoreVBox());
		
	}
	
	
	private void createCreditsSubScene() {
		
		creditsSubScene = new menuSubScene();
		mainPane.getChildren().add(creditsSubScene);
		
		InfoLabel creditsLabel = new InfoLabel("CREDITS");
		creditsLabel.setLayoutX(120);
		creditsLabel.setLayoutY(20);
		
		Label credit0 = new Label("Reuben Sidhu: UI/Game Logic");
		Label credit1 = new Label("Bharath Raj: UI/Level Design/Sound");
		Label credit2 = new Label("Eunji Kwak: Artificial Intelligence");
		Label credit3 = new Label("Iniyan Kanmani: Sound Design");
		Label credit4 = new Label("Alfred: UI/ Optimisation");
		Label credit5 = new Label("Matthew: ");
		Label credit6 = new Label("Xiaoliang Pu: ");
		
		credit0.setFont(new Font("Arial", 20));
		credit1.setFont(new Font("Arial", 20));
		credit2.setFont(new Font("Arial", 20));
		credit3.setFont(new Font("Arial", 20));
		credit4.setFont(new Font("Arial", 20));
		credit5.setFont(new Font("Arial", 20));
		credit6.setFont(new Font("Arial", 20));
		
		VBox creditsBox = new VBox(20, credit0, credit1, credit2, credit3, credit4, credit5, credit6);
		
		creditsBox.setLayoutX(50);
		creditsBox.setLayoutY(80);
		creditsSubScene.getPane().getChildren().addAll(creditsLabel, creditsBox);				
				
	}
	
	
	private void createHelpSubScene() {
		
		helpSubScene = new menuSubScene();
		mainPane.getChildren().add(helpSubScene);
		InfoLabel help = new InfoLabel("HELP");
		help.setLayoutX(120);
		help.setLayoutY(20);
		GridPane helpGrid = new GridPane();
		helpGrid.setLayoutX(80);
		helpGrid.setLayoutY(90);
		helpGrid.setHgap(20);
		helpGrid.setVgap(20);
		
		ImageView ship = new ImageView(new Image("/res/playerShip3_red.png", 80, 80, true, false));
		ImageView meteor1 = new ImageView(); //meteor2 = new ImageView();
		ImageView star = new ImageView(new Image("/res/playerLife3_red.png", 20, 20, true, false));
		ImageView life = new ImageView(new Image("/res/playerLife3_red.png", 20, 20, true, false));
		
		meteor1.setImage(new Image("/res/spaceShips_004.png", 80, 80, true, false));
		
		Label shipHelp 	 = new Label("This is your ship. Choose colour from the \nPlay menu. Control it with arrow keys or W/S/A/D keys.");
		Label meteorHelp = new Label("These are enemy ships.\nAvoid them!");
		Label starHelp   = new Label("The coins give you points,\nIF you can grab them!");
		Label lifeHelp   = new Label("This is extra life.\nGrab it to gain an extra ship\nif you have less than three ships.");
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				meteor1.setRotate(90+now/10000000l);
				//meteor2.setRotate(180+now/10000000l);
				ship.setRotate(-now/10000000l);
			}
		};
		timer.start();

		/* gridpane:
		 * ___0_|__1_|__2_|_3_
		 * 0|___|____|____|__
		 * 1|___|____|____|__
		 * 2|___|____|____|__
		 * 3|___|____|____|___
		 */
		
		helpGrid.add(ship, 0, 0);
		helpGrid.add(shipHelp, 1, 0);
		helpGrid.add(meteor1, 0, 1);
		//helpGrid.add(meteor2, 2, 1);
		helpGrid.add(meteorHelp, 1, 1);
		helpGrid.add(life, 0, 2);
		helpGrid.add(lifeHelp, 1, 2);
		helpGrid.add(star, 0, 3);
		helpGrid.add(starHelp, 1, 3);
		helpSubScene.getPane().getChildren().addAll(help, helpGrid);
		
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
	
	private TextField retrievePlayerName() {
		name = new TextField("");
		name.setPromptText("SHIP NAME");
		name.setText(playerName);
		name.setLayoutX(300-(118*2));
		name.setLayoutY(300);
		name.setPrefWidth(190);
    	name.setPrefHeight(49);
    	try {
			name.setFont(Font.loadFont(new FileInputStream("src/model/resources/kenvector_future.ttf"), 23));
		} catch (FileNotFoundException e) {
			name.setFont(Font.font("Verdana", 23));
		}
    	name.setStyle("-fx-background-color: transparent; -fx-background-image: url('/model/resources/blue_button05.png');");
		return name;
	}
	
	
	private buttons createButtonToStart() {
		
		buttons startButton = new buttons("START");
		startButton.setLayoutX(300-(118*2) + 265);
		startButton.setLayoutY(300);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if ((chosenShip != null) && !(name.getText().isBlank())) {
					
					soundManager.setStopTimer(true);
					if (soundManager.getBGMVolumeBeforeReached() == true) {
						soundManager.setBGMVolumeBeforeGame(soundManager.getbackGroundMusicVolume());
						soundManager.setBGMVolumeBeforeReached(false);
					}
					
					soundManager.setBackGroundMusicVolume(0);
					soundManager.setBgmVolumeShips();
					
					name.replaceText(0, name.getText().length(), name.getText().toUpperCase());
					playerName = name.getText();
					
					GameView gameViewManager = new GameView();
					gameViewManager.countDownTimer(chosenShip, 1000);
					
				}
				
			}
			
		});
	
		return startButton;
		
	}
	
	
	private void createMusicButtons() {
		
		musicControls = new menuSubScene();
		mainPane.getChildren().add(musicControls);

		InfoLabel chooseBGMusicOption = new InfoLabel("BACKGROUND MUSIC");
		chooseBGMusicOption.setLayoutX(125);
		chooseBGMusicOption.setLayoutY(25);
		musicControls.getPane().getChildren().add(chooseBGMusicOption);
		musicControls.getPane().getChildren().add(soundManager.bgmVolumeShips());
		
		InfoLabel chooseIGMusicOption = new InfoLabel("IN-GAME MUSIC");
		chooseIGMusicOption.setLayoutX(125);
		chooseIGMusicOption.setLayoutY(200);
		musicControls.getPane().getChildren().add(chooseIGMusicOption);
		musicControls.getPane().getChildren().add(soundManager.igmVolumeShips());
		
	}
	
	
	public Stage getMainStage() {
		
		return mainStage;
		
	}
	
	
	public void setToMainScene() {
		
		mainStage.setScene(mainScene);
		
	}
	
	
	private void addMenuButton(buttons button) {
		
		button.setLayoutX(MENU_BUTTON_STARTX);
		button.setLayoutY(MENU_BUTTON_STARTY + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
		
	}
	

	private void createButtons() {
		
		createStartButton();
		createScoresButton();
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
				soundManager.playMenuOpenMusic();
				showSubScene(shipSelectSubScene);
			}
			
		});
		
	}
	
	
	private void createScoresButton() {
		
		buttons scoresButton = new buttons("SCORES");
		addMenuButton(scoresButton);
		scoresButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(scoreSubScene);
			}
			
		});
		
	}
	
	
	private void createSettingsButton() {
		
		buttons settingsButton = new buttons("SETTINGS");
		addMenuButton(settingsButton);
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(musicControls);
			}
			
			
		});
		
	}
	
	
	private void createHelpButton() {
		
		buttons helpButton = new buttons("HELP");
		addMenuButton(helpButton);
		
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
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
				soundManager.playMenuOpenMusic();
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
	
	
	public Ship getChosenShip() {
		
		return chosenShip;
		
	}
	
	public String getPlayerName() {
		
		return playerName;
		
	}
	
	
	private void createBackground() {
		
		Image backgroundImage = new Image("view/resources/space.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
		
	}
	
	
	private void createLogo() {
		
		ImageView logo = new ImageView("view/resources/StarShooter.png");
		logo.setLayoutX(255);
		logo.setLayoutY(35);
		
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
