package view;

import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.VolumeShip;

public class SoundManager {
	
	private static Media bgm = new Media(Paths.get("src/resources/Sounds/Spaceinvaders-sound.mp3").toUri().toString());
	private Media menuMusic;
	private Media countDown;
	private Media coinPickUp;
	private Media laserPickUp;
	private Media lifePickUp;
	private Media laserEmpty;
	private Media laserShoot;
	private Media enemyExplode;
	private Media playerExplode;
	private Media levelCompleted;
	private Media gameOver;
	private Media gameWon;
	
	private static MediaPlayer mediaPlayerBgm = new MediaPlayer(bgm);
	private MediaPlayer mediaPlayerMenu;
	private MediaPlayer mediaPlayerCountDown;
	private MediaPlayer mediaPlayerCoinPickUp;
	private MediaPlayer mediaPlayerLaserPickUp;
	private MediaPlayer mediaPlayerLifePickUp;
	private MediaPlayer mediaPlayerLaserEmpty;
	private MediaPlayer mediaPlayerLaserShoot;
	private MediaPlayer mediaPlayerEnemyExplode;
	private MediaPlayer mediaPlayerPlayerExplode;
	private MediaPlayer mediaPlayerLevelCompleted;
	private MediaPlayer mediaPlayerGameOver;
	private MediaPlayer mediaPlayerGameWon;
	
	private VolumeShip volumeShip1;
	private VolumeShip volumeShip2;
	private VolumeShip volumeShip3;
	private VolumeShip volumeShip4;
	private VolumeShip volumeShip5;
	
	private VolumeShip volumeShip6;
	private VolumeShip volumeShip7;
	private VolumeShip volumeShip8;
	private VolumeShip volumeShip9;
	private VolumeShip volumeShip0;
	
	private static HBox bgmVolBox;
	private static HBox igmVolBox;
	
	private static double backGroundMusicVolume = 1.0;
	private static double inGameMusicVolume = 1.0;
	
	private static double pastBGMVolume;
	private double volumeToSet = 0;
	
	public static boolean stopTimer = true;
	public static boolean bgmVolumeBeforeReached = true;
	
	private Timer fadeInTimer;
	
	/**
	 * Plays BackGround Music.
	 */
	public void playBackGroundMusic() {
		
		mediaPlayerBgm.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayerBgm.setVolume(backGroundMusicVolume);
		mediaPlayerBgm.play();
		
	}
	
	/**
	 * Plays Menu Open Music.
	 */
	public void playMenuOpenMusic() {
		
		menuMusic = new Media(Paths.get("src/resources/Sounds/Fastinvader-sound.mp3").toUri().toString());
		mediaPlayerMenu = new MediaPlayer(menuMusic);
		mediaPlayerMenu.setVolume(backGroundMusicVolume);
		mediaPlayerMenu.setAutoPlay(true);
		
	}
	
	/**
	 * Plays BackGround Music with a Fade-in effect using Timer and TimerTask.
	 */
	public void bgmFadeIn() {
		
		int fadeDuration = 5000;
		int fadeInterval = 250;
		int numberOfSteps = fadeDuration/fadeInterval;
		double deltaVolume = getBGMVolumeBeforeGame() / (double) numberOfSteps;
		
		stopTimer = false;
		volumeToSet = 0;
		fadeInTimer = new Timer();
		
		TimerTask fadeInTimerTask = new TimerTask() {
			
			@Override
			public void run() {
				
				fadeIn(deltaVolume);
				
				if(volumeToSet >= getBGMVolumeBeforeGame()) {
					
					fadeInTimer.cancel();
					fadeInTimer.purge();
					setBackGroundMusicVolume(pastBGMVolume);
					bgmVolumeBeforeReached = true;
					volumeToSet = 0;
					
				}
							
			}
		};
		
		fadeInTimer.schedule(fadeInTimerTask, fadeInterval, fadeInterval);
		
	}
	
	/**
	 * Called by bgmFadeIn() to apply Fade-in effect.
	 * 
	 * @param deltaVolume  small value of volume to be increased. 
	 */
	private void fadeIn(double deltaVolume) {
		
		try {
			
			if (stopTimer == false) {
			
				mediaPlayerBgm.setVolume(volumeToSet);
				
				if(mediaPlayerBgm.isMute() == true) {
					
					mediaPlayerBgm.setMute(false);
					
				}
				
				volumeToSet += deltaVolume;
				
				if (volumeToSet == 1) {
					
					volumeShip2.setVolumeShip(true);
					volumeShip3.setVolumeShip(true);
					volumeShip4.setVolumeShip(true);
					volumeShip5.setVolumeShip(true);
					
				} else if (volumeToSet >= 0.75) {
					
					volumeShip2.setVolumeShip(true);
					volumeShip3.setVolumeShip(true);
					volumeShip4.setVolumeShip(true);
					volumeShip5.setVolumeShip(false);
					
				} else if (volumeToSet >= 0.50) {
					
					volumeShip2.setVolumeShip(true);
					volumeShip3.setVolumeShip(true);
					volumeShip4.setVolumeShip(false);
					volumeShip5.setVolumeShip(false);
					
				} else if (volumeToSet == 0.25) {
					
					volumeShip2.setVolumeShip(true);
					volumeShip3.setVolumeShip(false);
					volumeShip4.setVolumeShip(false);
					volumeShip5.setVolumeShip(false);
					
				}
				
			} else {
				
				fadeInTimer.cancel();
				fadeInTimer.purge();
				
			}
		
		} catch(NullPointerException e) {}
		
	}
	
	/**
	 * Sets BGM VolumeShips according to BackGround Music.  
	 */
	public void setBgmVolumeShips() {
		
		if (getBackGroundMusicVolume() == 0) {
			
			volumeShip2.setVolumeShip(false);
			volumeShip3.setVolumeShip(false);
			volumeShip4.setVolumeShip(false);
			volumeShip5.setVolumeShip(false);
			
		} else if (getBackGroundMusicVolume() == 0.25) {
			
			volumeShip2.setVolumeShip(true);
			volumeShip3.setVolumeShip(false);
			volumeShip4.setVolumeShip(false);
			volumeShip5.setVolumeShip(false);
			
		} else if (getBackGroundMusicVolume() == 0.5) {
			
			volumeShip2.setVolumeShip(true);
			volumeShip3.setVolumeShip(true);
			volumeShip4.setVolumeShip(false);
			volumeShip5.setVolumeShip(false);
			
		} else if (getBackGroundMusicVolume() == 0.75) {
			
			volumeShip2.setVolumeShip(true);
			volumeShip3.setVolumeShip(true);
			volumeShip4.setVolumeShip(true);
			volumeShip5.setVolumeShip(false);
			
		} else if (getBackGroundMusicVolume() == 1) {
			
			volumeShip2.setVolumeShip(true);
			volumeShip3.setVolumeShip(true);
			volumeShip4.setVolumeShip(true);
			volumeShip5.setVolumeShip(true);
			
		}
		
	}
	
	/**
	 * Makes a HBox of BGM VolumeShips.
	 * 
	 * @return bgmVolBox  HBox containing BGM VolumeShips.
	 */
	public HBox bgmVolumeShips() {
		
		bgmVolBox = new HBox();
		volumeShip1 = new VolumeShip();
		volumeShip2 = new VolumeShip();
		volumeShip3 = new VolumeShip();
		volumeShip4 = new VolumeShip();
		volumeShip5 = new VolumeShip();
		
		setBgmVolumeShips();
		
		bgmVolBox.getChildren().add(volumeShip1);
		volumeShip1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0);
				volumeShip2.setVolumeShip(false);
				volumeShip3.setVolumeShip(false);
				volumeShip4.setVolumeShip(false);
				volumeShip5.setVolumeShip(false);
				stopTimer = true;
				
			}
			
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip2);
		volumeShip2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0.25);
				volumeShip2.setVolumeShip(true);
				volumeShip3.setVolumeShip(false);
				volumeShip4.setVolumeShip(false);
				volumeShip5.setVolumeShip(false);
				stopTimer = true;
				
			}
			
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip3);
		volumeShip3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0.50);
				volumeShip2.setVolumeShip(true);
				volumeShip3.setVolumeShip(true);
				volumeShip4.setVolumeShip(false);
				volumeShip5.setVolumeShip(false);
				stopTimer = true;
				
			}
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip4);
		volumeShip4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0.75);
				volumeShip2.setVolumeShip(true);
				volumeShip3.setVolumeShip(true);
				volumeShip4.setVolumeShip(true);
				volumeShip5.setVolumeShip(false);
				stopTimer = true;
				
			}
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip5);
		volumeShip5.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(1);
				volumeShip2.setVolumeShip(true);
				volumeShip3.setVolumeShip(true);
				volumeShip4.setVolumeShip(true);
				volumeShip5.setVolumeShip(true);
				stopTimer = true;
				
			}
		});
		
		bgmVolBox.setLayoutX(55);
		bgmVolBox.setLayoutY(100);
		
		return bgmVolBox;
		
	}
	
	/**
	 * Plays CountDown Music before game starts.
	 */
	public void playCountDownMusic() {
		
		countDown = new Media(Paths.get("src/resources/Sounds/Rocketleague-Countdown-sound.mp3").toUri().toString());
		mediaPlayerCountDown = new MediaPlayer(countDown);
		mediaPlayerCountDown.setVolume(inGameMusicVolume);
		mediaPlayerCountDown.setAutoPlay(true);
		
	}
	
	/**
	 * Plays sound when a coin is picked up.
	 */
	public void playCoinPickUpMusic() {
			
		coinPickUp = new Media(Paths.get("src/resources/Sounds/Pokemon-Coin-sound.mp3").toUri().toString());
		mediaPlayerCoinPickUp = new MediaPlayer(coinPickUp);
		mediaPlayerCoinPickUp.setVolume(inGameMusicVolume);
		mediaPlayerCoinPickUp.setAutoPlay(true);
		
	}
	
	/**
	 * Plays sound when laser power up is picked up.
	 */
	public void playLaserPickUpMusic() {
		
		laserPickUp = new Media(Paths.get("src/resources/Sounds/Laser-PickUp-sound.mp3").toUri().toString());
		mediaPlayerLaserPickUp = new MediaPlayer(laserPickUp);
		mediaPlayerLaserPickUp.setVolume(inGameMusicVolume);
		mediaPlayerLaserPickUp.setAutoPlay(true);
		
	}
	
	/**
	 * Plays sound when life power up is picked up.
	 */
	public void playLifePickUpMusic() {
		
		lifePickUp = new Media(Paths.get("src/resources/Sounds/Life-PickUp-sound.mp3").toUri().toString());
		mediaPlayerLifePickUp = new MediaPlayer(lifePickUp);
		mediaPlayerLifePickUp.setVolume(inGameMusicVolume);
		mediaPlayerLifePickUp.setAutoPlay(true);
		
	}
	
	/*
	 * Plays sound when laser is empty.
	 */
	public void playLaserEmptyMusic() {
		
		laserEmpty = new Media(Paths.get("src/resources/Sounds/Laser-Empty-sound.mp3").toUri().toString());
		mediaPlayerLaserEmpty = new MediaPlayer(laserEmpty);
		mediaPlayerLaserEmpty.setVolume(inGameMusicVolume);
		mediaPlayerLaserEmpty.setAutoPlay(true);
		
	}
	
	/**
	 * Plays sound when player shoots a laser.
	 */
	public void playLaserShootMusic() {
		
		laserShoot = new Media(Paths.get("src/resources/Sounds/Laser-Shoot-sound.mp3").toUri().toString());
		mediaPlayerLaserShoot = new MediaPlayer(laserShoot);
		mediaPlayerLaserShoot.setVolume(inGameMusicVolume);
		mediaPlayerLaserShoot.setAutoPlay(true);
		
	}
	
	/**
	 * Plays sound when enemy is exploded.
	 */
	public void playEnemyExplodeMusic() {
			
		enemyExplode = new Media(Paths.get("src/resources/Sounds/Enemy-Explode-sound.mp3").toUri().toString());
		mediaPlayerEnemyExplode = new MediaPlayer(enemyExplode);
		mediaPlayerEnemyExplode.setVolume(inGameMusicVolume);
		mediaPlayerEnemyExplode.setAutoPlay(true);
		
	}
	
	/**
	 * Plays sound when player is exploded.
	 */
	public void playPlayerExplodeMusic() {
			
		playerExplode = new Media(Paths.get("src/resources/Sounds/Player-Explode-sound.mp3").toUri().toString());
		mediaPlayerPlayerExplode = new MediaPlayer(playerExplode);
		mediaPlayerPlayerExplode.setVolume(inGameMusicVolume);
		mediaPlayerPlayerExplode.setAutoPlay(true);	
		
	}
	
	/**
	 * Plays sound when level is completed.
	 */
	public void playLevelCompletedMusic() {
			
		levelCompleted = new Media(Paths.get("src/resources/Sounds/Level-Completed-sound.mp3").toUri().toString());
		mediaPlayerLevelCompleted = new MediaPlayer(levelCompleted);
		mediaPlayerLevelCompleted.setVolume(inGameMusicVolume);
		mediaPlayerLevelCompleted.setAutoPlay(true);	
		
	}
	
	/**
	 * Plays sound when game is over.
	 */
	public void playGameOverMusic() {
			
		gameOver = new Media(Paths.get("src/resources/Sounds/Game-Over-sound.mp3").toUri().toString());
		mediaPlayerGameOver = new MediaPlayer(gameOver);
		mediaPlayerGameOver.setVolume(inGameMusicVolume);
		mediaPlayerGameOver.setAutoPlay(true);	
		
	}
	
	/**
	 * Plays sound when game is won.
	 */
	public void playGameWonMusic() {
			
		gameWon = new Media(Paths.get("src/resources/Sounds/Game-Won-sound.mp3").toUri().toString());
		mediaPlayerGameWon = new MediaPlayer(gameWon);
		mediaPlayerGameWon.setVolume(inGameMusicVolume);
		mediaPlayerGameWon.setAutoPlay(true);	
		
	}
	
	/**
	 * sets IGM VolumeShips according to In-Game Music. 
	 */
	public void setIgmVolumeShips() {
		
		if (getInGameMusicVolume() == 0) {
			
			volumeShip7.setVolumeShip(false);
			volumeShip8.setVolumeShip(false);
			volumeShip9.setVolumeShip(false);
			volumeShip0.setVolumeShip(false);
			
		} else if (getInGameMusicVolume() == 0.25) {
			
			volumeShip7.setVolumeShip(true);
			volumeShip8.setVolumeShip(false);
			volumeShip9.setVolumeShip(false);
			volumeShip0.setVolumeShip(false);
			
		} else if (getInGameMusicVolume() == 0.5) {
			
			volumeShip7.setVolumeShip(true);
			volumeShip8.setVolumeShip(true);
			volumeShip9.setVolumeShip(false);
			volumeShip0.setVolumeShip(false);
			
		} else if (getInGameMusicVolume() == 0.75) {
			
			volumeShip7.setVolumeShip(true);
			volumeShip8.setVolumeShip(true);
			volumeShip9.setVolumeShip(true);
			volumeShip0.setVolumeShip(false);
			
		} else if (getInGameMusicVolume() == 1) {
			
			volumeShip7.setVolumeShip(true);
			volumeShip8.setVolumeShip(true);
			volumeShip9.setVolumeShip(true);
			volumeShip0.setVolumeShip(true);
			
		}
		
	}
	
	/**
	 * Makes a HBox of IGM VolumeShips.
	 * 
	 * @return igmVolBox  HBox containing IGM VolumeShips.
	 */
	public HBox igmVolumeShips() {
		
		igmVolBox = new HBox();
		volumeShip6 = new VolumeShip();
		volumeShip7 = new VolumeShip();
		volumeShip8 = new VolumeShip();
		volumeShip9 = new VolumeShip();
		volumeShip0 = new VolumeShip();
		
		setIgmVolumeShips();
		
		igmVolBox.getChildren().add(volumeShip6);
		volumeShip6.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0);
				volumeShip7.setVolumeShip(false);
				volumeShip8.setVolumeShip(false);
				volumeShip9.setVolumeShip(false);
				volumeShip0.setVolumeShip(false);
				
			}
		
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip7);
		volumeShip7.setOnMouseClicked(new EventHandler<MouseEvent>() {
		
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0.25);
				volumeShip7.setVolumeShip(true);
				volumeShip8.setVolumeShip(false);
				volumeShip9.setVolumeShip(false);
				volumeShip0.setVolumeShip(false);
				
			}
		
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip8);
		volumeShip8.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0.50);
				volumeShip7.setVolumeShip(true);
				volumeShip8.setVolumeShip(true);
				volumeShip9.setVolumeShip(false);
				volumeShip0.setVolumeShip(false);
		
			}
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip9);
		volumeShip9.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0.75);
				volumeShip7.setVolumeShip(true);
				volumeShip8.setVolumeShip(true);
				volumeShip9.setVolumeShip(true);
				volumeShip0.setVolumeShip(false);
		
			}
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip0);
		volumeShip0.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(1);
				volumeShip7.setVolumeShip(true);
				volumeShip8.setVolumeShip(true);
				volumeShip9.setVolumeShip(true);
				volumeShip0.setVolumeShip(true);
		
			}
		});
		
		igmVolBox.setLayoutX(55);
		igmVolBox.setLayoutY(275);
		
		return igmVolBox;
		
	}
	
	/**
	 * Sets parameter value to backGroundMusicVolume.
	 * 
	 * @param volume  value to be set to backGroundMusicVolume.
	 */
	public void setBackGroundMusicVolume(double volume) {
		
		try {
			backGroundMusicVolume = volume;
			if(backGroundMusicVolume == 0) {
				mediaPlayerBgm.setMute(true);
			} else {
				mediaPlayerBgm.setVolume(backGroundMusicVolume);
				mediaPlayerBgm.setMute(false);
			}
			setBgmVolumeShips();
		} catch (NullPointerException e) {}
		
	}
	
	/**
	 * @return backGroundMusicVolume  Volume of BackGround Music.
	 */
	public double getBackGroundMusicVolume() {
		return backGroundMusicVolume;	
	}
	
	/**
	 * Sets parameter value to pastBGMVolume.
	 * 
	 * @param volume  value to be set to pastBGMVolume.
	 */
	public static void setBGMVolumeBeforeGame(double volume) {
		pastBGMVolume = volume;
	}
	
	/**
	 * @return pastBGMVolume  Volume of BackGround Music before Game starts.
	 */
	public static double getBGMVolumeBeforeGame() {
		return pastBGMVolume;	
	}
	
	/**
	 * Sets parameter value to stopTimer.
	 * 
	 * @param val  value to be set to stopTimer.
	 */
	public void setStopTimer(boolean val) {
		stopTimer = val;	
	}
	
	/**
	 * @return stopTimer  boolean used to know whether the fadeInTimer has stopped.
	 */
	public boolean getStopTimer() {
		return stopTimer;
	}
	
	/**
	 * Sets parameter value to bgmVolumeBeforeReached.
	 * 
	 * @param val  value to be set to bgmVolumeBeforeReached.
	 */
	public void setBGMVolumeBeforeReached(boolean val) {
		bgmVolumeBeforeReached = val;	
	}
	
	/**
	 * @return bgmVolumeBeforeReached  boolean used to know whether backGroundMusicVolume is set to pastBGMVolume.
	 */
	public boolean getBGMVolumeBeforeReached() {
		return bgmVolumeBeforeReached;
	}
	
	/**
	 * @return bgmVolBox HBox used to contain the ships which is used to adjust BGM volumes.
	 */
	public HBox getBgmBox() {
		return bgmVolBox;
	}
	
	/**
	 * @return igmVolBox HBox used to contain the ships which is used to adjust IGM volumes.
	 */
	public HBox getIgmBox() {
		return igmVolBox;
	}
	
	/**
	 * Sets parameter value to inGameMusicVolume.
	 * 
	 * @param volume  value to be set to inGameMusicVolume.
	 */
	public static void setInGameMusicVolume(double volume) {
		inGameMusicVolume = volume;
	}
	
	/**
	 * @return inGameMusicVolume  volume of In-Game Music.
	 */
	public static double getInGameMusicVolume() {
		return inGameMusicVolume;
	}
	
}
