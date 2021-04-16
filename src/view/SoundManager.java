package view;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
	
	static Media bgm = new Media(Paths.get("src/view/resources/sounds/Spaceinvaders-sound.mp3").toUri().toString());
	
	static MediaPlayer mediaPlayerBgm = new MediaPlayer(bgm);
	MediaPlayer mediaPlayerMenu;
	MediaPlayer mediaPlayerCountDown;
	MediaPlayer mediaPlayerCoin;
	MediaPlayer mediaPlayerEnemyDead;
	MediaPlayer mediaPlayerPlayerDead;
	
	static double backGroundMusicVolume = 1.00;
	static double inGameMusicVolume = 1.00;
	
	public void playBackGroundMusic() {
		
		mediaPlayerBgm.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayerBgm.setVolume(backGroundMusicVolume);
		mediaPlayerBgm.play();
		
	}
	
	public void setBackGroundMusicVolume(double vol) {
		
		backGroundMusicVolume = vol;
		if(backGroundMusicVolume == 0) {
			mediaPlayerBgm.pause();
		} else {
			mediaPlayerBgm.play();
			mediaPlayerBgm.setVolume(backGroundMusicVolume);
		}
		
	}
	
	public double getBackGroundMusicVolume() {
		
		return backGroundMusicVolume;
		
	}
	
	public void playMenuOpenMusic() {
		
		Media menuMusic = new Media(Paths.get("src/view/resources/sounds/Fastinvader-sound.mp3").toUri().toString());
		MediaPlayer mediaPlayerMenu = new MediaPlayer(menuMusic);
		mediaPlayerMenu.setVolume(backGroundMusicVolume);
		mediaPlayerMenu.setAutoPlay(true);
		
	}
	
	public void playCountDownMusic() {
		
		Media countDown = new Media(Paths.get("src/view/resources/sounds/Rocketleague-Countdown-sound.mp3").toUri().toString());
		MediaPlayer mediaPlayerCountDown = new MediaPlayer(countDown);
		mediaPlayerCountDown.setVolume(inGameMusicVolume);
		mediaPlayerCountDown.setAutoPlay(true);
		
	}
	
	public void playCoinCollectMusic() {
			
		Media coin = new Media(Paths.get("src/view/resources/sounds/Pokemon-Coin-sound.mp3").toUri().toString());
		MediaPlayer mediaPlayerCoin = new MediaPlayer(coin);
		mediaPlayerCoin.setVolume(inGameMusicVolume);
		mediaPlayerCoin.setAutoPlay(true);
		
	}
	
	public void playEnemyDeadMusic() {
			
		Media enemyDead = new Media(Paths.get("src/view/resources/sounds/Explosion-sound.mp3").toUri().toString());
		MediaPlayer mediaPlayerEnemyDead = new MediaPlayer(enemyDead);
		mediaPlayerEnemyDead.setVolume(inGameMusicVolume);
		mediaPlayerEnemyDead.setAutoPlay(true);
		
	}
	
	public void playPlayerDeadMusic() {
			
		Media playerDead = new Media(Paths.get("src/view/resources/sounds/Roblox-Death-sound.mp3").toUri().toString());
		MediaPlayer mediaPlayerPlayerDead = new MediaPlayer(playerDead);
		mediaPlayerPlayerDead.setVolume(inGameMusicVolume);
		mediaPlayerPlayerDead.setAutoPlay(true);	
		
	}
	
	public void setInGameMusicVolume(double vol) {
		
		inGameMusicVolume = vol;
		
	}
	
	public double getInGameMusicVolume() {
		
		return inGameMusicVolume;
	}

}
