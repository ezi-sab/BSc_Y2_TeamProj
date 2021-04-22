package view;

import java.nio.file.Paths;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.VolumeShip;

public class SoundManager {
	
	private static Media bgm = new Media(Paths.get("src/view/resources/sounds/Spaceinvaders-sound.mp3").toUri().toString());
	private Media menuMusic;
	private Media countDown;
	private Media coin;
	private Media enemyDead;
	private Media playerDead;
	
	private static MediaPlayer mediaPlayerBgm = new MediaPlayer(bgm);
	private MediaPlayer mediaPlayerMenu;
	private MediaPlayer mediaPlayerCountDown;
	private MediaPlayer mediaPlayerCoin;
	private MediaPlayer mediaPlayerEnemyDead;
	private MediaPlayer mediaPlayerPlayerDead;
	
	private static VolumeShip volumeShip1;
	private static VolumeShip volumeShip2;
	private static VolumeShip volumeShip3;
	private static VolumeShip volumeShip4;
	private static VolumeShip volumeShip5;
	
	private static VolumeShip volumeShip6;
	private static VolumeShip volumeShip7;
	private static VolumeShip volumeShip8;
	private static VolumeShip volumeShip9;
	private static VolumeShip volumeShip0;
	
	private static HBox bgmVolBox;
	private static HBox igmVolBox;
	
	private static double backGroundMusicVolume = 1.00;
	private static double inGameMusicVolume = 0.75;
	
	private static double pastBGMVolume;
	
	public void playBackGroundMusic() {
		
		mediaPlayerBgm.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayerBgm.setVolume(backGroundMusicVolume);
		mediaPlayerBgm.play();
		
	}
	
	public void playMenuOpenMusic() {
		
		menuMusic = new Media(Paths.get("src/view/resources/sounds/Fastinvader-sound.mp3").toUri().toString());
		mediaPlayerMenu = new MediaPlayer(menuMusic);
		mediaPlayerMenu.setVolume(backGroundMusicVolume);
		mediaPlayerMenu.setAutoPlay(true);
		
	}
	
	public void setBackGroundMusicVolume(double volume) {
		
		backGroundMusicVolume = volume;
		if(backGroundMusicVolume == 0) {
			mediaPlayerBgm.pause();
		} else {
			mediaPlayerBgm.play();
			mediaPlayerBgm.setVolume(backGroundMusicVolume);
		}
		
	}
	
	public double getbackGroundMusicVolume() {
		
		return backGroundMusicVolume;
		
	}
	
	public void setBGMVolumeBeforeGame() {
		
		pastBGMVolume = getbackGroundMusicVolume();
		
	}
	
	public double getBGMVolumeBeforeGame() {
		
		return pastBGMVolume;
		
	}
	
	public void setBgmVolumeShips() {
		
		if (getbackGroundMusicVolume() == 0) {
			
			volumeShip2.setVolume(false);
			volumeShip3.setVolume(false);
			volumeShip4.setVolume(false);
			volumeShip5.setVolume(false);
			
		} else if (getbackGroundMusicVolume() == 0.25) {
			
			volumeShip2.setVolume(true);
			volumeShip3.setVolume(false);
			volumeShip4.setVolume(false);
			volumeShip5.setVolume(false);
			
		} else if (getbackGroundMusicVolume() == 0.5) {
			
			volumeShip2.setVolume(true);
			volumeShip3.setVolume(true);
			volumeShip4.setVolume(false);
			volumeShip5.setVolume(false);
			
		} else if (getbackGroundMusicVolume() == 0.75) {
			
			volumeShip2.setVolume(true);
			volumeShip3.setVolume(true);
			volumeShip4.setVolume(true);
			volumeShip5.setVolume(false);
			
		} else if (getbackGroundMusicVolume() == 1) {
			
			volumeShip2.setVolume(true);
			volumeShip3.setVolume(true);
			volumeShip4.setVolume(true);
			volumeShip5.setVolume(true);
			
		}
		
	}
	
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
				volumeShip2.setVolume(false);
				volumeShip3.setVolume(false);
				volumeShip4.setVolume(false);
				volumeShip5.setVolume(false);
				
			}
			
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip2);
		volumeShip2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0.25);
				volumeShip2.setVolume(true);
				volumeShip3.setVolume(false);
				volumeShip4.setVolume(false);
				volumeShip5.setVolume(false);
				
			}
			
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip3);
		volumeShip3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0.50);
				volumeShip2.setVolume(true);
				volumeShip3.setVolume(true);
				volumeShip4.setVolume(false);
				volumeShip5.setVolume(false);
				
			}
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip4);
		volumeShip4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(0.75);
				volumeShip2.setVolume(true);
				volumeShip3.setVolume(true);
				volumeShip4.setVolume(true);
				volumeShip5.setVolume(false);
				
			}
		});
		
		bgmVolBox.setSpacing(10);
		
		bgmVolBox.getChildren().add(volumeShip5);
		volumeShip5.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
				setBackGroundMusicVolume(1);
				volumeShip2.setVolume(true);
				volumeShip3.setVolume(true);
				volumeShip4.setVolume(true);
				volumeShip5.setVolume(true);
				
			}
		});
		
		bgmVolBox.setLayoutX(35);
		bgmVolBox.setLayoutY(100);
		
		return bgmVolBox;
		
	}
	
	public void playCountDownMusic() {
		
		countDown = new Media(Paths.get("src/view/resources/sounds/Rocketleague-Countdown-sound.mp3").toUri().toString());
		mediaPlayerCountDown = new MediaPlayer(countDown);
		mediaPlayerCountDown.setVolume(inGameMusicVolume);
		mediaPlayerCountDown.setAutoPlay(true);
		
	}
	
	public void playCoinCollectMusic() {
			
		coin = new Media(Paths.get("src/view/resources/sounds/Pokemon-Coin-sound.mp3").toUri().toString());
		mediaPlayerCoin = new MediaPlayer(coin);
		mediaPlayerCoin.setVolume(inGameMusicVolume);
		mediaPlayerCoin.setAutoPlay(true);
		
	}
	
	public void playEnemyDeadMusic() {
			
		enemyDead = new Media(Paths.get("src/view/resources/sounds/Explosion-sound.mp3").toUri().toString());
		mediaPlayerEnemyDead = new MediaPlayer(enemyDead);
		mediaPlayerEnemyDead.setVolume(inGameMusicVolume);
		mediaPlayerEnemyDead.setAutoPlay(true);
		
	}
	
	public void playPlayerDeadMusic() {
			
		playerDead = new Media(Paths.get("src/view/resources/sounds/Roblox-Death-sound.mp3").toUri().toString());
		mediaPlayerPlayerDead = new MediaPlayer(playerDead);
		mediaPlayerPlayerDead.setVolume(inGameMusicVolume);
		mediaPlayerPlayerDead.setAutoPlay(true);	
		
	}
	
	public void setInGameMusicVolume(double volume) {
		
		inGameMusicVolume = volume;
		
	}
	
	public double getInGameMusicVolume() {
		
		return inGameMusicVolume;
		
	}
	
	public void setIgmVolumeShips() {
		
		if (getInGameMusicVolume() == 0) {
			
			volumeShip7.setVolume(false);
			volumeShip8.setVolume(false);
			volumeShip9.setVolume(false);
			volumeShip0.setVolume(false);
			
		} else if (getInGameMusicVolume() == 0.25) {
			
			volumeShip7.setVolume(true);
			volumeShip8.setVolume(false);
			volumeShip9.setVolume(false);
			volumeShip0.setVolume(false);
			
		} else if (getInGameMusicVolume() == 0.5) {
			
			volumeShip7.setVolume(true);
			volumeShip8.setVolume(true);
			volumeShip9.setVolume(false);
			volumeShip0.setVolume(false);
			
		} else if (getInGameMusicVolume() == 0.75) {
			
			volumeShip7.setVolume(true);
			volumeShip8.setVolume(true);
			volumeShip9.setVolume(true);
			volumeShip0.setVolume(false);
			
		} else if (getInGameMusicVolume() == 1) {
			
			volumeShip7.setVolume(true);
			volumeShip8.setVolume(true);
			volumeShip9.setVolume(true);
			volumeShip0.setVolume(true);
			
		}
		
	}
	
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
				volumeShip7.setVolume(false);
				volumeShip8.setVolume(false);
				volumeShip9.setVolume(false);
				volumeShip0.setVolume(false);
				
			}
		
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip7);
		volumeShip7.setOnMouseClicked(new EventHandler<MouseEvent>() {
		
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0.25);
				volumeShip7.setVolume(true);
				volumeShip8.setVolume(false);
				volumeShip9.setVolume(false);
				volumeShip0.setVolume(false);
				
			}
		
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip8);
		volumeShip8.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0.50);
				volumeShip7.setVolume(true);
				volumeShip8.setVolume(true);
				volumeShip9.setVolume(false);
				volumeShip0.setVolume(false);
		
			}
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip9);
		volumeShip9.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(0.75);
				volumeShip7.setVolume(true);
				volumeShip8.setVolume(true);
				volumeShip9.setVolume(true);
				volumeShip0.setVolume(false);
		
			}
		});
		
		igmVolBox.setSpacing(10);
		
		igmVolBox.getChildren().add(volumeShip0);
		volumeShip0.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
			
				setInGameMusicVolume(1);
				volumeShip7.setVolume(true);
				volumeShip8.setVolume(true);
				volumeShip9.setVolume(true);
				volumeShip0.setVolume(true);
		
			}
		});
		
		igmVolBox.setLayoutX(35);
		igmVolBox.setLayoutY(275);
		
		return igmVolBox;
		
	}

}
