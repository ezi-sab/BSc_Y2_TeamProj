package view;

import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;

<<<<<<< HEAD
=======
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.HostServices;
>>>>>>> master
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
<<<<<<< HEAD
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
=======
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
>>>>>>> master
import javafx.stage.Stage;
import model.InfoLabel;
import model.Ship;
import model.ShipPicker;
import model.buttons;
import model.menuSubScene;

public class ViewManager {
<<<<<<< HEAD

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

        //shipSelectSubScene = new menuSubScene();
        //mainPane.getChildren().add(shipSelectSubScene);

        settingsSubScene = new menuSubScene();
        mainPane.getChildren().add(settingsSubScene);

        helpSubScene = new menuSubScene();
        mainPane.getChildren().add(helpSubScene);

        creditsSubScene = new menuSubScene();
        mainPane.getChildren().add(creditsSubScene);

        createShipSelectSubScene();


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

    private buttons createButtonToStart() {
        buttons startButton = new buttons("START");
        startButton.setLayoutX(200);
        startButton.setLayoutY(300);

        startButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (chosenShip != null) {
                    GameView gameManager = new GameView();
                    gameManager.setShipImage(chosenShip);
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
                playSound("src/view/resources/sounds/fastinvader1.mp3");
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
                playSound("src/view/resources/sounds/fastinvader1.mp3");
                showSubScene(settingsSubScene);
            }
=======
	
	private static final int width = 1024;
	private static final int height = 768;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private final static int MENU_BUTTON_STARTX = 100;
	private final static int MENU_BUTTON_STARTY = 185;
	
	private menuSubScene shipSelectSubScene;
	private menuSubScene scoreSubScene;
	private menuSubScene settingsSubScene;
	private menuSubScene helpSubScene;
	private menuSubScene creditsSubScene;
	private menuSubScene musicControls;
	
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
		
		//shipSelectSubScene = new menuSubScene();
		//mainPane.getChildren().add(shipSelectSubScene);
		
		//scoreSubScene = new menuSubScene();
		//mainPane.getChildren().add(scoreSubScene);
		
		settingsSubScene = new menuSubScene();
		mainPane.getChildren().add(settingsSubScene);
		
		//helpSubScene = new menuSubScene();
		//mainPane.getChildren().add(helpSubScene);
		
		//creditsSubScene = new menuSubScene();
		//mainPane.getChildren().add(creditsSubScene);
		
		createShipSelectSubScene();
		
		createScoreSubScene();
		
		createCreditsSubScene();
		
		createHelpSubScene();
		
		createToggleButtons();
		
		
	}
	
	private void createShipSelectSubScene() {
		shipSelectSubScene = new menuSubScene();
		mainPane.getChildren().add(shipSelectSubScene);
		
		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
		chooseShipLabel.setLayoutX(120);
		chooseShipLabel.setLayoutY(20);
		shipSelectSubScene.getPane().getChildren().add(chooseShipLabel);
		shipSelectSubScene.getPane().getChildren().add(createShipsToChoose());
		shipSelectSubScene.getPane().getChildren().add(createButtonToStart());
		
	}
	
	private void createScoreSubScene() {
		scoreSubScene = new menuSubScene();
		mainPane.getChildren().add(scoreSubScene);
		InfoLabel score = new InfoLabel("SCORES");
		score.setLayoutX(115);
		score.setLayoutY(20);
		VBox scoreContainer = new VBox();
		scoreContainer.setLayoutX(150);
		scoreContainer.setLayoutY(150);
		
		Label scoreHeading = new Label("Name			Score ");
		scoreHeading.setUnderline(true);
		Label score1 = new Label("Ship 1		           100");
		Label score2 = new Label("Ship 2		           100");
		Label score3 = new Label("Ship 3		           100");
		scoreHeading.setFont(Font.font("Verdana",20));
		score1.setFont(Font.font("Verdana",20));
		score2.setFont(Font.font("Verdana",20));
		score3.setFont(Font.font("Verdana",20));
		scoreContainer.setBackground(new Background(new BackgroundFill(Color.MEDIUMAQUAMARINE, new CornerRadii(20), new Insets(-20,-20,-20,-20))));
		scoreContainer.getChildren().addAll(scoreHeading, score1, score2, score3);
		
		scoreSubScene.getPane().getChildren().addAll(score, scoreContainer);//, score1, score2, score3);		
		
	}
	
	private void createCreditsSubScene() {
		creditsSubScene = new menuSubScene();
		mainPane.getChildren().add(creditsSubScene);
		
		InfoLabel creditsLabel = new InfoLabel("CREDITS");
		creditsLabel.setLayoutX(120);
		creditsLabel.setLayoutY(20);
		
		Label credit0 = new Label("Reuben Sidhu: UI/Game Logic");
		Label credit1 = new Label("Bharath Raj: UI/Level Design");
		Label credit2 = new Label("Eunji Kwak: Artificial Intelligence");
		Label credit3 = new Label("Iniyan: Sound Design");
		Label credit4 = new Label("Alfred: ");
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
		//meteor2.setImage(new Image("/res/playerShip3_red.png", 80, 80, true, false));
		
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
	
	private buttons createButtonToStart() {
		buttons startButton = new buttons("START");
		startButton.setLayoutX(200);
		startButton.setLayoutY(300);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (chosenShip != null) {
					GameView gameManager = new GameView();
					gameManager.setShipImage(chosenShip);
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
	
	private void createToggleButtons() {
		musicControls = new menuSubScene();
		mainPane.getChildren().add(musicControls);

		InfoLabel chooseMusicOption = new InfoLabel("MUSIC");
		chooseMusicOption.setLayoutX(110);
		chooseMusicOption.setLayoutY(25);
		musicControls.getPane().getChildren().add(chooseMusicOption);
		musicControls.getPane().getChildren().add(musicToggleButton());

	}

	private VBox musicToggleButton() {
		VBox box = new VBox();
		box.setSpacing(40);
		Button musicOn, musicOff;

		musicOn = new Button("ON");
		musicOn.setLayoutX(100);
		musicOn.setLayoutY(220);
		musicOff = new Button("OFF");
		musicOff.setLayoutX(100);
		musicOff.setLayoutY(220);

		box.getChildren().addAll(musicOn,musicOff);

		MediaPlayer mediaPlayer;

		String src = "Space Shooter/src/view/resources/sounds/spaceinvaders1.mp3";
		Media tapped = new Media(Paths.get(src).toUri().toString());
		mediaPlayer = new MediaPlayer(tapped);

		musicOn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mediaPlayer.play();
				mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				//TODO : If the background sound is trimmed the above line parametrised with "Mediaplayer.INFINITY / 1".
			}
		});

		musicOff.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

				mediaPlayer.stop();
				//TODO : If the background sound is trimmed the above line parametrised with "Mediaplayer.INFINITY / 1".

			}
		});


//		VBox box = new VBox();
//		box.setSpacing(40);
//		ToggleButton toggleButton1 = new ToggleButton("ON");
//		ToggleButton toggleButton2 = new ToggleButton("OFF");

//		ToggleGroup toggleGroup = new ToggleGroup();
//
//		toggleButton1.setToggleGroup(toggleGroup);
//		toggleButton2.setToggleGroup(toggleGroup);

//		box.getChildren().addAll(toggleButton1,toggleButton2);
//
//
//			toggleButton1.setSelected(true);
//			toggleButton2.setSelected(false);
//
//		isOnSelected = toggleButton1.isSelected();
//		isOffSelected = toggleButton2.isSelected();

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
		createScoresButton();
		createHelpButton();
		createSettingsButton();
		createCreditsButton();
		createExitButton();
	}
	
	public void playButtonSound(String soundPath) {

		MediaPlayer mediaPlayer;

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
	
	private void createScoresButton() {
		buttons scoresButton = new buttons("SCORES");
		addMenuButton(scoresButton);
		scoresButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
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
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
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
				playSound("Space Shooter/src/view/resources/sounds/fastinvader1.mp3");
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
	
	MediaPlayer mediaPlayer;

	public void playSound(String soundPath) {
		String src = soundPath;
		Media tapped = new Media(Paths.get(src).toUri().toString());
		mediaPlayer = new MediaPlayer(tapped);
		mediaPlayer.setCycleCount(1);
		mediaPlayer.play();
>>>>>>> master

	//		AudioClip tapped = new AudioClip(this.getClass().getResource("view/resources/fastinvader1.wav").toString());
	//		tapped.play();

<<<<<<< HEAD
        });
    }

    private void createHelpButton() {
        buttons helpButton = new buttons("HELP");
        addMenuButton(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
=======
	} 

	
	
}
	
>>>>>>> master

            @Override
            public void handle(ActionEvent event) {
                playSound("src/view/resources/sounds/fastinvader1.mp3");
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
                playSound("src/view/resources/sounds/fastinvader1.mp3");
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
                playSound("src/view/resources/sounds/fastinvader1.mp3");
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



}
