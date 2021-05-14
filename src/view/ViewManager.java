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
import model.Buttons;
import model.MenuSubScene;

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
	
	private MenuSubScene shipSelectSubScene;
	private static MenuSubScene scoreSubScene;
	private MenuSubScene settingsSubScene;
	private MenuSubScene helpSubScene;
	private MenuSubScene creditsSubScene;
	private MenuSubScene musicControls;
	private MenuSubScene sceneToHide;
	
	private static SoundManager soundManager = new SoundManager();
	private static ScoreBoard scoreBoard = new ScoreBoard();
	
	List<Buttons> menuButtons;
	List<ShipPicker> shipsList;
	
	private static Ship chosenShip = null;
	
	/**
	 * Constructor for building the Main scene of the game.
	 */
	public ViewManager() {
		
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, width, height);
		mainStage.setScene(mainScene);
		mainStage.setMaxWidth(width);
		mainStage.setMaxHeight(height);
		mainStage.setTitle("Star Shooter");
		soundManager.playBackGroundMusic();
		createSubScene();
		createButtons();
		createBackground();
		createLogo();
		
	}
	
	/**
	 * Shows the selected sub scene.
	 * @param subScene to show or hide
	 */
	private void showSubScene(MenuSubScene subScene) {
		
		if(sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		
		subScene.moveSubScene();
		sceneToHide= subScene;
		
	}
	
	/**
	 * Creates and calls the sub scenes methods.
	 */
	private void createSubScene() {
		
		settingsSubScene = new MenuSubScene();
		mainPane.getChildren().add(settingsSubScene);
		
		createShipSelectSubScene();
		createScoreSubScene();
		createCreditsSubScene();
		createHelpSubScene();
		createMusicButtons();
		
	}
	
	/**
	 * Creates Ship select sub scene.
	 * Player can choose desired ship as their character.
	 * Necessary Font, layout and labels are set.
	 */
	private void createShipSelectSubScene() {
		
		shipSelectSubScene = new MenuSubScene();
		mainPane.getChildren().add(shipSelectSubScene);
		
		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
		chooseShipLabel.setLayoutX(120);
		chooseShipLabel.setLayoutY(20);
		shipSelectSubScene.getPane().getChildren().add(chooseShipLabel);
		shipSelectSubScene.getPane().getChildren().add(createShipsToChoose());
		shipSelectSubScene.getPane().getChildren().add(retrievePlayerName());
		shipSelectSubScene.getPane().getChildren().add(createButtonToStart());
		
	}
	
	/**
	 * Creates the Score sub scene.
	 * Player can view their scores.
	 * Necessary Font, layout and labels are set.
	 */
	private void createScoreSubScene() {
		
		scoreSubScene = new MenuSubScene();
		mainPane.getChildren().add(scoreSubScene);
		InfoLabel score = new InfoLabel("SCORES");
		score.setLayoutX(115);
		score.setLayoutY(20);
		
		scoreBoard.setScoreVBox();
		
		scoreSubScene.getPane().getChildren().add(score);
		scoreSubScene.getPane().getChildren().add(scoreBoard.getScoreVBox());
		
	}
	
	/**
	 * Creates the Credits sub scene.
	 * Contribution towards the game as sections.
	 * Necessary Font, layout and labels are set.
	 */
	private void createCreditsSubScene() {
		
		creditsSubScene = new MenuSubScene();
		mainPane.getChildren().add(creditsSubScene);
		
		InfoLabel creditsLabel = new InfoLabel("CREDITS");
		creditsLabel.setLayoutX(120);
		creditsLabel.setLayoutY(20);
		
		Label credit0 = new Label("Reuben Sidhu: UI/Game Logic");
		Label credit1 = new Label("Bharath Raj: UI/Game Logic/Level Design/Sound");
		Label credit2 = new Label("Eunji Kwak: Artificial Intelligence");
		Label credit3 = new Label("Iniyan Kanmani: Sound Design");
		Label credit4 = new Label("Alfred: UI");
		Label credit5 = new Label("Matthew: ");
		Label credit6 = new Label("Xiaoliang Pu: Shooting Mechanism");
		
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
	
	/**
	 * Creates the Help sub scene.
	 * Necessary guide for the player to get started.
	 * Font, layout and labels are set.
	 */
	private void createHelpSubScene() {
		
		helpSubScene = new MenuSubScene();
		mainPane.getChildren().add(helpSubScene);
		InfoLabel help = new InfoLabel("HELP");
		help.setLayoutX(120);
		help.setLayoutY(20);
		GridPane helpGrid = new GridPane();
		helpGrid.setLayoutX(80);
		helpGrid.setLayoutY(90);
		helpGrid.setHgap(20);
		helpGrid.setVgap(20);
		
		ImageView playerShip = new ImageView(new Image("/resources/Images/PlayerShip-Red-image.png", 80, 80, true, false));
		ImageView enemyShip = new ImageView(new Image("/resources/Images/EnemyShip-1-image.png", 80, 80, true, false));
		ImageView laserPowerUp = new ImageView(new Image("/resources/Images/PowerUp-Laser-image.png", 40, 40, true, false));
		ImageView lifePowerUp = new ImageView(new Image("/resources/Images/PowerUp-Life-image.png", 40, 40, true, false));
		
		Label playerShipHelp = new Label("This is your ship. Choose colour from the \nPlay menu. Control it with arrow keys or W/S/A/D keys.");
		Label enemyShipHelp = new Label("These are enemy ships.\nAvoid them!");
		Label laserPowerUpHelp = new Label("The laser power-ups gives you the ability to shoot enemies,\nby picking up the darts/bullets!");
		Label lifePowerUpHelp = new Label("This is extra life.\nGrab it to gain an extra ship\nif you have less than three ships.");
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				enemyShip.setRotate(90+now/10000000l);
				playerShip.setRotate(-now/10000000l);
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
		
		helpGrid.add(playerShip, 0, 0);
		helpGrid.add(playerShipHelp, 1, 0);
		helpGrid.add(enemyShip, 0, 1);
		helpGrid.add(enemyShipHelp, 1, 1);
		helpGrid.add(laserPowerUp, 0, 2);
		helpGrid.add(laserPowerUpHelp, 1, 2);
		helpGrid.add(lifePowerUp, 0, 3);
		helpGrid.add(lifePowerUpHelp, 1, 3);
		helpSubScene.getPane().getChildren().addAll(help, helpGrid);
		
	}
	
	/**
	 * Creates a HBox for choosing between ships for the player to pick.
	 * Necessary Font, layout and labels are set.
	 */
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
			name.setFont(Font.loadFont(new FileInputStream("src/resources/Fonts/Kenvector-Future-font.ttf"), 23));
		} catch (FileNotFoundException e) {
			name.setFont(Font.font("Verdana", 23));
		}
    	name.setStyle("-fx-background-color: transparent; -fx-background-image: url('/resources/Images/Button-NotPressed-Blue-image.png');");
		return name;
	}
	
	/**
	 * Implements the start button.
	 * Calls the game stub and starts everything from cold .
	 * Checks if Player hasn't chosen a ship and a Text for the ship isn't entered.
	 * Necessary Font, layout and labels are set.
	 */
	private Buttons createButtonToStart() {
		
		Buttons startButton = new Buttons("START");
		startButton.setLayoutX(300-(118*2) + 265);
		startButton.setLayoutY(300);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				if ((chosenShip != null) && !(name.getText().isBlank())) {
					
					soundManager.setStopTimer(true);
					if (soundManager.getBGMVolumeBeforeReached() == true) {
						soundManager.setBGMVolumeBeforeGame(soundManager.getBackGroundMusicVolume());
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
	
	/**
	 * Ability to control game sound through this menu.
	 * Image ships decides the game volume.
	 * A black ship says deactivated and a bright ship says it's activated.
	 * Necessary Font, layout and labels are set.
	 */
	private void createMusicButtons() {
		
		musicControls = new MenuSubScene();
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
	
	/**
	 * Gets the main stage of the game.
	 * @return mainStage
	 */
	public Stage getMainStage() {
		
		return mainStage;
		
	}
	
	/**
	 * Sets the main stage to the main scene.
	 */
	public void setToMainScene() {
		
		mainStage.setScene(mainScene);
		if (soundManager.getBackGroundMusicVolume() == 0 && soundManager.getBGMVolumeBeforeGame() != 0) {
			soundManager.bgmFadeIn();			
		} else {
			soundManager.setBgmVolumeShips();
		}
		soundManager.setIgmVolumeShips();
		chosenShip = null;
		
	}
	
	/**
	 * Creates a menu button template.
	 */
	private void addMenuButton(Buttons button) {
		
		button.setLayoutX(MENU_BUTTON_STARTX);
		button.setLayoutY(MENU_BUTTON_STARTY + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
		
	}
	
	/**
	 * Function that calls all the buttons for the scene.
	 * Displays every button on th main stage and scene.
	 */
	private void createButtons() {
		
		createStartButton();
		createScoresButton();
		createHelpButton();
		createSettingsButton();
		createCreditsButton();
		createExitButton();
		
	}
	
	/**
	 * Creates a button to start the game on the main stage.
	 * This redirects player to the shipSelectSubScene().
	 * Thereafter start button in sub scene enables the game .
	 */
	private void createStartButton() {
		
		Buttons startButton = new Buttons("PLAY");
		addMenuButton(startButton);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(shipSelectSubScene);
			}
			
		});
		
	}
	
	/**
	 * Creates a scores button on the main stage.
	 */
	private void createScoresButton() {
		
		Buttons scoresButton = new Buttons("SCORES");
		addMenuButton(scoresButton);
		scoresButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(scoreSubScene);
			}
			
		});
		
	}
	
	/**
	 * Creates a settings button on the main stage.
	 */
	private void createSettingsButton() {
		
		Buttons settingsButton = new Buttons("SETTINGS");
		addMenuButton(settingsButton);
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(musicControls);
			}
			
			
		});
		
	}
	
	/**
	 * Creates a help button on the main stage..
	 */
	private void createHelpButton() {
		
		Buttons helpButton = new Buttons("HELP");
		addMenuButton(helpButton);
		
		helpButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(helpSubScene);
			}
			
			
		});
		
	}
	
	/**
	 * Creates a credits button on the main stage.
	 */
	private void createCreditsButton() {
		
		Buttons creditsButton = new Buttons("CREDITS");
		addMenuButton(creditsButton);
		
		creditsButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				soundManager.playMenuOpenMusic();
				showSubScene(creditsSubScene);
			}
			
			
		});
		
	}
	
	/**
	 * Creates a exit button on the main stage.
	 * On tapped the game exits and game window closes.
	 * This is the end of the game.
	 */
	private void createExitButton() {
		
		Buttons exitButton = new Buttons("EXIT");
		addMenuButton(exitButton);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mainStage.close();				
			}
			
		});
		
	}
	
	/**
	 * Gets the chosen ship by the player.
	 */
	public Ship getChosenShip() {
		
		return chosenShip;
		
	}
	
	/**
	 * Gets the player name.
	 */
	public String getPlayerName() {
		
		return playerName;
		
	}
	
	/**
	 * Creates the background for main stage.
	 * Adds a Space Theme image.
	 */
	private void createBackground() {
		
		Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/Images/Space-BackGround-image.png"), 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
		
	}
	
	/**
	 * Creates and adds the logo of the game "STAR SHOOTER".
	 * Font Style, Layout are specified accordingly.
	 */
	private void createLogo() {
		
		ImageView logo = new ImageView("/resources/Images/Title-StarShooter-image.png");
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
